package com.jakeapp.gui.swing.view.menuBar.files;

import com.jakeapp.gui.swing.view.menuBar.AbstractMenuBarAction;
import com.jakeapp.gui.swing.controller.MenuBarController;

import java.awt.event.ActionEvent;


import org.jdesktop.application.ResourceMap;

import javax.swing.*;

public class DeleteFileAction extends AbstractMenuBarAction {

	public DeleteFileAction(MenuBarController controller, ResourceMap resourceMap) {
		super(controller, resourceMap);
		putValue(Action.NAME, resourceMap.getString("deleteMenuItem.text"));
	}

	@Override
	public void updateAction() {
		setEnabled(model.isProjectSet() && model.isFileSelected());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.deleteFiles();
	}
}