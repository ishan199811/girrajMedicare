package com.girrajmedico.girrajmedico.compiler;

import org.springframework.stereotype.Service;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeExecutionService {

    private static final Path BASE_CODE_DIR = Paths.get("src", "main", "resources", "java");
    private static final long TIMEOUT_SECONDS = 5;

    // Define the default imports to be added
    private static final String DEFAULT_IMPORTS =
            "import java.util.Collection;\n" +
            "import java.util.List;\n" +
            "import java.util.Set;\n" +
            "import java.util.Map;\n" +
            "import java.util.ArrayList;\n" + // Common concrete implementations
            "import java.util.HashMap;\n" +
            "import java.util.HashSet;\n";

    public String executeJavaCode(String javaCode) {
        // Inject default imports into the user's Java code
        String modifiedJavaCode = injectDefaultImports(javaCode);

        String className = extractClassName(modifiedJavaCode);
        String packageName = extractPackageName(modifiedJavaCode);
        String fullyQualifiedClassName = (packageName != null && !packageName.isEmpty()) ?
                packageName + "." + className : className;

        if (className == null) {
            return "Error: Could not determine public class name from provided code. Make sure it contains a public class.";
        }

        Path targetSourceDir = BASE_CODE_DIR;
        if (packageName != null && !packageName.isEmpty()) {
            targetSourceDir = BASE_CODE_DIR.resolve(packageName.replace('.', File.separatorChar));
        }

        Path sourceFilePath = targetSourceDir.resolve(className + ".java");
        Path classFilePath = BASE_CODE_DIR.resolve(
                (packageName != null && !packageName.isEmpty() ? packageName.replace('.', File.separatorChar) + File.separator : "") +
                        className + ".class"
        );

        try {
            Files.createDirectories(targetSourceDir);

            try (FileWriter writer = new FileWriter(sourceFilePath.toFile())) {
                writer.write(modifiedJavaCode); // Write the modified code
            }
            System.out.println("Source file written to: " + sourceFilePath);

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return "Error: JDK not found. The application must be run with a JDK, not just a JRE.";
            }

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFilePath.toFile()));

            List<String> options = Arrays.asList("-d", BASE_CODE_DIR.toAbsolutePath().toString());
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);

            boolean success = task.call();
            fileManager.close();

            StringBuilder output = new StringBuilder();

            if (!success) {
                output.append("Compilation Failed:\n");
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    output.append(String.format(Locale.getDefault(), "Error on line %d in %s%n%s%n",
                            diagnostic.getLineNumber(),
                            diagnostic.getSource().getKind() == JavaFileObject.Kind.SOURCE ? diagnostic.getSource().getName() : "Unknown source",
                            diagnostic.getMessage(Locale.getDefault())));
                }
                return output.toString();
            }

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", BASE_CODE_DIR.toAbsolutePath().toString(), fullyQualifiedClassName);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                output.append("\nExecution Timed Out after ").append(TIMEOUT_SECONDS).append(" seconds.\n");
            } else if (process.exitValue() != 0) {
                output.append("\nProcess exited with non-zero status: ").append(process.exitValue()).append("\n");
            }

            return output.toString();

        } catch (IOException | InterruptedException e) {
            return "Execution Error: " + e.getMessage();
        } finally {
            if (Files.exists(sourceFilePath)) {
                try {
                    Files.delete(sourceFilePath);
                    System.out.println("Cleaned up source file: " + sourceFilePath);
                } catch (IOException e) {
                    System.err.println("Failed to clean up source file: " + sourceFilePath + " - " + e.getMessage());
                }
            }
            if (Files.exists(classFilePath)) {
                try {
                    Files.delete(classFilePath);
                    System.out.println("Cleaned up class file: " + classFilePath);
                } catch (IOException e) {
                    System.err.println("Failed to clean up class file: " + classFilePath + " - " + e.getMessage());
                }
            }
            if (packageName != null && !packageName.isEmpty()) {
                Path currentDir = targetSourceDir;
                while (currentDir != null && !currentDir.equals(BASE_CODE_DIR) && Files.exists(currentDir)) {
                    try {
                        if (Files.isDirectory(currentDir) && Files.list(currentDir).findAny().isEmpty()) {
                            Files.delete(currentDir);
                            System.out.println("Cleaned up empty directory: " + currentDir);
                            currentDir = currentDir.getParent();
                        } else {
                            break;
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to clean up directory: " + currentDir + " - " + e.getMessage());
                        break;
                    }
                }
            }
        }
    }

    // Helper method to extract the public class name from the Java code string
    private String extractClassName(String javaCode) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+([a-zA-Z_$][a-zA-Z0-9_$]*)\\s*\\{");
        Matcher matcher = pattern.matcher(javaCode);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Helper method to extract the package name from the Java code string
    private String extractPackageName(String javaCode) {
        Pattern pattern = Pattern.compile("package\\s+([a-zA-Z_][a-zA-Z0-9_\\.]*);");
        Matcher matcher = pattern.matcher(javaCode);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Injects default import statements into the provided Java code.
     * It attempts to place them after the package declaration, or at the very beginning.
     *
     * @param javaCode The original Java code string.
     * @return The Java code string with default imports injected.
     */
    private String injectDefaultImports(String javaCode) {
        // Regex to find the package declaration, including line breaks
        Pattern packagePattern = Pattern.compile("(package\\s+[a-zA-Z_][a-zA-Z0-9_\\.]*;\\s*)");
        Matcher packageMatcher = packagePattern.matcher(javaCode);

        if (packageMatcher.find()) {
            // If a package declaration is found, insert imports after it
            return packageMatcher.group(1) + "\n" + DEFAULT_IMPORTS + javaCode.substring(packageMatcher.end());
        } else {
            // If no package declaration, insert imports at the very beginning
            return DEFAULT_IMPORTS + javaCode;
        }
    }
}