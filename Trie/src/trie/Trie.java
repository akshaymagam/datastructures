package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode root = new TrieNode(null,null,null);
		int index = 0;
		for(String word : allWords) {
			addWord(root, index++, allWords, 0, word);
		}
		return root;
	}
	
	private static void addWord(TrieNode input, int index, String [] words, int first, String word) {
		//need a base case
		if(input.firstChild == null) {
			Indexes indexes = new Indexes(index, (short)first, (short)(word.length()- 1 + first));
			input.firstChild = new TrieNode(indexes, null, null);
			return;
		}
		
		//building general case
		TrieNode child = input.firstChild, lastChild = child;
		//need to compare the prefixes
		int prefix = 0;
		while(child != null) {
			if(words[child.substr.wordIndex].charAt(first) == word.charAt(first)) {
				while(words[child.substr.wordIndex].charAt(first+prefix) == word.charAt(first+prefix)) {
					if(child.substr.endIndex == first + prefix) {
						addWord(child, index, words, first + prefix + 1, word);
						return;
					}
					prefix++;
				}
				//creating new Nodes indices to build children/siblings once loop breaks
				int endIndex = child.substr.endIndex;
				Indexes indexes = new Indexes(child.substr.wordIndex, (short)first, (short)(prefix - 1 + first));
				child.substr = indexes;
				Indexes newIndexes = new Indexes(child.substr.wordIndex, (short) (prefix + first), (short) endIndex);
				TrieNode newNode = new TrieNode(newIndexes, child.firstChild, null);
				child.firstChild = newNode;
				Indexes siblingIdx = new Indexes(index, (short)(prefix + first), (short) (word.length() - 1) );
				TrieNode siblingNode = new TrieNode(siblingIdx, null, null);
				newNode.sibling = siblingNode;
				return;
			}
			if(child.sibling == null) {
				lastChild = child;
			}
			child = child.sibling;
		}
		 Indexes indexes = new Indexes(index, (short)first, (short)(word.length()-1));
	        lastChild.sibling = new TrieNode(indexes, null, null);
	}
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		ArrayList<TrieNode> end = new ArrayList<TrieNode>();
		return comparePre(root, allWords, prefix, end);
	}
	
	private static ArrayList<TrieNode> comparePre(TrieNode root, String[] allWords, String prefix, ArrayList<TrieNode> end){
		TrieNode child = root.firstChild;
		while(child != null) {
			Indexes index = child.substr;
			int startIndex = index.startIndex, endIndex = index.endIndex + 1;
			String curr = allWords[index.wordIndex];
			if(prefix.charAt(startIndex) == curr.charAt(startIndex)) {
				if(prefix.length() < endIndex + 1) {
					if(prefix.substring(startIndex).equals(curr.substring(startIndex, prefix.length()))) {
						if(child.firstChild == null) {
							end.add(child);
							return end;
						}
						addChildorSib(child.firstChild, end);
						return end;
					}
					else return null;
				}
				else {
					if(curr.substring(startIndex,endIndex).equals(prefix.substring(startIndex, endIndex))) return comparePre(child, allWords, prefix, end);
					else return null;
				}
			}
			child = child.sibling;
			
		}
		return null;
	}
	
	private static void addChildorSib(TrieNode root, ArrayList<TrieNode> list) {
		while(root != null) {
			if(root.firstChild == null) {
				list.add(root);
			}
			if(root.firstChild != null ) {
				addChildorSib(root.firstChild, list);
			} if(root.sibling != null){
				addChildorSib(root.sibling, list);
			}
			break;
		}
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
