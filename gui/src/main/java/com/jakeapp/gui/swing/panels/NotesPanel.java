/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NotesPanel.java
 *
 * Created on Dec 3, 2008, 2:00:15 AM
 */

package com.jakeapp.gui.swing.panels;

import com.jakeapp.core.domain.NoteObject;
import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.ICoreAccess;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.actions.CommitNoteAction;
import com.jakeapp.gui.swing.actions.DeleteNoteAction;
import com.jakeapp.gui.swing.actions.NewNoteAction;
import com.jakeapp.gui.swing.actions.SoftlockNoteAction;
import com.jakeapp.gui.swing.callbacks.NoteSelectionChanged;
import com.jakeapp.gui.swing.callbacks.ProjectChanged;
import com.jakeapp.gui.swing.callbacks.ProjectSelectionChanged;
import com.jakeapp.gui.swing.controls.cmacwidgets.ITunesTable;
import com.jakeapp.gui.swing.helpers.Colors;
import com.jakeapp.gui.swing.helpers.JakePopupMenu;
import com.jakeapp.gui.swing.helpers.Platform;
import com.jakeapp.gui.swing.models.NotesTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The NotesPanel
 *
 * @author studpete, simon
 */
public class NotesPanel extends javax.swing.JPanel implements ProjectSelectionChanged, ProjectChanged, ListSelectionListener {

	private static final long serialVersionUID = -7703570005631651276L;
	private static NotesPanel instance;
	private static Logger log = Logger.getLogger(NotesPanel.class);
	private java.util.List<NoteSelectionChanged> noteSelectionListeners = new ArrayList<NoteSelectionChanged>();
	private NotesTableModel notesTableModel;
	private javax.swing.JScrollPane notesTableScrollPane;
	private javax.swing.JSplitPane mainSplitPane;
	private org.jdesktop.swingx.JXPanel noteReadPanel;
	private org.jdesktop.swingx.JXTable notesTable;
	private ResourceMap resourceMap;
	private JTextArea noteReader;
	private ICoreAccess core;
	private Project currentProject;
	private JButton shareUpdateBtn;
	private JButton lockBtn;

	private class NoteContainerMouseListener extends MouseAdapter {
		private NotesPanel panel;
		private JTable table;
		private NotesTableModel tableModel;
		private JPopupMenu popupMenu;

		{
			this.popupMenu = new JakePopupMenu();
			this.popupMenu.add(new JMenuItem(new NewNoteAction()));
			this.popupMenu.addSeparator();
			this.popupMenu.add(new JMenuItem(new DeleteNoteAction()));
			this.popupMenu.add(new JMenuItem(new CommitNoteAction()));
			this.popupMenu.addSeparator();
			// this.popupMenu.add(new JMenuItem(new SaveNoteAction()));
			this.popupMenu.add(new JMenuItem(new SoftlockNoteAction()));
		}

		public NoteContainerMouseListener(NotesPanel panel, JTable table, NotesTableModel tableModel) {
			super();
			this.panel = panel;
			this.table = table;
			this.tableModel = tableModel;
		}

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (SwingUtilities.isRightMouseButton(mouseEvent)) {

				Point p = mouseEvent.getPoint();
				int rowNumber = this.table.rowAtPoint(p);

				if (rowNumber == -1) { // click in empty area
					this.panel.notifyNoteSelectionListeners(new ArrayList<NoteObject>());
					this.table.clearSelection();
				} else { //click hit something
					boolean found = false;
					for (int row : this.table.getSelectedRows()) {
						if (row == rowNumber) {
							found = true;
							break;
						}
					}
					if (!found) {
						this.table.changeSelection(rowNumber, 0, false, false);
					}
				}
				showMenu(mouseEvent);
			} else if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
				if (this.table.rowAtPoint(mouseEvent.getPoint()) == -1) {
					this.table.clearSelection();
				}
			}
		}

		private void showMenu(MouseEvent me) {
			this.popupMenu.show(this.table, (int) me.getPoint().getX(), (int) me.getPoint().getY());
		}
	}

	/**
	 * Creates new form NotesPanel
	 */
	public NotesPanel() {
		instance = this;

		// get resource map
		this.setResourceMap(org.jdesktop.application.Application.getInstance(
				  JakeMainApp.class).getContext()
				  .getResourceMap(NotesPanel.class));

		//get core
		this.core = JakeMainApp.getCore();

		// init components
		initComponents();

		// register the callbacks
		JakeMainApp.getApp().addProjectSelectionChangedListener(this);
		JakeMainApp.getApp().getCore().addProjectChangedCallbackListener(this);
		this.notesTable.getSelectionModel().addListSelectionListener(this);

		// TODO: make this a styler property
		if (!Platform.isMac()) {
			this.notesTable.setHighlighters(HighlighterFactory.createSimpleStriping());
		}
		final JPopupMenu notesPopupMenu = new JakePopupMenu();

		this.notesTable.addMouseListener(new NoteContainerMouseListener(this, this.notesTable, this.notesTableModel));
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (this.notesTable.getSelectedRow() == -1) {
			this.noteReader.setText("");
			this.notifyNoteSelectionListeners(new ArrayList<NoteObject>());
		} else {
			String text;
			text = this.notesTableModel.getNoteAtRow(this.notesTable.getSelectedRow()).getContent();
			this.noteReader.setText(text);

			List<NoteObject> selectedNotes = new ArrayList<NoteObject>();
			for (int row : this.notesTable.getSelectedRows()) {
				selectedNotes.add(this.notesTableModel.getNoteAtRow(row));
			}
			this.notifyNoteSelectionListeners(selectedNotes);
		}
	}


	public ResourceMap getResourceMap() {
		return this.resourceMap;
	}


	public void setResourceMap(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public Project getCurrentProject() {
		return this.currentProject;
	}

	/**
	 * Get the current Project.
	 *
	 * @param currentProject
	 */
	private void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}


	@Override
	public void projectChanged(ProjectChangedEvent ignored) {
		log.info("received projectChangedEvent: " + ignored.toString());
		this.notesTableModel.update();
	}

	@Override
	public void setProject(Project pr) {
		this.setCurrentProject(pr);
		this.notesTableModel.update(this.getCurrentProject());
	}

	private boolean isNoteSelected() {
		return this.notesTable.getSelectedRow() >= 0;
	}

	/**
	 * Create a new note both in the persistence and in the notes table.
	 */
	private void newNote() {
		this.core.newNote(new NoteObject(UUID.randomUUID(), this.getCurrentProject(), "new note"));
	}

	/**
	 * Commit the currently selected note.
	 */
	private void shareSelectedNote() {
		//TODO
	}

	/**
	 * Save the currently selected note. If it is a local note, it is only saved, if it is a shared note
	 * it is automatically commited.
	 */
	private void saveSelectedNote() {
		//TODO
	}


	private void initComponents() {
		this.setLayout(new MigLayout("wrap 1, fill, ins 0"));

		mainSplitPane = new JSplitPane();
		notesTableScrollPane = new JScrollPane();
		notesTable = new ITunesTable();

		mainSplitPane.setBorder(null);
		mainSplitPane.setDividerSize(2);
		mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		notesTableScrollPane.setViewportView(notesTable);
		mainSplitPane.setLeftComponent(notesTableScrollPane);

		this.add(mainSplitPane, "grow");

		// set up table model
		this.notesTableModel = new NotesTableModel();
		this.notesTable.setModel(this.notesTableModel);
		this.notesTable.setSortable(true);
		this.notesTable.setColumnControlVisible(true);

		// FIXME: set column with for soft lock and shared note
		this.notesTable.getColumnModel().getColumn(0).setResizable(false); // lock
		this.notesTable.getColumnModel().getColumn(0).setMaxWidth(20);
		this.notesTable.getColumnModel().getColumn(1).setResizable(false); // shared note
		this.notesTable.getColumnModel().getColumn(1).setMaxWidth(20);
		this.notesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		this.notesTable.getSelectionModel().addListSelectionListener(this);

		this.notesTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					new DeleteNoteAction().execute();
				}
			}
		});

		noteReadPanel = new JXPanel(new MigLayout("wrap 1, ins 0, fill"));
		noteReadPanel.setBackground(getResourceMap().getColor("noteReadPanel.background"));

		JPanel noteControlPanel = new JPanel(new MigLayout("nogrid, ins 0"));

		shareUpdateBtn = new JButton();
		shareUpdateBtn.putClientProperty("JButton.buttonType", "textured");
		noteControlPanel.add(shareUpdateBtn);
		updateShareUpdateBtn();

		lockBtn = new JButton();
		updateLockBtn();
		lockBtn.putClientProperty("JButton.buttonType", "textured");
		noteControlPanel.add(lockBtn);

		this.noteReadPanel.add(noteControlPanel, "growx");

		this.noteReader = new JTextArea();
		this.noteReader.setLineWrap(true);
		this.noteReader.setOpaque(false);
		this.noteReader.setText("Enter your Note here.\nChanges will be saved automatically.");
		this.noteReader.setMargin(new Insets(8, 8, 8, 8));

		JScrollPane noteReaderScrollPane = new JScrollPane(this.noteReader);
		noteReaderScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		noteReaderScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		noteReaderScrollPane.setOpaque(false);
		noteReaderScrollPane.getViewport().setOpaque(false);
		noteReaderScrollPane.setBorder(new LineBorder(Color.BLACK, 0));

		this.noteReadPanel.add(noteReaderScrollPane, "grow");

		// set the background painter
		MattePainter mp = new MattePainter(Colors.Yellow.alpha(0.5f));
		GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
				  GlossPainter.GlossPosition.TOP);
		this.noteReadPanel.setBackgroundPainter(new CompoundPainter(mp, gp));

		mainSplitPane.setRightComponent(noteReadPanel);

	}

	private void updateLockBtn() {
		// TODO
		boolean isLocked = true;
		lockBtn.setText(getResourceMap().getString(isLocked ? "lockBtn" : "unLockBtn"));
	}

	/**
	 * update the button to set right label
	 */
	private void updateShareUpdateBtn() {
		// TODO
		boolean isSharedNote = true;

		if (isSharedNote) {
			shareUpdateBtn.setText(getResourceMap().getString("updateBtn"));
		} else {
			shareUpdateBtn.setText(getResourceMap().getString("shareBtn"));
		}
	}

	public static NotesPanel getInstance() {
		return instance;
	}

	public void addNoteSelectionListener(NoteSelectionChanged listener) {
		this.noteSelectionListeners.add(listener);
	}

	public void removeNoteSelectionListener(NoteSelectionChanged listener) {
		this.noteSelectionListeners.remove(listener);
	}

	public void notifyNoteSelectionListeners(java.util.List<NoteObject> selectedNotes) {
		log.debug("notify selection listeners: " + selectedNotes.toArray());
		for (NoteSelectionChanged listener : this.noteSelectionListeners) {
			listener.noteSelectionChanged(new NoteSelectionChanged.NoteSelectedEvent(selectedNotes));
		}
	}

	/**
	 * Get a <code>List</code> of selected notes.
	 *
	 * @return the list of currently selected notes. If nothing is selected, an empty list is returned.
	 */
	public List<NoteObject> getSelectedNotes() {
		log.debug("get selected notes...");
		List<NoteObject> selectedNotes = new ArrayList<NoteObject>();

		if (this.notesTable.getSelectedRow() == -1) {
			return selectedNotes;
		}

		log.debug("selcted notes count: " + notesTable.getSelectedRowCount());
		for (int row : this.notesTable.getSelectedRows()) {
			selectedNotes.add(this.notesTableModel.getNoteAtRow(row));
		}
		return selectedNotes;
	}

	public void resetFilter() {
		log.debug("resetting filter...");
		this.notesTable.setFilters(null);
	}

	public void setFilter(FilterPipeline filterPipeline) {
		this.notesTable.setFilters(filterPipeline);
	}
}
