/**
 * 
 */
package com.jakeapp.core.services.futures;


import java.io.FileNotFoundException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.jakeapp.availablelater.AvailableLaterObject;
import com.jakeapp.availablelater.AvailableLaterWrapperObject;
import com.jakeapp.core.domain.FileObject;
import com.jakeapp.jake.fss.IFSService;
import com.jakeapp.jake.fss.exceptions.InvalidFilenameException;
import com.jakeapp.jake.fss.exceptions.NotAFileException;

/**
 * <code>AvailableLaterObject</code> returning a <code>Long</code> representing the cummulated size of the given
 * <code>Collection</code> of <code>FileObject</code>s.
 */
/**
 * Calculates the total size of a list of files.
 * @author djinn
 */
public class ProjectSizeTotalFuture extends AvailableLaterWrapperObject<Long, Collection<FileObject>> {

	private static final Logger log = Logger.getLogger(ProjectSizeTotalFuture.class);

	private IFSService fss;

	public ProjectSizeTotalFuture(IFSService fss, AvailableLaterObject<Collection<FileObject>> filesFuture) {
		super(filesFuture);
		this.fss = fss;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long calculate() {
		final String STATUS = "";
		long result = 0;
		Collection<FileObject> files;
		double progress=0d;
		double singlestep = 0;
		getListener().statusUpdate(progress, STATUS);
		
		files = this.getSource().get();
		if (files.size() > 0) singlestep = 1d / files.size();
		for (FileObject file : files) {
			try {
				try {
					result += this.fss.getFileSize(file.getRelPath());
				} catch (FileNotFoundException e) {
					// no size because only-remote or deleted.
				} catch (NotAFileException e) {
					log.warn("unexpected exception", e);
				} catch (InvalidFilenameException e) {
					log.warn("database is corrupt", e);
				}
			} catch (SecurityException se) {
				// empty catch
			}
			progress += singlestep;
			getListener().statusUpdate(progress, STATUS);
		}
		
		return result;
	}
}
