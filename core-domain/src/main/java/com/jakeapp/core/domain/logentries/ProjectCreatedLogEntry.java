package com.jakeapp.core.domain.logentries;

import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.User;
import com.jakeapp.core.domain.LogAction;
import com.jakeapp.core.domain.ILogable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * This <code>LogEntry</code> specifies that the given <code>Project</code> was created by the <code>User</code>
 * creating this <code>LogEntry</code>. 
 */
@Entity
@DiscriminatorValue(value = "PROJECT_CREATED")
public class ProjectCreatedLogEntry extends ProjectLogEntry {

	public ProjectCreatedLogEntry(Project project, User member) {
		super(LogAction.PROJECT_CREATED, project, member);
	}

	public ProjectCreatedLogEntry() {
		setLogAction(LogAction.PROJECT_CREATED);
	}


	public static ProjectLogEntry parse(LogEntry<? extends ILogable> logEntry) {
		ProjectCreatedLogEntry le = new ProjectCreatedLogEntry((Project) logEntry
				.getBelongsTo(), logEntry.getMember());
		le.setTimestamp(logEntry.getTimestamp());
		le.setUuid(logEntry.getUuid());
		return le;
	}
}
