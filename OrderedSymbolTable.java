// This abstract class defines an ordered symbol table, such as a
// binary search tree. It is pretty close to being an interface, save
// for a couple of simple methods near the end.

abstract class OrderedSymbolTable<K extends Comparable<K>,V> {
    // map key to value
    abstract public void put(K key, V value);

    // return key's value
    abstract public V get(K key);

    // remove key-value pair
    abstract public void delete(K key);	

    // how many pairs?
    abstract public int size();	

    // what's the first key?
    abstract public K getMinKey();

    // what's the last key?
    abstract public K getMaxKey();

    // what key comes before this one? (return null if it's the first)
    abstract public K findPredecessor(K key);

    // what key comes after this one? (return null if it's the last)
    abstract public K findSuccessor(K key);

    // how many keys come before this one?
    abstract public int findRank(K key);

    // return the key with rank keys before it.
    abstract public K select(int rank);

    // is key present?
    public boolean contains(K key) { return (get(key) != null); }

    // is table empty?
    public boolean isEmpty() { return (size()==0); }	
}