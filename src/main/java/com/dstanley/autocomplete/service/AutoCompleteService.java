package com.dstanley.autocomplete.service;

import com.dstanley.autocomplete.api.AutoCompleteResponseDTO;

public interface AutoCompleteService {

	public AutoCompleteResponseDTO getSuggestions(String prefix);

	public AutoCompleteResponseDTO getSuggestions(String prefix, int maxNumSuggestions);
}
