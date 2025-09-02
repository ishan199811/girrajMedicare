package com.girrajmedico.girrajmedico.controller;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.configuration.PaytmConfig;
import com.girrajmedico.girrajmedico.generator.CheckSumGenerator;

@RestController
@RequestMapping("/paytm")
public class PaytmController {

    @Autowired
    private PaytmConfig paytmConfig;

    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestParam String orderId, @RequestParam String amount) {
        try {
            Map<String, String> paytmParams = new TreeMap<>();
            Map<String, String> paytmDetails = paytmConfig.getPaytmDetails();

            paytmParams.put("MID", paytmDetails.get("MID"));
            paytmParams.put("ORDER_ID", orderId);
            paytmParams.put("CUST_ID", "CUSTOMER123");
            paytmParams.put("MOBILE_NO", "7777777777");
            paytmParams.put("EMAIL", "customer@example.com");
            paytmParams.put("CHANNEL_ID", paytmDetails.get("CHANNEL_ID"));
            paytmParams.put("TXN_AMOUNT", amount);
            paytmParams.put("WEBSITE", paytmDetails.get("WEBSITE"));
            paytmParams.put("INDUSTRY_TYPE_ID", paytmDetails.get("INDUSTRY_TYPE_ID"));
            paytmParams.put("CALLBACK_URL", paytmDetails.get("CALLBACK_URL"));

            String checksum = CheckSumGenerator.generateChecksum(paytmDetails.get("MERCHANT_KEY"), paytmParams);
            paytmParams.put("CHECKSUMHASH", checksum);

            return ResponseEntity.ok(paytmParams);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/paytmCallback")
    public ResponseEntity<String> paytmCallback(@RequestParam Map<String, String> allParams) {
        // Verify checksum and transaction response
        return ResponseEntity.ok("Payment Callback received: " + allParams.toString());
    }
}
