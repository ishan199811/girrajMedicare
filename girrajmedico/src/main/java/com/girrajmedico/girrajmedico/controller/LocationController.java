package com.girrajmedico.girrajmedico.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.girrajmedico.girrajmedico.model.dao.Location;

//LocationController.java
@RestController
@RequestMapping("/api")
public class LocationController {

	@PostMapping("/saveLocation")
	public ResponseEntity<String> getLocation(@RequestBody Location location) {
	    double lat = location.getLatitude();
	    double lon = location.getLongitude();

	    String address = getAddressFromCoordinates(lat, lon);
	    return ResponseEntity.ok(address);
	}

	private String getAddressFromCoordinates(double lat, double lon) {
	    String url = "https://nominatim.openstreetmap.org/reverse?" +
	                 "format=json&lat=" + lat + "&lon=" + lon +
	                 "&addressdetails=1&namedetails=1&zoom=19";

	    try {
	        RestTemplate restTemplate = new RestTemplate();

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("User-Agent", "GirrajMedico/1.0 (ishansharma199811@gmail.com)");

	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode() == HttpStatus.OK) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode root = objectMapper.readTree(response.getBody());

	            JsonNode address = root.path("address");
	            JsonNode nameDetails = root.path("namedetails");

	            if (address.isMissingNode()) {
	                return "Address not found";
	            }

	            StringBuilder fullAddress = new StringBuilder();

	            appendIfExists(fullAddress, address, "house_number");
	            appendIfExists(fullAddress, address, "building", ", ");
	            appendIfExists(fullAddress, address, "road", ", ");
	            appendIfExists(fullAddress, address, "residential", ", ");
	            appendIfExists(fullAddress, address, "suburb", ", ");
	            appendIfExists(fullAddress, address, "city", ", ");
	            appendIfExists(fullAddress, address, "state", ", ");
	            appendIfExists(fullAddress, address, "postcode", ", ");
	            appendIfExists(fullAddress, address, "country");

	            return fullAddress.toString().trim();

	        } else {
	            System.err.println("Error: " + response.getStatusCode() + " - Unable to fetch address.");
	            return "Error fetching address";
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error fetching address";
	    }
	}

	private void appendIfExists(StringBuilder builder, JsonNode node, String field) {
	    appendIfExists(builder, node, field, " ");
	}

	private void appendIfExists(StringBuilder builder, JsonNode node, String field, String suffix) {
	    String value = node.path(field).asText(null);
	    if (value != null && !value.isEmpty()) {
	        builder.append(value).append(suffix);
	    }
	}

}