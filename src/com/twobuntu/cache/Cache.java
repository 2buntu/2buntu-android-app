package com.twobuntu.cache;

import java.util.HashMap;
import java.util.Map;

// Represents a cache for temporarily storing data retrieved from a remote source.
public class Cache<T> {
	
	// TODO: it would be nice if the data stored here persisted longer than
	// the current activity, but this will work for now.
	
	// Represents an entry in the cache.
	private class CacheEntry {
		public T mValue;
		public long mExpires;
	}
	
	// Amount of time (in milliseconds) that data is stored in the cache.
	private static final long DURATION = 600000;
	
	// Stores the data by the unique key.
	private Map<String, CacheEntry> mCache = new HashMap<String, CacheEntry>();
	
	// Returns the current Unix timestamp.
	private long timestamp() {
		return System.currentTimeMillis() / 1000l;
	}
	
	// Attempts to retrieve the specified data from the cache.
	public T get(String key) {
		CacheEntry entry = mCache.get(key);
		if(entry == null || timestamp() > entry.mExpires)
			return null;
		return entry.mValue;
	}
	
	public void set(String key, T value) {
		CacheEntry entry = new CacheEntry();
		entry.mValue = value;
		entry.mExpires = timestamp() + DURATION;
		mCache.put(key, entry);
	}
}
