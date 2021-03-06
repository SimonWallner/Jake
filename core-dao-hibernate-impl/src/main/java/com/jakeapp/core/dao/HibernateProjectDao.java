package com.jakeapp.core.dao;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.jakeapp.core.dao.exceptions.NoSuchProjectException;
import com.jakeapp.core.domain.Account;
import com.jakeapp.core.domain.InvitationState;
import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.exceptions.InvalidProjectException;

/**
 * Hibernate implementation of the <code>IProjectDAO</code> Interface.
 */
public class HibernateProjectDao extends HibernateDaoSupport implements IProjectDao {

	private static Logger log = Logger.getLogger(HibernateProjectDao.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project create(Project project) throws InvalidProjectException {
		if (project == null || project.getName() == null
				|| project.getName().length() == 0) {
			throw new InvalidProjectException();
		}

		if (project.getCredentials() == null) {
			throw new InvalidProjectException(
					"A project's Account may not be null.");
		}

		if (project.getProjectId() == null)
			throw new InvalidProjectException("projectId must not be null");

		log.debug("persisting project with uuid " + project.getProjectId());
		try {
			if (project.getProjectId().isEmpty())
				project.setProjectId(UUID.randomUUID());


			this.getHibernateTemplate().getSessionFactory().getCurrentSession().persist(
					project);
		} catch (DataAccessException dae) {
			throw new InvalidProjectException(dae);
		}

		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project read(UUID uuid) throws NoSuchProjectException {
		List<Project> results;

		if (uuid == null) {
			throw new NoSuchProjectException();
		}

		try {
			results = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
					.createQuery("FROM Project WHERE uuid = ?").setString(0,
							uuid.toString()).list();
			if (results.size() < 1) {
				log.debug("Didn't find a project belonging to uuid " + uuid.toString());
				throw new NoSuchProjectException();
			}

			return results.get(0);

		} catch (DataAccessException dae) {
			log.warn(dae);
			throw new NoSuchProjectException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project update(Project project) throws NoSuchProjectException {
		if (project == null || project.getProjectId() == null) {
			throw new NoSuchProjectException();
		}

		try {
			this.getHibernateTemplate().getSessionFactory().getCurrentSession().update(
					project);
			// this.getHibernateTemplate().update(project, LockMode.WRITE);
		} catch (DataAccessException dae) {
			throw new NoSuchProjectException();
		}

		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAll() {
		return this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery("FROM Project").list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Project project) throws NoSuchProjectException {
		if (project == null || project.getProjectId() == null) {
			throw new NoSuchProjectException();
		}

		log.info("deleting project: " + project.getProjectId());

		try {
			int row = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
					.createQuery("DELETE FROM Project WHERE uuid = ?").setString(0,
							project.getProjectId()).executeUpdate();
			if (row < 1)
				throw new NoSuchProjectException("affected rows count < 1");
		} catch (DataAccessException dae) {
			throw new NoSuchProjectException(dae);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAll(InvitationState state) {
		List<Project> result = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createQuery("FROM Project WHERE invitationstate = ?").setInteger(0,
						state.ordinal()).list();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAll(Account account) {
		List<Project> result = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession()
				.createQuery("FROM Project WHERE userid = ?").setString(0, account.getUuid()).list();

		return result;
	}
}
