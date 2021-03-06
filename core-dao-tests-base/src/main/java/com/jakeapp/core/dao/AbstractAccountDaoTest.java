package com.jakeapp.core.dao;

import com.jakeapp.core.dao.exceptions.NoSuchServiceCredentialsException;
import com.jakeapp.core.domain.Account;
import com.jakeapp.core.domain.ProtocolType;
import com.jakeapp.core.domain.exceptions.InvalidCredentialsException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.UUID;


@ContextConfiguration
public abstract class AbstractAccountDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger log = Logger.getLogger(AbstractAccountDaoTest.class);


	@Autowired
	private  IAccountDao accountDao;

	public IAccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(IAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	Account validCredentials;



    @Before
    public void setUp() throws UnknownHostException, InvalidCredentialsException {

        validCredentials = new Account();
        validCredentials.setUuid("6c8b815b-c50c-4b49-a74a-3eefe9fa2977");
        validCredentials.setUserId("domdorn@jabber.fsinf.at");
        validCredentials.setPlainTextPassword("somePassword");
        validCredentials.setEncryptionUsed(true);

//        InetAddress addr = InetAddress.getLocalHost();
        validCredentials.setServerAddress("localhost");
//        validCredentials.setServerAddress(addr);
        validCredentials.setServerPort(5222);

        validCredentials.setProtocol(ProtocolType.XMPP);
    }

    @After
    public void tearDown() {
    }

    /**
     * This test tries to persist a null value which should not work.
     * @throws com.jakeapp.core.domain.exceptions.InvalidCredentialsException
     */
    @Test(expected = InvalidCredentialsException.class)
    @Transactional
    public final void create_shouldFailPersistNull() throws InvalidCredentialsException {
        accountDao.create(null);
    }


    /**
     * This test tries to persist empty service credentials, which should not work.
     * @throws com.jakeapp.core.domain.exceptions.InvalidCredentialsException
     */
    @Test(expected = InvalidCredentialsException.class)
    @Transactional
    public final void create_persistEmptyCredentials() throws InvalidCredentialsException {
        Account credentials = new Account();

        accountDao.create(credentials);
    }


    /**
     * Basic test that simply tries to create valid ServiceCredentials, to find out if this
     * is the problem when other tests fail.
     *
     * @throws InvalidCredentialsException
     * @throws UnknownHostException
     */
    @Test
    @Transactional
    public final void basicSetCredentialsTest() throws InvalidCredentialsException, UnknownHostException {
        Account credentials = new Account();
        //credentials.setUuid("6c8b815b-c50c-4b49-a74a-3eefe9fa2977");
        credentials.setUuid("57e81674-03a1-4422-b05e-c9c9b6eeeb2a");
        credentials.setUserId("domdorn@jabber.fsinf.at");
        credentials.setPlainTextPassword("somePassword");
        credentials.setEncryptionUsed(true);
        credentials.setServerAddress("localhost");
//        credentials.setServerAddress(InetAddress.getLocalHost());
        credentials.setServerPort(5222);

        log.info("basicSetCredentialsTest succeeded.");
    }

    /**
     * Simple test to persist valid credentials
     *
     * @throws InvalidCredentialsException
     * @throws UnknownHostException
     */
    @Test
    @Transactional
    public final void create_persistBasicCredentialsTest() throws InvalidCredentialsException, UnknownHostException {


        accountDao.create(validCredentials);
    }

    @Test
    @Transactional
    public final void createRead_testWithPasswordSaving() throws InvalidCredentialsException, NoSuchServiceCredentialsException {
        Account result;
        validCredentials.setUuid("9c16a0d1-5ee1-4df9-9a3c-f5e4b5dcc0b3");
        validCredentials.setSavePassword(true);
        accountDao.create(validCredentials);
        result = accountDao.read(UUID.fromString(validCredentials.getUuid()));

        assertEquals(validCredentials, result);
    }

    @Test
    @Transactional
    public final void createRead_testNoPasswordSaving() throws InvalidCredentialsException, NoSuchServiceCredentialsException {
        Account accountCreated;
		Account accountRead;

        validCredentials.setUuid("9c16a0d1-5ee1-4df9-9a3c-f5e4b5dcc0b4");
		validCredentials.setSavePassword(false);
		accountCreated = accountDao.create(validCredentials);
		assertEquals("Password may not be lost on persisting",
				validCredentials.getPlainTextPassword(), accountCreated.getPlainTextPassword());

		accountRead = accountDao.read(UUID.fromString(validCredentials.getUuid()));
		assertFalse("Password may not be set when retrieving Account", accountCreated.getPlainTextPassword().equals(accountRead.getPlainTextPassword()));
   }


}
