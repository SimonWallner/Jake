package com.jakeapp.core.domain.logentries;

import com.jakeapp.core.domain.*;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * This <code>LogEntry</code> specifies that the given <code>Tag</code> was added to a <code>JakeObject</code> by the
 * <code>User</code> creating this <code>LogEntry</code>
 * The programmer has to make sure, that the corresponding <code>JakeObject</code> is set inside the <code>Tag</code>.
 * TODO: This will be changed in the final version. 
 */
@Entity
@DiscriminatorValue(value = "TAG_ADD")
public class TagAddLogEntry extends TagLogEntry {

	public TagAddLogEntry(Tag belongsTo, User member) {
		super(LogAction.TAG_ADD, belongsTo, member);
	}

	public TagAddLogEntry() {
		setLogAction(LogAction.TAG_ADD);
	}


	public static TagAddLogEntry parse(LogEntry<? extends ILogable> logEntry) {

		if (!logEntry.getLogAction().equals(LogAction.TAG_ADD))
			throw new UnsupportedOperationException();

		TagAddLogEntry le = new TagAddLogEntry((Tag) logEntry.getBelongsTo(), logEntry
				.getMember());
		le.setTimestamp(logEntry.getTimestamp());
		le.setUuid(logEntry.getUuid());
		return le;
	}

}
