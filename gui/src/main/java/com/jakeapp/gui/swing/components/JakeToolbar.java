package com.jakeapp.gui.swing.components;

import com.explodingpixels.macwidgets.LabeledComponentGroup;
import com.explodingpixels.macwidgets.MacButtonFactory;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.TriAreaComponent;
import com.jakeapp.core.domain.InvitationState;
import com.jakeapp.gui.swing.actions.project.CreateProjectAction;
import com.jakeapp.gui.swing.actions.file.ImportFileAction;
import com.jakeapp.gui.swing.actions.users.InviteUsersAction;
import com.jakeapp.gui.swing.actions.abstracts.ProjectAction;
import com.jakeapp.gui.swing.controls.SearchField;
import com.jakeapp.gui.swing.filters.FileObjectNameFilter;
import com.jakeapp.gui.swing.filters.NoteObjectFilter;
import com.jakeapp.gui.swing.panels.FilePanel;
import com.jakeapp.gui.swing.panels.NotesPanel;
import com.jakeapp.gui.swing.JakeMainView;
import com.jakeapp.gui.swing.ContextPanelEnum;
import com.jakeapp.gui.swing.xcore.EventCore;
import com.jakeapp.gui.swing.globals.JakeContext;
import com.jakeapp.gui.swing.helpers.ImageLoader;

import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.application.ResourceMap;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.PatternSyntaxException;

/**
 * Implementation of the Toolbar
 */
public class JakeToolbar {
		private static final Logger log = Logger.getLogger(JakeToolbar.class);
	private final JakeMainView jakeMainView;
	private SearchField searchField;
	private AbstractButton createProjectButton;
	private AbstractButton addFilesButton;
	private AbstractButton invitePeopleButton;
	private AbstractButton inspectorButton;

	private final ResourceMap resourceMap;
	private final EventCore eventCore;
	private final FilePanel filePanel;


	public JakeToolbar(JakeMainView jakeMainView, EventCore eventCore, FilePanel filePanel) {
		this.jakeMainView = jakeMainView;
		this.eventCore = eventCore;
		this.filePanel = filePanel;
		resourceMap = jakeMainView.getResourceMap();

	}

	/**
	 * Creates the unified toolbar on top.
	 *
	 * @return TriAreaComponent of toolbar.
	 */
	public TriAreaComponent createToolBar() {
		// create empty toolbar
		TriAreaComponent toolBar = MacWidgetFactory.createUnifiedToolBar();
		// Create Project
		ProjectAction createProjectAction;
		createProjectAction = new CreateProjectAction(false, jakeMainView.getResourceMap());
		JButton createProjectJButton = new JButton();
		createProjectJButton.setAction(createProjectAction);
		createProjectButton = MacButtonFactory.makeUnifiedToolBarButton(createProjectJButton);
		createProjectButton.setEnabled(true);
		createProjectButton.setBorder(new LineBorder(Color.BLACK, 0));
		toolBar.addComponentToLeft(createProjectButton, 10);

		// Add Files
		Icon addFilesIcon = ImageLoader.getScaled(jakeMainView.getClass(), 
						"/icons/toolbar-addfiles.png", 32);

		JButton jCreateAddFilesButton = new JButton(resourceMap.getString("toolbarAddFiles"),
						addFilesIcon);

		addFilesButton = MacButtonFactory.makeUnifiedToolBarButton(jCreateAddFilesButton);
		addFilesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				new ImportFileAction(eventCore, JakeToolbar.this.filePanel, resourceMap).openImportDialog();
			}
		});
		addFilesButton.setEnabled(true);
		jCreateAddFilesButton.setBorder(new LineBorder(Color.BLACK, 0));
		toolBar.addComponentToLeft(addFilesButton, 10);

		/*
		// Create Note
		Icon noteIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				  getClass().getResource("/icons/notes.png")).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
		JButton jCreateNodeButton = new JButton(getResourceMap().getString("toolbarCreateNote"), noteIcon);

		addFilesButton = MacButtonFactory.makeUnifiedToolBarButton(jCreateNodeButton);
		addFilesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				addFilesAction();
			}
		});
		addFilesButton.setEnabled(true);
		jCreateNodeButton.setBorder(new LineBorder(Color.BLACK, 0));
		toolBar.addComponentToLeft(addFilesButton, 10);
		*/

		// Add People

		JButton invitePeopleJButton = new JButton(new InviteUsersAction(false, jakeMainView.getResourceMap()));
		invitePeopleButton = MacButtonFactory.makeUnifiedToolBarButton(invitePeopleJButton);
		invitePeopleButton.setBorder(new LineBorder(Color.BLACK, 0));
		toolBar.addComponentToLeft(invitePeopleButton, 10);


		/*
			 // Announce File
			 Icon announceIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						getClass().getResourceMap("/icons/announce.png")).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
			 JButton announceJButton = new JButton("Announce", announceIcon);


			 AbstractButton announceButton =
						MacButtonFactory.makeUnifiedToolBarButton(announceJButton);

			 announceButton.setEnabled(true);
			 announceJButton.setBorder(new LineBorder(Color.BLACK, 0));
			 toolBar.addComponentToRight(announceButton, 10);

			 // Pull File
			 Icon pullIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						getClass().getResourceMap("/icons/pull.png")).getScaledInstance(32, 32, Image.SCALE_SMOOTH));

			 JButton jPullButton = new JButton("Pull", pullIcon);
			 AbstractButton pullButton =
						MacButtonFactory.makeUnifiedToolBarButton(jPullButton);
			 pullButton.setEnabled(true);
			 jPullButton.setBorder(new LineBorder(Color.BLACK, 0));

			 toolBar.addComponentToRight(pullButton, 10);
  */
		/*
					// Lock File
					Icon lockIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
									getClass().getResourceMap("/icons/lock.png")).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
					JButton jLockButton = new JButton("Lock File", lockIcon);
					AbstractButton lockButton =
									MacButtonFactory.makeUnifiedToolBarButton(
													jLockButton);
					lockButton.setEnabled(false);
					jLockButton.setBorder(new LineBorder(Color.BLACK, 0));
					toolBar.addComponentToRight(lockButton, 10);
	*/

		Icon inspectorIcon = ImageLoader.getScaled(jakeMainView.getClass(),
				"/icons/inspector.png", 32);
		JButton inspectorJButton = new JButton("Inspector", inspectorIcon);
		inspectorJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				jakeMainView.setInspectorEnabled(!jakeMainView.isInspectorEnabled());
			}
		});


		// The mighty Inspector
		inspectorButton = MacButtonFactory.makeUnifiedToolBarButton(inspectorJButton);
		inspectorButton.setEnabled(true);
		inspectorJButton.setBorder(new LineBorder(Color.BLACK, 0));
		toolBar.addComponentToRight(inspectorButton, 10);


		//announceButton.setBackground(Color.);

		/*
				  JButton annouceButton = new JButton("Announce");
				  annouceButton.putClientProperty("JButton.buttonType", "textured");
					*/
		//toolBar.addComponentToLeft(announceButton);
		//toolBar.add(announceButton);


		searchField = new SearchField();
		searchField.setSize(searchField.getHeight(), 30);
		searchField.putClientProperty("JTextField.variant", "search");
		searchField.setSendsNotificationForEachKeystroke(true);
		toolBar.addComponentToRight(new LabeledComponentGroup("Search", searchField).getComponent());

//		final FilePanel filePanel = jakeMainView.getFilePanel();
		searchField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				log.trace("Search field: " + e.getActionCommand());
				if (e.getActionCommand().equals("")) {
					filePanel.resetFilter();
					NotesPanel.getInstance().resetFilter();
				} else {
					try {
						PatternFilter fileFilter = new FileObjectNameFilter(e.getActionCommand());
						PatternFilter notesFilter = new NoteObjectFilter(e.getActionCommand());

						FilterPipeline filePipeline = new FilterPipeline(fileFilter);
						FilterPipeline notesPipeline = new FilterPipeline(notesFilter);

						filePanel.switchToFlatAndFilter(filePipeline);

						NotesPanel.getInstance().setFilter(notesPipeline);
					} catch (PatternSyntaxException ex) {
						log.info("Invalid regex was entered in search field", ex);
					}
				}
			}
		});

		toolBar.addComponentToCenter(new LabeledComponentGroup("View", jakeMainView.getContextSwitcherPane()).getComponent());
		toolBar.installWindowDraggerOnWindow(jakeMainView.getFrame());

		updateToolBar();
		return toolBar;
	}

	/**
	 * Enables/disables the toolbar depending on current dataset
	 */
	public void updateToolBar() {
		boolean hasProject = JakeContext.getProject() != null;
		boolean isInvite = JakeContext.getProject() != null && JakeContext.getProject()
						.getInvitationState() == InvitationState.INVITED;
		boolean isProjectContext = jakeMainView.getContextViewPanel() == ContextPanelEnum.Project;
		boolean hasUser = JakeContext.getMsgService() != null;

		createProjectButton.setEnabled(hasUser);
		addFilesButton.setEnabled(hasProject && !isInvite);
		invitePeopleButton.setEnabled(hasProject && !isInvite);
		for (JToggleButton btn : jakeMainView.getContextSwitcherButtons()) {
			btn.setEnabled(isProjectContext && hasProject && !isInvite);
		}
		inspectorButton.setEnabled(jakeMainView.isInspectorAllowed());
		searchField.setEditable(hasProject);
	}
}