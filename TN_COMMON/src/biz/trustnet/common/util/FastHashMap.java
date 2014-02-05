/* 
 * Project Name	:   TN_COMMON
 * File Name		:	FastHashMap.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 2:13:57
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:    
 */
 
package biz.trustnet.common.util;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * This complete HashMap implementation is typicaly:
 *  60% faster for get operations.
 *  100% faster for add operations.
 *
 * This implementation only keeps the Key's hashcode.
 * Feel free to change the source code to handle different Types.
 *
 * @version January 2001
 * @author Olivier Refalo
 * @see www.crionics.com
 */
public final class FastHashMap extends AbstractMap
	implements Map, Cloneable, Serializable {
	/**
	 * Unique object ID. (Used by the Serialization mechanisum)
	 */
	private final static long serialVersionUID = -7495231766421401792L;
	/**
	 * Default fill fraction allowed before growing table.
	 * When number of element reaches DEFAULT_FILL*Table size -> rehash()
	 */
	private static final float DEFAULT_FILL = 0.70F;
	/**
	 *  Minimum size used for hash table
	 *  Must be a multiple of 2^N
	 */
	private static final int MINIMUM_SIZE = 16;
	/**
	 * Binary mask used to optimize modulo operations
	 */
	private int mask_;
	/**
	 * Number of entries present in table
	 */
	private int count_ = 0;
	/**
	 * Fill factor for the collection
	 */
	private float fillFactor_ = DEFAULT_FILL;
	/**
	 * Entries allowed before growing table
	 */
	private int limit_;
	/**
	 * incremented when the collection is modified: used to detect concurrent access
	 */
	transient private int modif_ = 0;
	/**
	 * Contains a reference to an empty iterator.
	 */
	private static final EmptyHashIterator EMPTY_ITERATOR = new EmptyHashIterator();
	/**
	 * contains the Set associated with the values when first created.
	 */
	private transient Set entrySet_ = null;
	/**
	 * contains the Set associated with the keys when first created.
	 */
	private transient Set keySet_ = null;
	/**
	 * Array of keys
	 */
	private Object[] keyTable_;
	/**
	 * Array of values
	 */
	private Object[] valueTable_;
	/**
	 *
	 */
	static final private Object REMOVED = "";

	/**
	 * Constructor
	 *
	 * @param _initialCapacity number of elements at build time
	 * @param _loadFactor pourcentage used before table is resized
	 */
	public FastHashMap (int _initialCapacity, float _loadFactor) {
		// validate input params
		if (_initialCapacity < 0)
			throw  new IllegalArgumentException("Illegal Initial Capacity: " + _initialCapacity);
		if (_loadFactor <= 0 || Float.isNaN(_loadFactor))
			throw  new IllegalArgumentException("Illegal Load factor: " + _loadFactor);
		// Compute initial table size (ensure odd)
		int tempsize = (int)((float)_initialCapacity/_loadFactor);
		int size = 16;
		while (size < tempsize) {
			size <<= 1;
		}
		int mask = size - 1;
		//	size = (tempsize > MINIMUM_SIZE) ? tempsize : MINIMUM_SIZE;
		//	System.out.println(size);
		mask_ = mask;
		limit_ = (int)(size*_loadFactor);
		fillFactor_ = _loadFactor;
		keyTable_ = new Object[size];
		valueTable_ = new Object[size];
	}

	/**
	 * Constructor
	 *
	 * @param initialCapacity number of elements at build time
	 */
	public FastHashMap (int initialCapacity) {
		this(initialCapacity, DEFAULT_FILL);
	}

	/**
	 * Default constructor
	 *
	 */
	public FastHashMap () {
		this(0, DEFAULT_FILL);
	}

	/**
	 * @return The collection size
	 */
	final public int size () {
		return  count_;
	}

	/**
	 * @return true is the collection is empty
	 */
	final public boolean isEmpty () {
		return  count_ == 0;
	}

	/**
	 * @param _value a not null object
	 * @return true if _value is in Map
	 */
	public boolean containsValue (Object _value) {
		Object vtab[] = valueTable_;
		for (int i = vtab.length; --i >= 0;) {
			Object o = vtab[i];
			if (o != null && _value.equals(o))
			return  true;
		}
		return  false;
	}

	/**
	 * @param _key the key to look for.
	 * @return true if collection contains the key
	 */
	final public boolean containsKey (Object _key) {
		int mask = mask_;
		int hash = _key.hashCode();
		int offset = hash & mask;
		Object ktab[] = keyTable_;
		Object vtab[] = valueTable_;
		Object o;
		int idx = vtab.length;
		while (--idx>=0 && vtab[offset] == REMOVED || ((o = ktab[offset]) != null && !o.equals(_key))) {
			offset = (offset - 1) & mask;
		}
		if (idx < 0)
			return  false;
		else
			return  vtab[offset] != null;
	}

	/**
	 * @param _key the entry id
	 * @return the object associated with the key
	 */
	final public Object get (Object _key) {
		int mask = mask_;
		int hash = _key.hashCode();
		int offset = hash & mask;
		Object ktab[] = keyTable_;
		int idx = ktab.length;
		Object vtab[] = valueTable_;
		Object o;
		while (--idx>=0 && vtab[offset] == REMOVED || ((o = ktab[offset]) != null && !(o.hashCode() == hash && o.equals(_key)))) {
			offset = (offset - 1) & mask;
		}
		if (idx < 0)
			return  null;
		else
			return  vtab[offset];
	}
	
	final public String getString(Object _key) {
		try{
			if(get(_key) != null)
				return (String)get(_key);
			else 
				return "";
		}catch(Exception e){
			return "";
		}
	}

	final public String getString(Object _key,String replaceStr) {
		try{
			if(get(_key) != null)
				return (String)get(_key);
			else 
				return replaceStr;
		}catch(Exception e){
			return replaceStr;
		}
	}
	/**
	 * Stores a (key,value) in the Map.
	 *
	 * @param _key the entry id
	 * @param _value the entry value
	 * @return the old object or null.
	 */
	final public Object put (Object _key, Object _value) {
		modif_++;
		if (count_++ > limit_) {
			rehash();
		}
		int mask = mask_;
		int hash = _key.hashCode();
		int offset = hash & mask;
		Object ktab[] = keyTable_;
		Object o = null;
		while ((o = ktab[offset]) != null && !(o.equals(_key))) {
			offset = (offset - 1) & mask;
		}
		o = valueTable_[offset];
		if (o != null && o != REMOVED) {
			// it's an overlap
			count_--;
		}
		else {
			ktab[offset] = _key;
		}
		valueTable_[offset] = _value;
		if (o == REMOVED)
			return  null;
		else
			return  o;
	}

	/**
	 * Called when the unlying table needs to be expanded.
	 *
	 */
	private void rehash () {
		modif_++;
		Object[] oldkeys = keyTable_;
		int len = oldkeys.length << 1;
		limit_ = (int)(len*fillFactor_);
		Object[] ktab = keyTable_ = new Object[len];
		Object[] oldvalues = valueTable_;
		Object[] vtab = valueTable_ = new Object[len];
		int mask = (mask_ << 1) | 1;
		for (int i = oldvalues.length; --i >= 0;) {
			Object ov = oldvalues[i];
			if (ov != null && ov != REMOVED) {
			// Compute the new location
			Object k = oldkeys[i];
			int hash = k.hashCode();
			int offset = hash & mask;
			Object o;
			while ((o = ktab[offset]) != null && !(o.hashCode() == hash && o.equals(k))) {
				offset = (offset - 1) & mask;
			}
			vtab[offset] = ov;
			ktab[offset] = k;
			}
		}
		mask_ = mask;
	}

	/**
	 * Removes the (key,value) pair from the Map
	 *
	 * @param _key entry id
	 * @return the object just removed
	 */
	final public Object remove (Object _key) {
		modif_++;
		int hash = _key.hashCode();
		int mask = mask_;
		int offset = hash & mask;
		Object ktab[] = keyTable_;
		int idx = ktab.length;
		Object vtab[] = valueTable_;
		Object o = null;
		while (--idx >=0 && vtab[offset] == REMOVED || ((o = ktab[offset]) != null && !(o.hashCode() == hash && o.equals(_key)))) {
			offset = (offset - 1) & mask;
		}
		if (idx < 0)
			return  null;
		o = vtab[offset];
		if (o != null) {
			vtab[offset] = REMOVED;
			ktab[offset] = null;
			count_--;
			return  o;
		}
		return  null;
	}

	/**
	 * Clear the collection.
	 * Warning, this method doesn't deallocate the buffers
	 *
	 */
	public void clear () {
	modif_++;
	Object vtab[] = valueTable_;
	Object ktab[] = keyTable_;
	for (int i = vtab.length; --i >= 0;) {
		vtab[i] = null;
		ktab[i] = null;
	}
	count_ = 0;
	}

	/**
	 * Clones the Map instance
	 *
	 * @return a cloned copy
	 */
	public Object clone () {
		try {
			FastHashMap t = (FastHashMap)super.clone();
			// copy keys: Keys are supposed to be immutable
			int len = keyTable_.length;
			t.keyTable_ = new Object[len];
			t.valueTable_ = new Object[len];
			System.arraycopy(keyTable_, 0, t.keyTable_, 0, len);
			// copy values, Clone objects if marked as Clonable
			Object vtab[] = valueTable_;
			Object dvtab[] = t.valueTable_;
			for (; --len > 0;) {
			Object o = vtab[len];
			if (o != null && o instanceof Cloneable) {
				// Object.clone() is protected -> reflection mandatory
				Class c = o.getClass();
				Method method = c.getMethod("clone", null);
				o = method.invoke(o, null);
			}
			dvtab[len] = o;
			}
			t.count_ = count_;
			t.limit_ = limit_;
			t.mask_ = mask_;
			t.fillFactor_ = fillFactor_;
			t.keySet_ = null;
			t.entrySet_ = null;
			t.values = null;
			t.modif_ = 0;
			return  t;
		} catch (CloneNotSupportedException e) {
			throw  new InternalError("One of the value object is not clonable");
		} catch (NoSuchMethodException e1) {
			throw  new InternalError("Weird! clone() method not found!:" + e1.toString());
		} catch (InvocationTargetException e2) {
			throw  new InternalError("Weird! problem during clone() invocation");
		} catch (IllegalAccessException e3) {
			throw  new InternalError("Weird! illegal access on clone() invocation");
		}
	}
	private transient Collection values = null;

	/**
	 * @return A browsable Set on the keys in this Map
	 */
	public Set keySet () {
	if (keySet_ == null) {
		keySet_ = new AbstractSet() {

		/**
		 *
		 * @return an Iterator
		 */
		public Iterator iterator () {
			return  getHashIterator(KEYS);
		}

		/**
		 *
		 * @return the set size
		 */
		public int size () {
			return  count_;
		}

		/**
		 *
		 * @param o
		 * @return true if set contains parameter
		 */
		public boolean contains (Object _o) {
			return  containsKey(_o);
		}

		/**
		 * put your documentation comment here
		 * @param o
		 * @return
		 */
		public boolean remove (Object _o) {
			int oldSize = count_;
			FastHashMap.this.remove(_o);
			return  count_ != oldSize;
		}

		/**
		 * put your documentation comment here
		 */
		public void clear () {
			FastHashMap.this.clear();
		}
		};
	}
	return  keySet_;
	}

	/**
	 * @return A browsable Set on the values\s in this Map
	 */
	public Collection values () {
	if (values == null) {
		values = new AbstractCollection() {

		/**
		 * put your documentation comment here
		 * @return
		 */
		public Iterator iterator () {
			return  getHashIterator(VALUES);
		}

		/**
		 * put your documentation comment here
		 * @return
		 */
		public int size () {
			return  count_;
		}

		/**
		 * put your documentation comment here
		 * @param _o
		 * @return
		 */
		public boolean contains (Object _o) {
			return  containsValue(_o);
		}

		/**
		 * put your documentation comment here
		 */
		public void clear () {
			FastHashMap.this.clear();
		}
		};
	}
	return  values;
	}

	/**
	 * @return A browsable Set on the entries in this Map
	 */
	public Set entrySet () {
	if (entrySet_ == null) {
		entrySet_ = new AbstractSet() {

		/**
		 * put your documentation comment here
		 * @return
		 */
		public Iterator iterator () {
			return  getHashIterator(ENTRIES);
		}

		/**
		 * put your documentation comment here
		 * @param o
		 * @return
		 */
		public boolean contains (Object o) {
			if (!(o instanceof Map.Entry))
			return  false;
			Map.Entry entry = (Map.Entry)o;
			Object key = entry.getKey();
			Object obj = FastHashMap.this.get(key);
			return  (obj != null && obj.equals(entry.getValue()));
		}

		/**
		 * put your documentation comment here
		 * @param o
		 * @return
		 */
		public boolean remove (Object o) {
			if (this.contains(o)) {
			Map.Entry entry = (Map.Entry)o;
			FastHashMap.this.remove(entry.getKey());
			return  true;
			}
			else
			return  false;
		}

		/**
		 *
		 * @return the size of teh collection
		 */
		public int size () {
			return  count_;
		}

		/**
		 * Clear the collection entries
		 */
		public void clear () {
			FastHashMap.this.clear();
		}
		};
	}
	return  entrySet_;
	}

	/**
	 * @param _type Type of Iterator: KEYS, VALUES, ENTRIES
	 * @return an Iterator on the Map
	 */
	private Iterator getHashIterator (int _type) {
	if (count_ == 0) {
		return  EMPTY_ITERATOR;
	}
	else {
		return  new HashIterator(_type);
	}
	}

	/**
	 * HashMap collision list entry.
	 */
	private final static class Entry
		implements Map.Entry {
	Object key_;
	Object value_[];
		int idx_;

	/**
	 *
	 * @param         int _key
	 * @param         Object _value
	 */
	Entry (Object _key, Object _value[], int _idx) {
		key_ = _key;
		value_ = _value;
			idx_=_idx;
	}

	/**
	 * Clones the current entry
	 * @return
	 */
	protected Object clone () {
		return  new Entry(key_, value_, idx_);
	}

	/**
	 * Gets the entry's key
	 *
	 * @return an Integer
	 */
	public Object getKey () {
		return  key_;
	}

	/**
	 * Gets the entry's value
	 *
	 * @return an Object
	 */
	public Object getValue () {
		return  value_[idx_];
	}

	/**
	 * Sets the entry's value
	 * @param _value the new value
	 * @return the old value
	 */
	public Object setValue (Object _value) {
		Object oldValue = value_[idx_];
		value_[idx_] = _value;
		return  oldValue;
	}

	/**
	 * Compare two entry objects
	 *
	 * @param _o an Entry object
	 * @return true- if objects are equal
	 */
	public boolean equals (Object _o) {
		if (!(_o instanceof Entry))
		return  false;
		Entry e = (Entry)_o;
		return  (key_ == e.key_ && value_[idx_].equals(e.getValue()));
	}

	/**
	 * get's the object hashcode
	 *
	 * @return the hashcode as a int value
	 */
	public int hashCode () {
		return  key_.hashCode();
	}

	/**
	 * @return a String representing the entry's value
	 */
	public String toString () {
		return  key_ + "=" + value_[idx_];
	}
	}
	/**
	 * // Types of Iterators
	 */
	private static final int KEYS = 0;
	private static final int VALUES = 1;
	private static final int ENTRIES = 2;

	/**
	 * Empty iterator
	 */
	private final static class EmptyHashIterator
		implements Iterator {

	/**
	 * put your documentation comment here
	 */
	EmptyHashIterator () {
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public boolean hasNext () {
		return  false;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Object next () {
		throw  new NoSuchElementException();
	}

	/**
	 * put your documentation comment here
	 */
	public void remove () {
		throw  new IllegalStateException();
	}
	}

	private final class HashIterator
		implements Iterator {
	// Index of last value found
	private int index_ = keyTable_.length;
	// Last value found
	private Object entry_ = null;
	private int lastReturned_ = -1;
	// KEYS, VALUES or ENTRIES
	private int type_;

	/** Used to detect multithread modifications */
	private int expectedModif_ = modif_;

	/**
	 * Contrustor for a given type
	 *
	 */
	HashIterator (int _type) {
		type_ = _type;
	}

	/**
	 * @return true if entries are pending
	 */
	public boolean hasNext () {
		int i = index_;
		Object[] ktab = keyTable_;
		Object e = entry_;
		// Look for next value !=null
		while (e == null && i > 0)
		e = ktab[--i];
		entry_ = e;
		index_ = i;
		return  e != null;
	}

	/**
	 * Jumps to the next element.
	 *
	 * @return the next element (if any)
	 */
	public Object next () {
		// Check multi concurrency
		if (modif_ != expectedModif_)
		throw  new ConcurrentModificationException();
		Object e = entry_;
		int i = index_;
		Object[] ktab = keyTable_;
		// Look for value!=null
		while (e == null && i > 0)
		e = ktab[--i];
		index_ = i;
		// If value found, return the appropriate value
		if (e != null) {
		lastReturned_ = i;
		entry_ = null;
		if (type_ == KEYS)
			return  e;
		if (type_ == VALUES)
			return  valueTable_[i];
		else
			return  new Entry(e, valueTable_, i);
		}
		throw  new NoSuchElementException();
	}

	/**
	 * Removes the currently pointed element.
	 *
	 */
	public void remove () {
		// Check if element already removed
		if (lastReturned_ == -1)
		throw  new IllegalStateException();
		// Check for concurrency pb
		if (modif_ != expectedModif_)
		throw  new ConcurrentModificationException();
		Object obj = FastHashMap.this.remove(keyTable_[lastReturned_]);
		expectedModif_++;
		lastReturned_ = -1;
		if (obj != null)
		return;
		else
		throw  new ConcurrentModificationException();
	}
	}
}




