package com.dstanley.autocomplete.dictionary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trie {

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	public void insert(String word) {
		TrieNode node = root;
		StringBuilder prefix = new StringBuilder();
		for (Character letter : word.toCharArray()) {
			prefix.append(letter);
			node = node.getChildren().computeIfAbsent(
					letter,
					c -> new TrieNode(letter, prefix.toString(), prefix.toString().equals(word)));
		}
		node.setEndOfWord(true);
	}
}
