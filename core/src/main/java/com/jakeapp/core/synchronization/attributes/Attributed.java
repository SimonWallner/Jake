package com.jakeapp.core.synchronization.attributes;

import java.util.ArrayList;
import java.util.Collection;

import com.jakeapp.core.domain.ILogable;
import com.jakeapp.core.domain.JakeObject;
import com.jakeapp.core.domain.LogAction;
import com.jakeapp.core.domain.logentries.LogEntry;
import org.apache.log4j.Logger;

/**
 * An attributed version of a JakeObject.
 * The attributed version includes information about the synchronization
 * status of a JakeObject e.g. if the object is in sync with other
 * clients, if it is shared, deleted or has been modified.
 * @author johannes
 */
public class Attributed<T extends JakeObject> extends JakeObjectStatus {

	static final Logger log = Logger.getLogger(Attributed.class);
	private T jakeObject;
	private long lastModificationDate;
	private long size;

	/**
	 * @param jakeObject The Jakeobject whose status is described.
	 * @param lastVersionLogEntry {@link JakeObjectStatus}
	 * @param lastLockLogEntry {@link JakeObjectStatus}
	 * @param objectExistsLocally {@link JakeObjectStatus}
	 * @param checksumDifferentFromLastNewVersionLogEntry {@link JakeObjectStatus}
	 * @param hasUnprocessedLogEntries {@link JakeObjectStatus}
	 * @param lastProcessedLogAction {@link JakeObjectStatus}
	 * @param lastModificationDate Date of the last modification.
	 * @param size Size of the <code>JakeObject</code>. Either the length of the content for notes
	 * 	or the filesize (in bytes) for files.
	 * */
	public Attributed(T jakeObject, LogEntry<? extends ILogable> lastVersionLogEntry,
			LogEntry<? extends ILogable> lastLockLogEntry, boolean objectExistsLocally,
			boolean checksumDifferentFromLastNewVersionLogEntry,
			boolean hasUnprocessedLogEntries, LogAction lastProcessedLogAction,
			long lastModificationDate, long size) {
		super(lastVersionLogEntry, lastLockLogEntry, objectExistsLocally,
				checksumDifferentFromLastNewVersionLogEntry, hasUnprocessedLogEntries,
				lastProcessedLogAction);
		this.jakeObject = jakeObject;
		this.lastModificationDate = lastModificationDate;
		this.size = size;
	}

	public long getSize() {
		return this.size;
	}

	/**
	 * Get JakeObject, typesave
	 * @return
	 */
	public T getJakeObject() {
		return this.jakeObject;
	}

	/**
	 * Get the last modification date
	 * @return
	 */
	public long getLastModificationDate() {
		return this.lastModificationDate;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getJakeObject() + "] "
				+ super.toString();
	}

	/**
	 * Transforms a Collection of attributed JakeObjects to a Collection of
	 * plain JakeObjects
	 * @param <T> a concrete JakeObject-subclass.
	 * @param collection The collection to transform.
	 * @return A copy of collection with exactly the same elements as collection,
	 * or null, if collection was null.
	 */
	public static <T extends JakeObject> Collection<T> extract(Collection<Attributed<T>> collection) {
		Collection<T> result;
		
		if (collection==null) return null;
		
		result = new ArrayList<T>();
		
		for (Attributed<T> attributed : collection)
			result.add(attributed.getJakeObject());
		
		return result;
	}
	
	/**
	 * 
	 * TODO refactor - move method
	 * Transforms a Collection of specialized JakeObjects to a Collection of
	 * plain JakeObjects
	 * @param <T> a concrete JakeObject-subclass.
	 * @param collection The collection to transform.
	 * @return A copy of collection with exactly the same elements as collection,
	 * or null, if collection was null.
	 */
	public static <T extends JakeObject> Collection<JakeObject> castDownCollection(Collection<T> collection) {
		Collection<JakeObject> result;
		
		if (collection==null) return null;
		
		result = new ArrayList<JakeObject>();
		for (T jo : collection)
			result.add(jo);
		
		return result;
	}
}