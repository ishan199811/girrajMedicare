package com.girrajmedico.girrajmedico.service;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiService {

	
	
	private final OllamaChatModel model ;
	
	public AiService(OllamaChatModel model) {
		this.model=model;
	}
	
	
	public String getAiResponse(String inputText) {

	
		String aiResponse=model.call(inputText);
		
		return aiResponse;
	}
}
