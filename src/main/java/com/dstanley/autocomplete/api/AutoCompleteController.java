package com.dstanley.autocomplete.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dstanley.autocomplete.service.AutoCompleteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/suggestion"})
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AutoCompleteController {

	private final AutoCompleteService acs;

	@GetMapping(value = "/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AutoCompleteResponseDTO> getAllSuggestions(
			@PathVariable("prefix") String prefix) {
		return new ResponseEntity<>(acs.getSuggestions(prefix), HttpStatus.OK);
	}

	@GetMapping(value = "/{prefix}/{maxNumSuggestions}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AutoCompleteResponseDTO> getLimitedSuggestions(
			@PathVariable("prefix") String prefix,
			@PathVariable("maxNumSuggestions") int maxNumSuggestions) {
		return new ResponseEntity<>(acs.getSuggestions(prefix, maxNumSuggestions), HttpStatus.OK);
	}
}
