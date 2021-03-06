package com.jakeapp.gui.swing.helpers.pftools;

import com.jakeapp.gui.swing.helpers.FileIconLabelHelper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;

/**
 * Mac implementation if PfTools
 *
 * @author studpete
 */
public class MacPfTools extends AbstractPfTools {
	private static final Logger log = Logger.getLogger(MacPfTools.class);

	/**
	 * Inits the native lib
	 */
	public MacPfTools() {

		// need to fix the quaqua lib paths
		// -Djava.library.path=
		//System.load(FileUtilities.getApplicationPath() + "/quaqua");
	}

	/**
	 * Mac version of getFileIcon, supports getIcon via native lib.
	 * Get up to 512x512 as native icons.
	 *
	 * @param file
	 * @param size
	 * @return
	 */
	@Override
	public Icon getFileIcon(File file, int size) {
		log.debug("file: " + file + ", size: " + size);
		//return ch.randelshofer.quaqua.filechooser.Files.getIcon(file, size);
		return FileIconLabelHelper.getIcon(file);
	}
}
