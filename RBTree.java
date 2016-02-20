/**
* Left-leaning red-black tree. Stores key-value pairs. 
* @author Lukas Edman
**/
public class RBTree<K extends Comparable<K>,V> extends OrderedSymbolTable<K,V>{

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node root;

	private class Node{
		K key;
		V value;
		Node left;
		Node right;
		boolean color;
		int subtreeSize;

		public Node(K key, V value, boolean color, int subtreeSize){
			this.key = key;
			this.value = value;
			this.color = color;
			this.subtreeSize = subtreeSize;
		}
	}

	//returns if specified node is red
	private boolean isRed(Node node){
		if (node == null) return false;
		else return node.color;
	}

    /**
    * Puts the key value pair into the tree
    * @param 	key 	the given key
    * @param 	value 	the given value
    **/
    public void put(K key, V value){
    	root = put(root, key, value);
    	root.color = BLACK;
    }

	//recursive helper method for put
    private Node put(Node node, K key, V value){
    	if (node == null) return new Node(key, value, RED, 1);

		//finding node's place
    	int x = key.compareTo(node.key);
    	if (x < 0) node.left = put(node.left, key, value);
    	else if (x > 0) node.right = put(node.right, key, value);
    	else node.value = value;

		//fixing stuff
    	if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);	
    	if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
    	if (isRed(node.right) && isRed(node.left)) flipColors(node);

		//adjust size of node
    	node.subtreeSize = size(node.left) + size(node.right) + 1;
    	return node;
    }

    /**
    * Returns the corresponding value of the specified key
    * @param 	key 	the given key
    * @return 	the value of the given key
    **/
    public V get(K key){
    	Node node = root;
    	while(node!=null){
    		int x = key.compareTo(node.key);
    		if (x < 0) node = node.left;
    		else if (x > 0) node = node.right;
    		else return node.value;
    	}
    	return null;

    }

	//rotates node right, so the left child moves up and becomes node's parent
    private Node rotateRight(Node node){
    	Node x = node.left;
    	node.left = x.right;
    	x.right = node;
    	x.color = node.color;
    	node.color = RED;
    	x.subtreeSize = node.subtreeSize;
    	node.subtreeSize = size(node.left) + size(node.right) + 1;
    	return x;
    }

	//rotates node left, so the right child moves up and becomes node's parent
    private Node rotateLeft(Node node){
    	Node x = node.right;
    	node.right = x.left;
    	x.left = node;
    	x.color = node.color;
    	node.color = RED;
    	x.subtreeSize = node.subtreeSize;
    	node.subtreeSize = size(node.left) + size(node.right) + 1;
    	return x;
    }

	//flips the colors of the parent and children
    private void flipColors(Node node){
    	node.color = !node.color;
    	node.left.color = !node.left.color;
    	node.right.color = !node.right.color;
    }

    /**
    * Deletes the key value pair (specified by the key) from the tree
    * @param 	key 	the key to be deleted
    **/
    public void delete(K key){
    	root = delete(root, key);
    	root.color = BLACK;
    }

    //recursive helper method for deletion
    private Node delete(Node node, K key){
    	int x = key.compareTo(node.key);

    	if(x < 0){ //left side
    		if(node.left == null) return fix(node); //key is not in the tree
    		if(!isRed(node.left) && !isRed(node.left.left)) { //node.right and node.left.right have to be black, need to move stuff so that child is red
    			flipColors(node); //make children red (will fix right red child on the way back up)
    			if (isRed(node.right.left)) { //fixes red parent red child
    				node.right = rotateRight(node.right); 
    				node = rotateLeft(node); //parent node becomes child, should have red left child
    				flipColors(node); //makes left child red
    			}
    		}
    		node.left = delete(node.left, key); //go left
    	} else { 
    		if (isRed(node.left)) {
    			node = rotateRight(node); 
    			x = key.compareTo(node.key); //compare to new node
    		}
    		if (x == 0 && (node.right == null)) return null; //delete
    		else if (node.right == null) return fix(node); //key is not in the tree
    		if (!isRed(node.right) && !isRed(node.right.left)){ 
    			flipColors(node);
    			if (isRed(node.left.left)) { 
    				node = rotateRight(node);
    				x = key.compareTo(node.key); //compare to new node
    				flipColors(node);
    			}
    		}
    		if (x == 0) { //found the key, has a right child
    			Node succ = node.right;
    			while(succ.left!=null) succ = succ.left; //node's successor
    			node.key = succ.key; //swap with successor
    			node.value = succ.value; 
    			node.right = deleteSuccessor(node.right); 
    		}
    		else node.right = delete(node.right, key); //go right
    	}

    	return fix(node);
    }

    //helper method for delete, deletes the node after being swapped with the successor
    //mostly the same process as the beginning of delete method when going down the left side, without comparison
    private Node deleteSuccessor(Node node){
    	if(node.left == null) return null; //delete
    	if(!isRed(node.left) && !isRed(node.left.left)) { 
    		flipColors(node); 
    		if (isRed(node.right.left)) { 
    			node.right = rotateRight(node.right); 
    			node = rotateLeft(node); 
    			flipColors(node); 
    		}
    	}
    	node.left = deleteSuccessor(node.left);

    	return fix(node);
    }

    //fixes and resizes the nodes while going back up
    private Node fix(Node node){
   		//fixing stuff while recursing back up
    	if (isRed(node.right)) node = rotateLeft(node);
    	if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
    	if (isRed(node.left) && isRed(node.right)) flipColors(node);

    	//resize
    	node.subtreeSize = size(node.left) + size(node.right) + 1;
    	return node;
    }


    /**
    * Finds the number of key-value pairs in the tree.
    * @return the number of key-value pairs in the tree.
    **/
    public int size(){
    	return size(root);
    }	
    private int size(Node node){
    	if (node == null) return 0;
    	else return node.subtreeSize;
    }

    /**
    * Gets the minimum key
    * @return	the minimum key in the tree
    **/
    public K getMinKey(){
    	Node node = root;
    	while(node.left!=null) node = node.left;
    	return node.key;
    }

    /**
    * Gets the maximum key
    * @return	the maximum key in the tree
    **/
    public K getMaxKey(){
    	Node node = root;
    	while(node.right!=null) node = node.right;
    	return node.key;
    }

    /**
    * Finds the predecessor of a given key
    * @param	key 	the key in question
    * @return	the predecessor key
    **/
    public K findPredecessor(K key){
    	Node node = root;
    	K pred = null;
    	while (node!= null){
    		int x = key.compareTo(node.key);
    		if (x < 0) node = node.left; //key is smaller, go left
    		else if (x > 0) { //key is greater, go right and bring pred along
    			pred = node.key; 
    			node = node.right;
    		} else { //at the key's node
    			if (node.left!=null) node = node.left; //go left once more if possible
    			else break; //can't go further, done
    		}
    	}	
    	return pred;
    }

    /**
    * Finds the successor of a given key
    * @param	key 	the key in question
    * @return	the successor key
    **/
    public K findSuccessor(K key){
    	Node node = root;
    	K succ = null;
    	while (node!= null){
    		int x = key.compareTo(node.key);
    		if (x < 0){
    			succ = node.key;
    			node = node.left;
    		} else if (x > 0) node = node.right;
    		else {
    			if (node.right!=null) node = node.right;
    			else break;
    		}
    	}
    	return succ;
    }

    /**
    * Finds the rank of a given key, which is the number of keys before it. Min key should return 0.
    * @param	key 	the key in question
    * @return	the rank of the key
    **/
    public int findRank(K key){
    	Node node = root;
    	int rank = 0;
    	while(node!= null){
    		int x = key.compareTo(node.key);
    		if (x < 0) node = node.left;
    		else if (x > 0){
    			rank += 1;
    			if (node.left!=null) rank += node.left.subtreeSize;
    			node = node.right;
    		} else {
    			if (node.left!=null) rank+= node.left.subtreeSize;
    			break;
    		}
    	}
    	return rank;
    }

    /**
    * Finds the key with the given rank
    * @param	rank 	the specified rank
    * @return	the key with the given rank
    **/
    public K select(int rank){
    	Node node = root;
    	while(node!=null){
    		int x;
    		if (node.left == null) x = 0;
	    	else x = node.left.subtreeSize;
    		if (x > rank) node = node.left;
    		else if (x < rank) { 
    			rank -= x+1;
    			node = node.right;
    		}
    		else return node.key;
    	}
    	return null;
    }

    /**
    * Given a key, finds if the key is in the tree.
    * @param key    the key in question
    * @return whether or not the key is in the tree
    **/ 
    public boolean contains(K key) { 
        return (get(key) != null); 
    }

    /**
    * Finds if the tree is empty.
    * @return whether or not the tree is empty.
    **/
    public boolean isEmpty() { 
        return (size()==0); 
    }	

}