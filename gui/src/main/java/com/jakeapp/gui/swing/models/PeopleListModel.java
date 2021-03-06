package com.jakeapp.gui.swing.models;

import com.jakeapp.core.domain.Project;
import com.jakeapp.core.synchronization.UserInfo;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.callbacks.ContextChangedCallback;
import com.jakeapp.gui.swing.callbacks.DataChangedCallback;
import com.jakeapp.gui.swing.callbacks.ProjectChangedCallback;
import com.jakeapp.gui.swing.controls.MutableListModel;
import com.jakeapp.gui.swing.exceptions.PeopleOperationFailedException;
import com.jakeapp.gui.swing.globals.JakeContext;
import com.jakeapp.gui.swing.helpers.ExceptionUtilities;
import com.jakeapp.gui.swing.helpers.JakeHelper;
import com.jakeapp.gui.swing.helpers.UserHelper;
import com.jakeapp.gui.swing.xcore.EventCore;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Capsulates UserId into a ListModel.
 */
public class PeopleListModel extends AbstractListModel
				implements MutableListModel, ContextChangedCallback, ProjectChangedCallback,
				DataChangedCallback {
	private static final Logger log = Logger.getLogger(PeopleListModel.class);

	private List<UserInfo> people;

	public PeopleListModel() {

		this.people = new ArrayList<UserInfo>();

		// register for events
		EventCore.get().addProjectChangedCallbackListener(this);
		EventCore.get().addContextChangedListener(this);
		EventCore.get().addDataChangedCallbackListener(this);

		updateModel();
	}

	public int getSize() {
		return this.people != null ? this.people.size() : 0;
	}

	public Object getElementAt(int i) {
		if (this.people.size() > i) {
			return this.people.get(i);
		} else {
			log.warn("Tried to get Element on invalid index " + i + " size=" + this.people
							.size());
			return null;
		}
	}


	public void projectChanged(ProjectChangedEvent ev) {
		updateModel();
	}

	private void updateModel() {
		if (!JakeContext.isCoreInitialized())
			return;

		if (JakeContext.getProject() != null) {
			log.debug("updating people model...");
			try {
				this.people =
								JakeMainApp.getCore().getAllProjectMembers(JakeContext.getProject());
			} catch (PeopleOperationFailedException e) {
				this.people = new ArrayList<UserInfo>();
				ExceptionUtilities.showError(e);
			}

			this.fireContentsChanged(this, 0, getSize());
		}
	}

	@Override
	public boolean isCellEditable(int index) {
		// we are not editable!
		return UserHelper.isCurrentProjectMember(people.get(index).getUser());
	}

	@Override
	public void setValueAt(Object value, int index) {
		if (!JakeMainApp.getCore()
						.setUserNick(JakeContext.getProject(), people.get(index).getUser(),
										(String) value)) {

			JakeHelper.showMsgTranslated("PeopleListRenameNicknameInvalid",
							JOptionPane.WARNING_MESSAGE);

			// redraw
			fireContentsChanged(this, index, index);
		}
	}

	@Override public void contextChanged(EnumSet<Reason> reason, Object context) {
		if (reason.contains(Reason.Project)) {
			updateModel();
		}
	}

	@Override public void dataChanged(EnumSet<DataReason> dataReason, Project p) {
		if (dataReason.contains(DataReason.User)) {
			updateModel();
		}
	}
}