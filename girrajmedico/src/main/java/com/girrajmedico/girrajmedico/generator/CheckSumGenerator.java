package com.girrajmedico.girrajmedico.generator;

import java.util.Map;
import java.util.TreeMap;

public class CheckSumGenerator {
    public static String generateChecksum(String merchantKey, Map<String, String> paytmParams) throws Exception {
        // Make sure parameters are sorted (TreeMap does this automatically)
        TreeMap<String, String> sortedParams = new TreeMap<>(paytmParams);
//        PaytmChecksum.generateSignature(sortedParams, merchantKey);
        return null;
    }
}
