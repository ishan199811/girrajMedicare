package com.girrajmedico.girrajmedico.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaytmConfig {

    @Value("${paytm.merchant.id}")
    private String mid;

    @Value("${paytm.merchant.key}")
    private String merchantKey;

    @Value("${paytm.channel.id}")
    private String channelId;

    @Value("${paytm.industry.type}")
    private String industryTypeId;

    @Value("${paytm.website}")
    private String website;

    @Value("${paytm.callback.url}")
    private String callbackUrl;

    @Value("${paytm.transaction.url}")
    private String transactionUrl;

    public Map<String, String> getPaytmDetails() {
        Map<String, String> details = new HashMap<>();
        details.put("MID", mid);
        details.put("MERCHANT_KEY", merchantKey);
        details.put("CHANNEL_ID", channelId);
        details.put("INDUSTRY_TYPE_ID", industryTypeId);
        details.put("WEBSITE", website);
        details.put("CALLBACK_URL", callbackUrl);
        details.put("TXN_URL", transactionUrl);
        return details;
    }
}