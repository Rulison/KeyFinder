/* HashTableChained.java */
package dict;
import list.*;


/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

	/**
	 *  Place any data fields here.
	 **/

	SList[] table;
	final static int DEFAULT_BUCKETS = 101;
	int size;
	public static void main(String[] args) {

	}
	/** 
	 *  Construct a new empty hash table intended to hold roughly sizeEstimate
	 *  entries.  (The precise number of buckets is up to you, but we recommend
	 *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
	 **/

	public HashTableChained(int sizeEstimate) {
		// Your solution here.
		table = new SList[findClosePrime(sizeEstimate)];
		for(int i=0;i<table.length;i++)
			table[i] = new SList();
	}
	private static int findClosePrime(int n) {
		int i=0;
		while(true) {
			if(isPrime(n+i))
				return n+i;
			i++;
		}
	}
	private static boolean isPrime(int n) {
		if(n==0 || n==1)
			return true;
		for(int i=2;i<=Math.sqrt(n);i++) {
			if(n%i == 0)
				return false;
		}
		return true;
	}

	/** 
	 *  Construct a new empty hash table with a default size.  Say, a prime in
	 *  the neighborhood of 100.
	 **/

	public HashTableChained() {
		// Your solution here.
		this(DEFAULT_BUCKETS);
	}

	/**
	 *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
	 *  to a value in the range 0...(size of hash table) - 1.
	 *
	 *  This function should have package protection (so we can test it), and
	 *  should be used by insert, find, and remove.
	 **/

	int compFunction(int code) {
		// Replace the following line with your solution.
		return Math.round(Math.abs((127*code + 4)%table.length));
	}

	/** 
	 *  Returns the number of entries stored in the dictionary.  Entries with
	 *  the same key (or even the same key and value) each still count as
	 *  a separate entry.
	 *  @return number of entries in the dictionary.
	 **/

	public int size() {
		// Replace the following line with your solution.
		return size;
	}

	/** 
	 *  Tests if the dictionary is empty.
	 *
	 *  @return true if the dictionary has no entries; false otherwise.
	 **/

	public boolean isEmpty() {
		// Replace the following line with your solution.
		return size==0;
	}

	/**
	 *  Create a new Entry object referencing the input key and associated value,
	 *  and insert the entry into the dictionary.  Return a reference to the new
	 *  entry.  Multiple entries with the same key (or even the same key and
	 *  value) can coexist in the dictionary.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the key by which the entry can be retrieved.
	 *  @param value an arbitrary object.
	 *  @return an entry containing the key and value.
	 **/

	public Entry insert(Object key, Object value) {
		// Replace the following line with your solution.
		Entry e = new Entry();
		e.key = key;
		e.value = value;
		int hash = key.hashCode();
		table[compFunction(hash)].insertBack(e);
		size++;
		return e;
	}

	/** 
	 *  Search for an entry with the specified key.  If such an entry is found,
	 *  return it; otherwise return null.  If several entries have the specified
	 *  key, choose one arbitrarily and return it.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the search key.
	 *  @return an entry containing the key and an associated value, or null if
	 *          no entry contains the specified key.
	 **/

	public Entry find(Object key) {
		// Replace the following line with your solution.
		SList chain = table[compFunction(key.hashCode())];
		SListNode runner = (SListNode) chain.front();
		while(true) {
			try {
				if(((Entry)runner.item()).key().equals(key))
					return (Entry) runner.item();
				runner = (SListNode) runner.next();
			} catch (InvalidNodeException e) {
				break;
			}

		}
		return null;
	}

	/** 
	 *  Remove an entry with the specified key.  If such an entry is found,
	 *  remove it from the table and return it; otherwise return null.
	 *  If several entries have the specified key, choose one arbitrarily, then
	 *  remove and return it.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the search key.
	 *  @return an entry containing the key and an associated value, or null if
	 *          no entry contains the specified key.
	 */

	public Entry remove(Object key) {
		// Replace the following line with your solution.
		SList chain = table[compFunction(key.hashCode())];
		SListNode runner = (SListNode) chain.front();
		while(true) {
			try {
				if(((Entry)runner.item()).key().equals(key)) {
					Entry toReturn = (Entry) runner.item();
					runner.remove();
					size--;
					return toReturn;
				}
				runner = (SListNode) runner.next();
			} catch (InvalidNodeException e) {
				break;
			}

		}
		return null;

	}

	/**
	 *  Remove all entries from the dictionary.
	 */
	public void makeEmpty() {
		// Your solution here.
		table = new SList[table.length];
		for(int i=0;i<table.length;i++)
			table[i] = new SList();
		size=0;
	}
	public int numCollisions() {
		int num = 0;
		for(int i=0;i<table.length;i++) {
			if(table[i].size() > 1)
				num+=table[i].size()-1;
		}
		return num;
			
	}
	public int[] histograph() {
		int[] sizes = new int[table.length];
		for(int i=0;i<table.length;i++) {
			sizes[i] = table[i].size();
		}
		return sizes;
	}

}