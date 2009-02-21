package com.jakeapp.gui.swing.models;

import com.jakeapp.core.domain.Project;
import com.jakeapp.core.synchronization.UserInfo;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.callbacks.ProjectChanged;
import com.jakeapp.gui.swing.callbacks.ProjectSelectionChanged;
import com.jakeapp.gui.swing.controls.MutableListModel;
import com.jakeapp.gui.swing.exceptions.PeopleOperationFailedException;
import com.jakeapp.gui.swing.helpers.ExceptionUtilities;
import com.jakeapp.gui.swing.helpers.JakeHelper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Capsulates UserId into a ListModel.
 */
public class PeopleListModel extends AbstractListModel
		  implements MutableListModel, ProjectSelectionChanged, ProjectChanged {
	private static final Logger log = Logger.getLogger(PeopleListModel.class);

	private List<UserInfo> people;
	private Project project;

	public PeopleListModel() {

		this.people = new ArrayList<UserInfo>();
		
		// register for events
		JakeMainApp.getCore().addProjectChangedCallbackListener(this);
		JakeMainApp.getApp().addProjectSelectionChangedListener(this);

		updateModel();
	}

	public int getSize() {
		return this.people != null ? this.people.size() : 0;
	}

	public Object getElementAt(int i) {
		return this.people.get(i);
	}


	public void projectChanged(ProjectChangedEvent ev) {
		updateModel();
	}

	private void updateModel() {
		try {
			this.people = JakeMainApp.getCore().getProjectUser(getProject());
		} catch (PeopleOperationFailedException e) {
			this.people = new ArrayList<UserInfo>();
			ExceptionUtilities.showError(e);
		}

		this.fireContentsChanged(this, 0, getSize());
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project pr) {
		this.project = pr;

		updateModel();
	}

	@Override
	public boolean isCellEditable(int index) {
		// we are not editable!
		return index > 0;
	}

	@Override
	public void setValueAt(Object value, int index) {
		if (!JakeMainApp.getCore().setPeopleNickname(getProject(), people.get(index).getUser(), (String) value)) {

			JakeHelper.showMsgTranslated("PeopleListRenameNicknameInvalid", JOptionPane.WARNING_MESSAGE);

			// redraw
			fireContentsChanged(this, index, index);
		}
	}
}