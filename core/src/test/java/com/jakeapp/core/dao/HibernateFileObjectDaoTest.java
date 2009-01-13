package com.jakeapp.core.dao;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import com.jakeapp.core.domain.FileObject;
import com.jakeapp.core.domain.Tag;
import com.jakeapp.core.domain.exceptions.InvalidTagNameException;
import com.jakeapp.core.dao.exceptions.NoSuchJakeObjectException;

import java.util.UUID;
import java.util.List;

@ContextConfiguration(locations = {"/com/jakeapp/core/dao/jake_core_test_hibernateLocal_context.xml"})
public class HibernateFileObjectDaoTest extends AbstractJUnit4SpringContextTests {
    private static Logger log = Logger.getLogger(HibernateFileObjectDaoTest.class);

    private IFileObjectDao fileObjecttDao;
    private HibernateTemplate template;

    public IJakeObjectDao getFileObjecttDao() {
        return fileObjecttDao;
    }

    public void setFileObjecttDao(IFileObjectDao fileObjecttDao) {
        this.fileObjecttDao = fileObjecttDao;
    }

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    @Before
    public void setUp() {
        // Add your code here
        this.setFileObjecttDao((IFileObjectDao) this.applicationContext.getBean("fileObjectDao"));
        this.setTemplate((HibernateTemplate) applicationContext.getBean("hibernateTemplate"));
        this.getTemplate().getSessionFactory().getCurrentSession().getTransaction().begin();
    }

    @After
    public void tearDown() {
        this.getTemplate().getSessionFactory().getCurrentSession().getTransaction().commit();

        /* rollback for true unit testing */
//        this.getTemplate().getSessionFactory().getCurrentSession().getTransaction().rollback();
    }


    @Transactional
    @Test
    public void testPersist() {
        FileObject obj1 = new FileObject(
                UUID.fromString("201fbce5-f910-4557-80b5-2be3eab2f0dd"),
                null, // project
                "/testPersist_test.txt");

        fileObjecttDao.persist(obj1);

    }

    @Transactional
    @Test
    public void testGet() throws NoSuchJakeObjectException {
        FileObject obj1 = new FileObject(
                UUID.fromString("201fbce5-f910-aaaa-80b5-2be3eab2f0dd"),
                null, // project
                "/testGet_test.txt");

        fileObjecttDao.persist(obj1);
        FileObject result;

        result = (FileObject) fileObjecttDao.get(obj1.getUuid());
        Assert.assertEquals(obj1, result);
    }

    @Transactional
    @Test
    public void testGetAll() {
        FileObject obj1 = new FileObject(
                UUID.fromString("efcc5d3e-f7f1-4475-beac-7b5bd3bc34a5"),
                null, // project
                "/testGetAll_test1.txt");
        FileObject obj2 = new FileObject(
                UUID.fromString("91091dc4-eb7a-4d20-b4ac-e53ed0e24fc1"),
                null, // project
                "/testGetAll_test2.txt");
        FileObject obj3 = new FileObject(
                UUID.fromString("91e5628f-28df-4bb2-a472-5a347608134c"),
                null, // project
                "/testGetAll_test3.txt");
        FileObject obj4 = new FileObject(
                UUID.fromString("9271a754-73f4-40c4-a46f-7ab5f02391aa"),
                null, // project
                "/testGetAll_test4.txt");
        FileObject obj5 = new FileObject(
                UUID.fromString("84aa1f69-9c1c-401c-93f6-7616910ece06"),
                null, // project
                "/testGetAll_test5.txt");


        fileObjecttDao.persist(obj1);
        fileObjecttDao.persist(obj2);
        fileObjecttDao.persist(obj3);
        fileObjecttDao.persist(obj4);
        fileObjecttDao.persist(obj5);

        List<FileObject> results = fileObjecttDao.getAll();

        Assert.assertTrue(results.contains(obj1));
        Assert.assertTrue(results.contains(obj2));
        Assert.assertTrue(results.contains(obj3));
        Assert.assertTrue(results.contains(obj4));
        Assert.assertTrue(results.contains(obj5));

    }

    @Transactional
    @Test
    public void testDelete() throws NoSuchJakeObjectException {
                FileObject obj1 = new FileObject(
                UUID.fromString("efcc5d3e-f7f1-4475-aaaa-7b5bd3bc34a5"),
                null, // project
                "/testDelete_test1.txt");

                fileObjecttDao.persist(obj1);
        List<FileObject> results;
        results = fileObjecttDao.getAll();

        Assert.assertTrue(results.contains(obj1));

        fileObjecttDao.delete(obj1);

        results.clear();

        results = fileObjecttDao.getAll();

        Assert.assertFalse(results.contains(obj1));


    }

    @Transactional
    @Test
    public void testAddTagTo() throws InvalidTagNameException, NoSuchJakeObjectException {

                        FileObject obj1 = new FileObject(
                UUID.fromString("efcc5d3e-f7f1-4475-bbbb-7b5bd3bc34a5"),
                null, // project
                "/testAddTagTo_test1.txt");

                fileObjecttDao.persist(obj1);

        Tag t1 = new Tag("testAddTagTo_tag1");

        fileObjecttDao.addTagTo(obj1, t1);


        List<Tag> tagResults = fileObjecttDao.getTagsFor(obj1);

        Assert.assertTrue(tagResults.contains(t1));
    }

    @Transactional
    @Test
    public void testRemoveTagFrom() {
        // Add your code here
    }

    @Transactional
    @Test
    public void testAddTagsTo() {
        // Add your code here
    }

    @Transactional
    @Test
    public void testGetTagsFor() {
        // Add your code here
    }

    @Transactional
    @Test
    public void testRemoveTagsFrom() {
        // Add your code here
    }
}