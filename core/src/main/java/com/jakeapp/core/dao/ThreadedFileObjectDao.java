package com.jakeapp.core.dao;
import com.jakeapp.core.dao.exceptions.NoSuchJakeObjectException;
import com.jakeapp.core.domain.FileObject;
import java.util.List;
import java.util.UUID;
import com.jakeapp.core.util.InjectableTask;
import com.jakeapp.core.util.SpringThreadBroker;

public class ThreadedFileObjectDao implements IFileObjectDao {

	private IFileObjectDao dao;

	public ThreadedFileObjectDao(IFileObjectDao dao) {
		this.dao = dao;
	}

	// This file was automatically generated by generateDao.sh. Do not modify. 

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public FileObject persist(final FileObject foin) {
		
		try {
			return SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<FileObject>() {

				@Override
				public FileObject calculate() throws Exception {
					return ThreadedFileObjectDao.this.dao.persist(foin);
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public FileObject get(final UUID objectId) throws NoSuchJakeObjectException {
		
		try {
			return SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<FileObject>() {

				@Override
				public FileObject calculate() throws Exception {
					return ThreadedFileObjectDao.this.dao.get(objectId);
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public List<FileObject> getAll() {
		
		try {
			return SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<List<FileObject>>() {

				@Override
				public List<FileObject> calculate() throws Exception {
					return ThreadedFileObjectDao.this.dao.getAll();
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void delete(final FileObject jakeObject) {
		
		try {
			SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<Void>() {

				@Override
				public Void calculate() throws Exception {
					ThreadedFileObjectDao.this.dao.delete(jakeObject);
					return null;
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public FileObject complete(final FileObject jakeObject) throws NoSuchJakeObjectException {
		
		try {
			return SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<FileObject>() {

				@Override
				public FileObject calculate() throws Exception {
					return ThreadedFileObjectDao.this.dao.complete(jakeObject);
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public FileObject get(final String relpath) throws NoSuchJakeObjectException {
		
		try {
			return SpringThreadBroker.getThreadForObject(this).doTask(new InjectableTask<FileObject>() {

				@Override
				public FileObject calculate() throws Exception {
					return ThreadedFileObjectDao.this.dao.get(relpath);
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}


}
