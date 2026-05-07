package com.girrajmedico.girrajmedico.controller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.service.MedicineService;

@RestController
@RequestMapping("/api/files")
public class CsvToJsonController {

	@Autowired
    private MedicineService medicineService;
	
	@PostMapping("/csv-to-json-stream")
	public ResponseEntity<StreamingResponseBody> streamCsvToJson(@RequestParam("file") MultipartFile file) {
	    
	    StreamingResponseBody stream = outputStream -> {
	        try (InputStream inputStream = file.getInputStream()) {
	            CsvMapper csvMapper = new CsvMapper();
	            CsvSchema schema = CsvSchema.emptySchema().withHeader();
	            MappingIterator<Map<String, String>> iterator = csvMapper
	                    .readerFor(Map.class)
	                    .with(schema)
	                    .readValues(inputStream);

	            // Write the JSON array bracket manually
	            outputStream.write("[".getBytes());
	            
	            boolean isFirst = true;
	            ObjectMapper jsonMapper = new ObjectMapper();

	            // Stream row by row (Uses almost zero RAM!)
	            while (iterator.hasNext()) {
	                if (!isFirst) {
	                    outputStream.write(",".getBytes());
	                }
	                outputStream.write(jsonMapper.writeValueAsBytes(iterator.next()));
	                outputStream.flush(); // Send to client immediately
	                isFirst = false;
	            }
	            outputStream.write("]".getBytes());
	        }
	    };

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.json\"")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(stream);
	}
	
	// ✅ NEW ENDPOINT: Save directly to Database with Error Tracking
    @PostMapping("/upload-medicine-csv")
    public ResponseEntity<?> uploadMedicineCsvToDb(@RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }

        int currentRow = 1; // Row 1 is usually the header
        int successCount = 0;
        List<String> failedRows = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<Map<String, String>> iterator = csvMapper
                    .readerFor(Map.class)
                    .with(schema)
                    .readValues(inputStream);

            while (iterator.hasNext()) {
                currentRow++; // Data starts at row 2
                try {
                    Map<String, String> row = iterator.next();
                    
                    Medicine medicine = new Medicine();
                    
                    // Map CSV columns to Entity fields based on your JSON schema
                    medicine.setMedicineName(row.get("name"));
                    medicine.setPrice(safeParseDouble(row.get("price")));
                    medicine.setIsDiscontinued(row.get("Is_discontinued"));
                    medicine.setManufacturerName(row.get("manufacturer_name"));
                    medicine.setMedineType(row.get("type"));
                    medicine.setPackSizeLabel(row.get("pack_size_label"));
                    medicine.setShortComposition1(row.get("short_composition1"));
                    medicine.setShortComposition2(row.get("short_composition2"));
                    medicine.setDescription(row.get("medicine_desc"));
                    
                    // Set default values for fields that might be null to prevent Math errors in Service
                    medicine.setDiscountPercentage(0.0); 
                    medicine.setTotalPiece(100.0); // Setting a default inventory count
                    
                    // Call your service to calculate discounts and save
                    medicineService.saveMedicine(medicine);
                    successCount++;
                    
                } catch (Exception e) {
                    // If parsing or saving fails, record the row number and the error message
                    failedRows.add("Row " + currentRow + " failed: " + e.getMessage());
                }
            }

            // Prepare a final report to send back to the React UI
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upload process completed.");
            response.put("successful_inserts", successCount);
            response.put("failed_inserts", failedRows.size());
            response.put("error_details", failedRows);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Critical error processing file: " + e.getMessage());
        }
    }

    /**
     * Helper method to safely parse doubles. 
     * If the CSV price column is empty or has text, it returns 0.0 instead of crashing the whole app.
     */
    private double safeParseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}