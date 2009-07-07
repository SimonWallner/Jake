package com.jakeapp.gui.swing;

import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.view.ProjectViewEnum;


/**
 * This Component stores the current View of the GUi and if selected, the current Project
 */
@Deprecated
public class ProjectViewHolder {

	private Project currentProject;
	private ProjectViewEnum currentView= ProjectViewEnum.EVENTS;

	public ProjectViewHolder() {
	}

	public ProjectViewEnum getCurrentView() {
		return currentView;
	}

	public void setCurrentView(ProjectViewEnum currentView) {
		this.currentView = currentView;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}


	

}