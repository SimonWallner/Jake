package com.jakeapp.core.services.futures;

import java.util.Collection;

import com.jakeapp.availablelater.AvailableNowObject;
import com.jakeapp.core.domain.NoteObject;
import com.jakeapp.core.domain.Project;
import com.jakeapp.core.util.ProjectApplicationContextFactory;

/**
 * gets all notes that lie in the database
 * 
 * @author johannes
 *
 */
public class AllLocalProjectNotesFuture extends
		AvailableNowObject<Collection<NoteObject>> {

	public AllLocalProjectNotesFuture(ProjectApplicationContextFactory context, Project p) {
		super(context.getNoteObjectDao(p).getAll());
	}
	
}