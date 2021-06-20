package com.dstanley.autocomplete.dictionary;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrieNode {

	private Map<Character, TrieNode> children;
	private Character letter;
	private String word;
	private boolean endOfWord;

	public TrieNode() {
		children = new HashMap<>();
	}

	public TrieNode(Character letter, String word, boolean endOfWord) {
		this.letter = letter;
		this.word = word;
		this.children = new HashMap<>();
		this.endOfWord = endOfWord;
	}
}
