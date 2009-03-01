package com.jakeapp.gui.swing.xcore;

import com.jakeapp.core.domain.InvitationState;
import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.callbacks.DataChanged;
import com.jakeapp.gui.swing.helpers.JakeExecutor;
import com.jakeapp.gui.swing.worker.GetProjectsWorker;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * This is a generic cache that saves data from the core.
 * Especially useful to cache the data from AvailableLaterObjects.
 *
 * @author studpete
 */
public class ObjectCache {
	private static ObjectCache instance = new ObjectCache();

	private List<Project> myProjects = new ArrayList<Project>();
	private List<Project> invitedProjects = new ArrayList<Project>();

	// do not construct
	private ObjectCache() {
	}

	/**
	 * Returns the Singleton Instance.
	 * @return
	 */
	public static ObjectCache get() {
		return instance;
	}

	public List<Project> getMyProjects() {
		return myProjects;
	}

	public void setMyProjects(List<Project> myProjects) {
		this.myProjects = myProjects;

		EventCore.get().fireDataChanged(EnumSet.of(DataChanged.Reason.Projects));
	}

	public List<Project> getInvitedProjects() {
		return invitedProjects;
	}

	public void setInvitedProjects(List<Project> invitedProjects) {
		this.invitedProjects = invitedProjects;

		EventCore.get().fireDataChanged(EnumSet.of(DataChanged.Reason.Projects));
	}


	public void updateProjects() {
		JakeExecutor.exec(new GetProjectsWorker(EnumSet.of(InvitationState.ACCEPTED)));
		JakeExecutor.exec(new GetProjectsWorker(EnumSet.of(InvitationState.INVITED)));
	}

	public void updateAll() {
		updateProjects();
	}
}
