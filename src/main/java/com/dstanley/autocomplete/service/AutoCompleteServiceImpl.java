package com.dstanley.autocomplete.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.dstanley.autocomplete.api.AutoCompleteResponseDTO;
import com.dstanley.autocomplete.dictionary.Trie;
import com.dstanley.autocomplete.dictionary.TrieNode;
import com.fasterxml.jackson.databind.util.LRUMap;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
@Service
public class AutoCompleteServiceImpl implements AutoCompleteService {

	private Trie autoCompleteTrie;
	private LRUMap<String, List<String>> autoCompleteCache;

	@PostConstruct
	public void init() {

		autoCompleteTrie = new Trie();
		autoCompleteCache = new LRUMap<>(0, 5);

		try (Stream<String> dictionaryStream =
				Files.lines(Path.of(ResourceUtils.getFile("classpath:static/wordlist.txt").getPath()))) {

			dictionaryStream.forEach(w -> autoCompleteTrie.insert(w));

		} catch (IOException e) {
			log.error("Error processing dictionary file", e);
		}
	}

	public AutoCompleteResponseDTO getSuggestions(String prefix) {
		List<String> suggestions = getSuggestionsList(prefix);
		return new AutoCompleteResponseDTO(suggestions.size(), suggestions);
	}

	public AutoCompleteResponseDTO getSuggestions(String prefix, int maxNumSuggestions) {
		List<String> suggestions = getSuggestionsList(prefix);
		return new AutoCompleteResponseDTO(
				suggestions.size(),
				suggestions.stream().limit(maxNumSuggestions).collect(Collectors.toList()));
	}

	private List<String> getSuggestionsList(String prefix) {

		List<String> cachedSuggestions = autoCompleteCache.get(prefix);
		if (cachedSuggestions != null && !cachedSuggestions.isEmpty()) {
			return cachedSuggestions;
		}

		List<String> suggestions = new ArrayList<>();
		TrieNode node = autoCompleteTrie.getRoot();

		for (Character c : prefix.toCharArray()) {
			node = node.getChildren().get(c);
		}

		buildSuggestionListDFS(node, suggestions);
		autoCompleteCache.put(prefix, suggestions);
		suggestions.sort(Comparator.comparing(String::length).thenComparing(String::compareToIgnoreCase));

		return suggestions;
	}

	/*
	 * There are 2 algorithms here DFS seems to be more performant on the ~69k
	 * sized dictionary that is included as part of the project but the BFS
	 * is here just to demonstrate that on a large enough sample set we would probably
	 * end up choosing a BFS or directed graph implementation over a recursive solution
	 */
	private void buildSuggestionListDFS(TrieNode node, List<String> suggestions) {

		if (node.isEndOfWord()) {
			suggestions.add(node.getWord().toString());
		}

		for (TrieNode child : node.getChildren().values()) {
			buildSuggestionListDFS(child, suggestions);
		}
	}

	/*
	 * Code for BFS is included as well just as an example of an alternative
	 * implementation that was utilized for testing
	 *
	private void buildSuggestionListBFS(TrieNode node, List<String> suggestions) {
		Queue<TrieNode> queue = new LinkedList<>();
		queue.add(node);
		while (!queue.isEmpty()) {
			TrieNode current = queue.poll();
			if (current.isEndOfWord()) {
				suggestions.add(current.getWord());
			}
			for (TrieNode tn : current.getChildren().values()) {
				queue.add(tn);
			}
		}
	}*/
}
