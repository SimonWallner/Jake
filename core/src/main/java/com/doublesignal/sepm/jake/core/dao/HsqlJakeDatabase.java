package com.doublesignal.sepm.jake.core.dao;

import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Implements IJakeDatabase for HSQL-Database-files.
 * @author johannes
 */
public class HsqlJakeDatabase implements IJakeDatabase {
	
	JdbcProjectMemberDao projectMemberDao;
	JdbcJakeObjectDao jakeObjectDao;
	JdbcLogEntryDao logEntryDao;
	JdbcConfigurationDao configurationDao;

	public void connect(String database) throws SQLException{
		close();
		if(database.endsWith(".properties") || database.endsWith(".script"))
			throw new SQLException("File ends with suffix!");
		System.err.println("HSQL-CONNECT: " + "jdbc:hsqldb:file:"+database+";ifexists=true;shutdown=false");
		DriverManagerDataSource ds = new DriverManagerDataSource(
			"org.hsqldb.jdbcDriver", 
			"jdbc:hsqldb:"+database+";shutdown=false",
			"sa",
			""
		);
		configurationDao.setDataSource(ds);
		jakeObjectDao.setDataSource(ds);
		projectMemberDao.setDataSource(ds);
		logEntryDao.setDataSource(ds);
	}
	public void close() throws SQLException{
		// close the hsql db
		Statement stmt = configurationDao.getDataSource().getConnection().createStatement();
		stmt.executeUpdate("SHUTDOWN");
		
		configurationDao.getDataSource().getConnection().close();
		jakeObjectDao.getDataSource().getConnection().close();
		projectMemberDao.getDataSource().getConnection().close();
		logEntryDao.getDataSource().getConnection().close();
	}
	
	public IConfigurationDao getConfigurationDao() {
		return configurationDao;
	}
	public void setConfigurationDao(IConfigurationDao configurationDao) {
		this.configurationDao = (JdbcConfigurationDao) configurationDao;
	}
	public IJakeObjectDao getJakeObjectDao() {
		return jakeObjectDao;
	}
	public void setJakeObjectDao(IJakeObjectDao jakeObjectDao) {
		this.jakeObjectDao = (JdbcJakeObjectDao) jakeObjectDao;
	}
	public ILogEntryDao getLogEntryDao() {
		return logEntryDao;
	}
	public void setLogEntryDao(ILogEntryDao logEntryDao) {
		this.logEntryDao = (JdbcLogEntryDao) logEntryDao;
	}
	public IProjectMemberDao getProjectMemberDao() {
		return projectMemberDao;
	}
	public void setProjectMemberDao(IProjectMemberDao projectMemberDao) {
		this.projectMemberDao = (JdbcProjectMemberDao) projectMemberDao;
	}
	

}
