package com.jakeapp.gui.swing.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import org.apache.log4j.Logger;

import com.jakeapp.core.domain.NoteObject;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.JakeMainView;
import com.jakeapp.gui.swing.ICoreAccess;
import com.jakeapp.gui.swing.actions.abstracts.NoteAction;
import com.jakeapp.gui.swing.exceptions.NoteOperationFailedException;
import com.jakeapp.gui.swing.helpers.ExceptionUtilities;

/**
 * Note Action to lock and unlock a note, on/off checkbox behaviour. Batch enabled.
 *
 * @author Simon
 */
public class SoftlockNoteAction extends NoteAction {

	private static final long serialVersionUID = -3793566528638754529L;
	private static Logger log = Logger.getLogger(SoftlockNoteAction.class);

	private boolean isLocked;

	public SoftlockNoteAction() {
		super();

		this.isLocked = false;
		String actionStr = JakeMainView.getMainView().getResourceMap().getString("softLockNote");
		putValue(Action.NAME, actionStr);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ICoreAccess core = JakeMainApp.getCore();
		boolean cachedNewLockingState = !this.isLocked;

		for (NoteObject note : this.getSelectedNotes()) {
			core.setSoftLock(note, cachedNewLockingState, null);
		}
	}

	@Override
	public void updateAction() {
		if (this.getSelectedNotes().size() > 0) {
			this.setEnabled(true);

			this.isLocked = JakeMainApp.getCore().isSoftLocked(this.getSelectedNotes().get(0));
			boolean isLocal = true;
			try {
				isLocal = JakeMainApp.getCore().isLocalNote(this.getSelectedNotes().get(0));
			} catch (NoteOperationFailedException e) {
				ExceptionUtilities.showError(e);
			}
			if (isLocal) {
				this.setEnabled(false);
			} else if (this.isLocked) {
				if (this.getSelectedNotes().size() == 1) {
					this.putValue(Action.NAME, JakeMainView.getMainView().getResourceMap().getString("unlockNote"));
				} else {
					this.putValue(Action.NAME, JakeMainView.getMainView().getResourceMap().getString("unlockNotes"));
				}
			} else {
				if (this.getSelectedNotes().size() == 1) {
					this.putValue(Action.NAME, JakeMainView.getMainView().getResourceMap().getString("softLockNote"));
				} else {
					this.putValue(Action.NAME, JakeMainView.getMainView().getResourceMap().getString("softLockNotes"));
				}
			}
		} else {
			this.setEnabled(false);
			this.putValue(Action.NAME, JakeMainView.getMainView().getResourceMap().getString("softLockNote"));
		}
	}
}
