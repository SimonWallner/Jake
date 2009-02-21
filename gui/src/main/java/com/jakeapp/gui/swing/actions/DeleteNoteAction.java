package com.jakeapp.gui.swing.actions;

import com.jakeapp.core.domain.NoteObject;
import com.jakeapp.core.synchronization.AttributedJakeObject;
import com.jakeapp.core.synchronization.UserInfo;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.JakeMainView;
import com.jakeapp.gui.swing.actions.abstracts.NoteAction;
import com.jakeapp.gui.swing.dialogs.generic.JSheet;
import com.jakeapp.gui.swing.dialogs.generic.SheetEvent;
import com.jakeapp.gui.swing.dialogs.generic.SheetListener;
import com.jakeapp.gui.swing.exceptions.NoteOperationFailedException;
import com.jakeapp.gui.swing.helpers.ExceptionUtilities;
import com.jakeapp.gui.swing.helpers.JakeHelper;
import com.jakeapp.gui.swing.helpers.Translator;
import com.jakeapp.gui.swing.panels.NotesPanel;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * DeleteNote Action, that deletes the selected Note from the given <code>notesTable.
 *
 * @author Simon
 */
public class DeleteNoteAction extends NoteAction {

	private static final long serialVersionUID = 8553169924173654143L;
	private static final Logger log = Logger.getLogger(DeleteNoteAction.class);

	public DeleteNoteAction() {
		super();

		String actionStr = JakeMainView.getMainView().getResourceMap().getString("deleteNoteMenuItem");
		putValue(Action.NAME, actionStr);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
				final List<AttributedJakeObject<NoteObject>> cache = new ArrayList<AttributedJakeObject<NoteObject>>(getSelectedNotes());

		ResourceMap map = NotesPanel.getInstance().getResourceMap();
		String[] options = {map.getString("confirmDeleteNote.ok"), map.getString("genericCancel")};
		String text;

		
		if (cache.size() == 1) { //single delete
			if (cache.get(0).isLocked()) { //is locked
				UserInfo lockOwner = JakeMainApp.getCore().getUserInfo(cache.get(0).getLockLogEntry().getMember());

				if (lockOwner.getUser().equals(JakeMainApp.getCurrentUser())) { //local member is owner
					text = map.getString("confirmDeleteNote.text");
				} else {
					text = Translator.get(map, "confirmDeleteLockedNote.text", lockOwner.getNickName());
				}
			} else {
				text = map.getString("confirmDeleteNote.text");
			}
		} else { //batch delete
			log.debug("batch delete -------------------------------------------------------------");
			boolean locked = false;
			for (AttributedJakeObject<NoteObject> note : cache) {
				if (note.isLocked() && !JakeHelper.isEditable(note)) {
					locked = true;
					break;
				}
			}
			if (locked) {
				log.debug("at least one file is locked");
				text = map.getString("confirmDeleteLockedNotes.text");
			} else {
				log.debug("no file is locked or locked by the local user");
				text = map.getString("confirmDeleteNotes.text");
			}
		}
		
		
		JSheet.showOptionSheet(NotesPanel.getInstance(), text,
				  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0], new SheetListener() {
			@Override
			public void optionSelected(SheetEvent evt) {
				if (evt.getOption() == 0) {
					for (AttributedJakeObject<NoteObject> note : cache) {
						try {
							JakeMainApp.getCore().deleteNote(note.getJakeObject());
						} catch (NoteOperationFailedException e) {
							ExceptionUtilities.showError(e);
						}
					}
					//XXX update view
					refreshNotesPanel();
				}
			}
		});
	}

	@Override
	public void updateAction() {
		this.setEnabled(this.hasSelectedNotes());
	}
}
