package com.jakeapp.core.dao;

import com.jakeapp.core.dao.exceptions.NoSuchProjectException;
import com.jakeapp.core.domain.Account;
import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.ProtocolType;
import com.jakeapp.core.domain.exceptions.InvalidCredentialsException;
import com.jakeapp.core.domain.exceptions.InvalidProjectException;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.UUID;


/**
 * Unit Tests for HibernateProjectDao
 * // TODO MERGE THIS WITCH AbstractAccountDaoTest
 */
@ContextConfiguration // global
public abstract class AbstractProjectCredentialsTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final String PROJECT_DAO_BEAN_ID = "projectDao";

	private static final String ACCOUNT_DAO_BEAN_ID = "accountDao";


	private static Logger log = Logger.getLogger(AbstractProjectCredentialsTest.class);

	@Autowired
	private IProjectDao projectDao;
	@Autowired
	private IAccountDao accountDao;

	public IProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public IAccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(IAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	private File systemTmpDir = new File(System.getProperty("java.io.tmpdir"));


	private String project_1_uuid = "480f3166-06f5-40db-93b7-a69789d1fcd2";

	Account credentials1;

	Project project1 = new Project("someName", UUID.fromString(project_1_uuid), null,
			systemTmpDir);



	@Before
	public void setUp() {

		credentials1 = new Account("me@localhost", "mypasswd", ProtocolType.XMPP);
		credentials1.setUuid(project_1_uuid);
		credentials1.setSavePassword(true);
		project1.setCredentials(credentials1);


	}

	@After
	public void tearDown() {
	}

	@Transactional
	@Test
	public final void testProjectDao() throws InvalidProjectException,
			NoSuchProjectException {
		Project actual;
		Account credentials = this.getAccountDao().create(credentials1);
		actual = this.getProjectDao().create(project1);
		Assert.assertEquals(project1, actual);
		Assert.assertEquals(1, this.getProjectDao().getAll().size());
	}

	@Transactional
	@Test
	public final void testServiceCredentialsDao() throws InvalidProjectException,
			NoSuchProjectException {
		
		//since rollback may not have happened, delete possibly inserted credentials1
		try {
			this.getAccountDao().delete(credentials1);
		}
		catch (Exception ex) {
			//empty handling
		}
		
		int before = this.getAccountDao().getAll().size();
		Account credentials = this.getAccountDao().create(
				credentials1);
		Assert.assertEquals(credentials.getUuid(), credentials1.getUuid());
		Assert.assertEquals(credentials.getPlainTextPassword(), credentials1.getPlainTextPassword());
		Assert.assertEquals(credentials.getUserId(), credentials1.getUserId());
		Assert.assertEquals(credentials.getProtocolType(), credentials1.getProtocolType());
		Assert.assertEquals(before + 1, this.getAccountDao().getAll().size());
	}


	@Transactional
	@Test
	public final void testProjectWithServiceCredentialsDao()
			throws InvalidProjectException, NoSuchProjectException {

		Account credentials =
		this.getAccountDao().create(credentials1);
		Assert.assertEquals(credentials, credentials1);
		Assert.assertEquals(1,	this.getAccountDao().getAll().size());




		Project project;
		project = this.getProjectDao().create(project1);
		Assert.assertEquals(project1, project);
		Assert.assertEquals(1, this.getProjectDao().getAll().size());


		project.setCredentials(credentials1);
		project = this.getProjectDao().update(project);
		Assert.assertEquals(credentials1, project.getCredentials());

		Assert.assertEquals(1, this.getProjectDao().getAll().size());
		project = this.getProjectDao().getAll().get(0);
		Assert.assertEquals(credentials1.getUserId(), project.getCredentials()
				.getUserId());
	}

	@Transactional
	@Test
	public final void testProjectWithMsgServiceDao() throws Exception {
		Project p;
		Account sc = new Account("username", "password", ProtocolType.XMPP);
		sc.setUuid(project_1_uuid);
		this.getAccountDao().create(sc);


		com.jakeapp.core.domain.IMsgService xmppMsgService = mock(com.jakeapp.core.domain.IMsgService.class);
		when(xmppMsgService.getProtocolType()).thenReturn(ProtocolType.XMPP);


		// create Project
		p = new Project("lol", UUID.fromString(project_1_uuid), xmppMsgService,
				new File("/home/lol"));

		// connect Project and MessageService
		p.getMessageService().setServiceCredentials(sc);
		p.setCredentials(sc);

		// persist Project
		this.projectDao.create(p);

		/* CALL */
		p = this.getProjectDao().read(UUID.fromString(project_1_uuid));
		/* VALIDATE METHOD RESULTS */
		Assert.assertNotNull(p);
		Assert.assertNotNull(p.getMessageService());
		Assert.assertNotNull(p.getCredentials());
		Assert.assertEquals("username", p.getCredentials().getUserId());
	}


	@Transactional
	@Test(expected = InvalidCredentialsException.class)
	public final void testDoubleCreateShouldFail() throws Exception {
		UUID uuid = UUID.randomUUID();
		Account sc1 = new Account("username", "password",
				ProtocolType.XMPP);
		sc1.setUuid(uuid);
		sc1.setSavePassword(true);


		Account sc2 = new Account("username2", "password2",
				ProtocolType.XMPP);
		sc2.setUuid(uuid);
		sc2.setSavePassword(true);

		try
		{
			this.accountDao.create(sc1);
		}
		catch(InvalidCredentialsException e)
		{
			Assert.fail();
		}
		
		this.accountDao.create(sc2);
		Assert.fail();
	}
	
	@Transactional
	@Test
	public final void testSilentUpdateCredentials() throws Exception {
		UUID uuid = UUID.randomUUID();
		Account sc1 = new Account("username", "password", ProtocolType.XMPP);
		sc1.setUuid(uuid);
		sc1.setSavePassword(true);

		
		Account sc2 = new Account("username2", "password2", ProtocolType.XMPP);
		sc2.setUuid(uuid);
		sc2.setSavePassword(true);
		
		int origsize = this.accountDao.getAll().size();
		
		this.accountDao.create(sc1);
		Account entry = this.accountDao.update(sc2);
		
		Assert.assertEquals(sc2.getUserId(), entry.getUserId());
		Assert.assertEquals(sc2.getUuid(), entry.getUuid());
		// Assert.assertEquals(sc2, entry);
		Assert.assertEquals(sc2.getPlainTextPassword(), entry.getPlainTextPassword());
		Assert.assertFalse(sc1.equals(entry));
	}



	@Test
	@Transactional
	public final void testSilentReplaceCredentials() throws Exception {
		UUID uuid = UUID.randomUUID();

		int origsize = this.accountDao.getAll().size();
		Account sc1 = new Account("username", "password", ProtocolType.XMPP);
		sc1.setUuid(uuid);
		sc1.setSavePassword(true);
		this.accountDao.create(sc1);

		sc1 = this.accountDao.read(UUID.fromString(sc1.getUuid()));

		Account sc2 = new Account("username2", "password2", ProtocolType.XMPP);
		sc2.setUuid(UUID.fromString(uuid.toString()));
		sc2.setSavePassword(true);

		Account entry = this.accountDao.update(sc2);

		Assert.assertEquals(sc2.getUserId(), entry.getUserId());
		Assert.assertEquals(sc2.getUuid(), entry.getUuid());
		// Assert.assertEquals(sc2, entry);
		Assert.assertEquals(sc2.getPlainTextPassword(), entry.getPlainTextPassword());
		Assert.assertFalse(sc1.equals(entry));
	}
}
