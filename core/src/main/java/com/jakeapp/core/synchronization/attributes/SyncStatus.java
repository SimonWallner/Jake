/**
 * 
 */
package com.jakeapp.core.synchronization.attributes;

import com.jakeapp.core.domain.LogAction;
import com.jakeapp.core.synchronization.SyncStatusCalculator;

public enum SyncStatus {
	/** no difference **/
	SYNC,

	/**
	 * you did something. you can announce it.
	 */
	MODIFIED_LOCALLY,

	/**
	 * someone did something. you can pull it.
	 */
	MODIFIED_REMOTELY,

	/**
	 * you and someone else did something. you got to sort it out.
	 */
	CONFLICT;

	/**
	 * @param checksumDifferentFromLastNewVersionLogEntry
	 * @param hasUnprocessedLogEntries
	 * @param lastProcessedLogAction
	 * @param objectExistsLocally
	 * @see SyncStatusCalculator#SyncStatusCalculator(boolean, boolean,
	 *      LogAction, boolean)
	 * @return
	 */
	static SyncStatus getSyncStatus(boolean checksumDifferentFromLastNewVersionLogEntry,
			boolean hasUnprocessedLogEntries, LogAction lastProcessedLogAction,
			boolean objectExistsLocally) {
		return new SyncStatusCalculator(checksumDifferentFromLastNewVersionLogEntry,
				hasUnprocessedLogEntries, lastProcessedLogAction, objectExistsLocally)
				.getSyncStatus();
	}
}