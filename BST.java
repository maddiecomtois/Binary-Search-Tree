
/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 15/11/18 13:37
 *
 *  @author Maddie Comtois
 *
 *************************************************************************/

public class BST<Key extends Comparable<Key>, Value> {
	private Node root; // root of BST 

	/**
	 * Private node class.
	 */
	private class Node {
		private Key key; // tree sorts by key value
		private Value val; // data associated with the key
		private Node left, right; // left and right subtrees
		private int N; // number of nodes in subtree

		public Node(Key key, Value val, int N) {
			this.key = key;
			this.val = val;
			this.N = N;
		}
	}

	// return boolean value of an empty tree
	public boolean isEmpty() {
		return size() == 0;
	}

	// return number of key-value pairs in tree
	public int size() {
		return size(root);
	}

	// return number of key-value pairs in BST rooted at x
	private int size(Node x) {
		if (x == null)
			return 0;
		else
			return x.N;
	}

	/**
	 * Search BST for given key. Does there exist a key-value pair with given key?
	 *
	 * @param key the search key
	 * @return true if key is found and false otherwise
	 */
	public boolean contains(Key key) {
		return get(key) != null;
	}

	/**
	 * Search BST for given key. What is the value associated with given key?
	 *
	 * @param key the search key
	 * @return value associated with the given key if found, or null if no such key
	 *         exists.
	 */
	public Value get(Key key) {
		return get(root, key);
	}

	private Value get(Node x, Key key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp < 0)
			return get(x.left, key);
		else if (cmp > 0)
			return get(x.right, key);
		else
			return x.val;
	}

	/**
	 * Insert key-value pair into BST. If key already exists, update with new value.
	 *
	 * @param key the key to insert
	 * @param val the value associated with key
	 */
	public void put(Key key, Value val) {
		if (val == null) {
			delete(key);
			return;
		}
		root = put(root, key, val);
	}

	private Node put(Node x, Key key, Value val) {
		if (x == null)
			return new Node(key, val, 1);
		int cmp = key.compareTo(x.key);
		if (cmp < 0)
			x.left = put(x.left, key, val);
		else if (cmp > 0)
			x.right = put(x.right, key, val);
		else
			x.val = val;
		x.N = 1 + size(x.left) + size(x.right);
		return x;
	}

	/**
	 * Tree height.
	 *
	 * Asymptotic worst-case running time using Theta notation: O(N)
	 * 
	 * Justification: N is the number of nodes in the tree, and this function makes
	 * a recursive function call to every node in the tree. Because the method ends
	 * once every node has been seen, the run time will be the number of nodes there
	 * are in the tree.
	 *
	 * @return the number of links from the root to the deepest leaf.      
	 */
	public int height() {
		if (isEmpty())
			return -1;
		return height(root);
	}

	private int height(Node x) {
		if (x == null)
			return -1;
		else {
			int leftMax = height(x.left);
			int rightMax = height(x.right);

			if (leftMax > rightMax)
				return leftMax + 1;
			else
				return rightMax + 1;
		}
	}

	/**
	 * Median key. If the tree has N keys k1 < k2 < k3 < ... < kN, then their median
	 * key is the element at position (N+1)/2
	 * 
	 * Asymptotic runtime: Theta(h), where h is the height of the tree
	 * 
	 * Justification: When the median function is called, it makes a call to the
	 * method rank, which gets the rank of the highest value node in the tree of
	 * a given root. This method calls the size function, which gets the total 
	 * number of nodes in the subtree. Because this function searches through
	 * every node, it ultimately reaches the height of the tree, which is the
	 * deepest chain of nodes. In a balanced tree, a runtime of Theta(h) is 
	 * reached in the while loop for finding the node of the highest value, as 
	 * all the bottom nodes in the tree share the same height. 
	 *
	 * @return the median key, or null if the tree is empty.
	 */
	public Key median() {
		if (isEmpty())
			return null;
		Node currentNode = root;
		while (currentNode.right != null)
			currentNode = currentNode.right;

		int rankOfLargestKey = rank(currentNode.key);
		int medianRank = (rankOfLargestKey + 1) / 2;
		return select(medianRank);
	}
	
	
	/**
	 * Search for the rank in BST of node with given key
	 *
	 * @param key of the node
	 * @return the rank of the node
	 */
	public int rank(Key key) {
		return rank(key, root);
	}

	private int rank(Key key, Node x) {
		if (x == null)
			return -1;
		int cmp = key.compareTo(x.key);

		if (cmp < 0)
			return rank(key, x.left);
		else if (cmp > 0)
			return (1 + size(x.left) + rank(key, x.right));
		else
			return size(x.left);
	}
	
	
	/**
	 * Search for the key of a node given its rank in the BST
	 *
	 * @param rank of the node
	 * @return key of the node
	 */
	public Key select(int rank) {
		if (rank < 0 || rank >= size())
			return null;
		Node x = select(root, rank);
		return x.key;
	}

	private Node select(Node x, int rank) {
		int leftSubtreeKeys = size(x.left);
		if (leftSubtreeKeys > rank)
			return select(x.left, rank);
		else if (leftSubtreeKeys < rank)
			return select(x.right, (rank - leftSubtreeKeys - 1));
		else
			return x;
	}

	/**
	 * Print all keys of the tree in a sequence, in-order. For each node,
	 * the keys in the left and right subtrees appear before the key in the node, 
	 * For each subtree, its keys appear within a parenthesis.
	 *
	 * @return a String with all keys in the tree, in order, parenthesized.
	 */
	public String printKeysInOrder() {
		if (isEmpty())
			return "()";
		return printKeysInOrder(root, "");
	}

	private String printKeysInOrder(Node x, String string) {
		string += "(";
		if (x != null) {
			string = printKeysInOrder(x.left, string);
			string += x.key;
			string = printKeysInOrder(x.right, string);
		}
		string += ")";
		return string;
	}

	/**
	 * Pretty Printing the tree. Print the tree in a vertical format, with the
	 * right subtrees resting above the left subtrees
	 *
	 * @return a multi-line string with the pretty ascii picture of the tree.
	 */
	public String prettyPrintKeys() {
		return prettyPrintKeys(root, "");
	}

	private String prettyPrintKeys(Node x, String prefix) {
		if (x == null)
			return prefix + "-null\n";
		String toPrint = prefix;
		prefix += " ";

		toPrint += "-" + x.key + "\n";
		toPrint += prettyPrintKeys(x.left, prefix + "|");
		toPrint += prettyPrintKeys(x.right, prefix + " ");

		return toPrint;
	}

	/**
	 * Deletes a key from a tree (if the key is in the tree). Works symmetrically
	 * from the Hibbard deletion; if the node has two children, it is replaced by its
	 * predecessor node.
	 *
	 * @param key the key to delete
	 */
	public void delete(Key key) {
		if (get(key) != null)
			delete(root, key);
	}

	private Node delete(Node x, Key key) {
		int cmp = key.compareTo(x.key);
		if (cmp < 0)
			x.left = delete(x.left, key);
		else if (cmp > 0)
			x.right = delete(x.right, key);
		else {
			// no child or one child
			if (x.left == null)
				return x.right;
			else if (x.right == null)
				return x.left;

			// two children
			x.key = findMax(x.left); 
			x.left = delete(x.left, x.key); 

		}
		return x;
	}

	/**
	 * Find the maximum key in a subtree of a given root
	 *
	 * @param node to search
	 * @return the max key value
	 */
	private Key findMax(Node x) {
		if (x.right == null)
			return x.key;
		else
			return findMax(x.right);
	}
}