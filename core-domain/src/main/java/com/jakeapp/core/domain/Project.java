package com.jakeapp.core.domain;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

/**
 * The <code>Project</code> Entity holds general information about a project in Jake.
 * It is also used to find the correct data for a project in all DAOs.
 * A project belongs to exactly one <code>User</code> but has multiple members.
 * <p/>
 * It consists of <ul>
 * <li>a <code>rootPath</code> to specify where the root folder of the project
 * lies on the local filesystem</li>
 * <li>a <code>name</code> specifying the name of the project (the user can change this)</li>
 * <li>a <code>projectId</code> used to internally (and cross-instance) identify the <code>Project</code> </li>
 * <li>a boolean <code>isStarted</code>, specifying if the project is currently running</li>
 * <li>a boolean <code>isAutoAnnounceEnabled</code>, specifying if automatical announcement of changes is enabled</li>
 * <li>a boolean <code>isAutoPullEnabled</code>, specifying if automatical
 * pull of new changes (files) is enabled</li>
 * <li>a <code>User</code>-Object to which the project is bound. This object also specifies on which instant
 * messaging network this project operates</li>
 * </ul>
 */
@Entity
@Table(name = "project")
//@UniqueConstraint(columnNames = {"projectId"} )
public class Project implements ILogable {
	private static final long serialVersionUID = 4634971877310089896L;
	
	private String name;
	private UUID projectId;
	private File rootPath;
	private Account credentials;

	private transient IMsgService messageService;
	
	private transient boolean started;
	private transient boolean open;
	private transient boolean autoAnnounceEnabled = true;
	private transient boolean autoPullEnabled = true;
	private transient boolean autologin;
	private transient InvitationState invitationState = InvitationState.ACCEPTED;

	/**
	 * Construct a Project. Freshly constructed projects are always stopped.
	 *
	 * @param name			 the name of the project
	 * @param projectId	the unique projectId
	 * @param msgService the <code>MsgService</code> to be used
	 * @param rootPath	 the root path of the project, i.e.
	 *                   the  path of the project folder.
	 */
	public Project(String name, UUID projectId, IMsgService msgService, File rootPath) {
		this.setName(name);
		this.setProjectId(projectId);
		this.setRootPath(rootPath);
		this.setCredentials(null);
		this.setMessageService(msgService);
	}


	public void setProjectId(UUID projectId) {
		this.projectId = projectId;
	}

	public void setProjectId(String projectId) {
		setProjectId(UUID.fromString(projectId));
	}

	/**
	 * A public constructor with no arguments is needed for hibernate.
	 */
	public Project() {
	}

	/**
	 * Get the project id.
	 *
	 * @return the unique <code>projectId</code> of the project
	 */
	@Id
	@Column(name = "UUID", nullable = false, unique = true)
	public String getProjectId() {
		return (this.projectId == null) ? null : this.projectId.toString();
	}

	/**
	 * Get the name of the <code>Project</code>.
	 *
	 * @return the name of the Project
	 */
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the <code>MsgService</code> of this Project.
	 * If you manually create a <code>Project</code>, this is not initialized and
	 * therefor may be null!
	 *
	 * @return the message service of the <code>Project</code>, may be null.
	 */
	@Column(name = "PROTOCOL", nullable = false)
	@Transient
	public IMsgService getMessageService() {
		return this.messageService;
	}

	/**
	 * Only set message service internal.
	 * This is directly connected with UserId, so update it when this is changed.
	 *
	 * @param messageService	the MsgService connected with the project.
	 */
	public void setMessageService(IMsgService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Get the root path.
	 *
	 * @return the root path of the project, i.e. the path of the project folder
	 */
	@Column(name = "ROOTPATH", nullable = true)
	public String getRootPath() {
		return this.rootPath != null ? this.rootPath.toString() : null;
	}

	public void setRootPath(final String rootPath) {
		if(rootPath != null && !rootPath.isEmpty())
			this.rootPath = new File(rootPath);
		else
			this.rootPath = null;
	}

	public void setRootPath(final File rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * Returns <code>true</code> iff the project is started.
	 *
	 * @return <code>true</code> iff the project is started
	 */
	@Column(name = "STARTED", nullable = false)
	public boolean isStarted() {
		return this.started;
	}

	/**
	 * Set the <code>started</code> flag.
	 *
	 * @param started The new value for started -
	 *                set this whenever you start or stop the proejct.
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Returns <code>true</code> iff the <code>autoAnnounce</code>
	 * feature is enabled.
	 *
	 * @return <code>true</code> iff the <code>autoAnnounce</code>
	 *         feature is enabled
	 */
	@Column(name = "AUTOANNOUNCE", nullable = false)
	public boolean isAutoAnnounceEnabled() {
		return this.autoAnnounceEnabled;
	}

	/**
	 * Set the <code>autoAnnounceEnabled</code> flag.
	 *
	 * @param enabled
	 */
	public void setAutoAnnounceEnabled(boolean enabled) {
		this.autoAnnounceEnabled = enabled;
	}

	/**
	 * Returns <code>true</code> iff the <code>autoPull</code>
	 * feature is enabled.
	 *
	 * @return <code>true</code> iff the <code>autoPull</code>
	 *         feature is enabled
	 */
	@Column(name = "AUTOPULL", nullable = false)
	public boolean isAutoPullEnabled() {
		return this.autoPullEnabled;
	}

	/**
	 * Set the <code>autoPullEnabled</code> flag.
	 *
	 * @param enabled
	 */
	public void setAutoPullEnabled(boolean enabled) {
		this.autoPullEnabled = enabled;
	}

	/**
	 * Get the userId.
	 *
	 * @return the userId that is associated with the project.
	 */
	@Transient
	public User getUserId() {
		return (this.getMessageService()==null)?
					null:
					this.getMessageService().getUserId();
	}

	/**
	 * AutoLogin determines, if the project should automatically
	 * try to login the user if
	 * the project is started and the user credentials are given.
	 *
	 * @return true, if the project automatically logs in the associated user
	 */
	@Column(name = "AUTOLOGIN", nullable = false)
	public boolean isAutologinEnabled() {
		return this.autologin;
	}

	/**
	 * @param newState a boolean indicating if the <code>Project</code> is opended or not.
	 */
	public void setOpen(boolean newState) {
		this.open = newState;
	}


	/**
	 * @return true iff the <code>Project</code> is open
	 */
	@Column(name = "OPENED", nullable = false)
	public boolean isOpen() {
		return open;
	}


	public void setAutologinEnabled(boolean enabled) {
		this.autologin = enabled;
	}


	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Account credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the credentials
	 */
	//@Column(name="USERID", nullable = false)
	// Do not set to lazy, otherwise it is loaded in the wrong thread.
	@ManyToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "userid", nullable = true)
	public Account getCredentials() {
		return credentials;
	}

	/**
	 * @param invitationState the invitationState to set
	 */
	@Deprecated
	public void setInvitationState(InvitationState invitationState) {
		this.invitationState = invitationState;
	}


	/**
	 * @return the invitationState
	 * @deprecated
	 */
	//    @Transient // TODO change here to save invitation state
	@Column(name = "invitationstate")
	@Deprecated
	public InvitationState getInvitationState() {
		return invitationState;
	}

	/**
	 * Convenicence Methode for getInvitationState.
	 *
	 * @return true if project is invited only.
	 * @deprecated
	 */
	@Transient
	@Deprecated
	public boolean isInvitation() {
		return getInvitationState() == InvitationState.INVITED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Project " + getName() + "(" + getProjectId() + "), path: \"" + ((getRootPath() != null)? getRootPath() : "null") + "\" user: " + getUserId() + " started: " + isStarted() + " state: " + getInvitationState();
	}

	/**
	 * Tests if to <code>Projects</code> are equal.
	 *
	 * @param obj The <code>Object</code> to compare this object to.
	 * @return <code>true</code> iff the <code>name, projectId </code> and
	 *         <code> rootPath</code> are equal.
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Project other = (Project) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.projectId == null) {
			if (other.projectId != null) {
				return false;
			}
		} else if (!this.projectId.equals(other.projectId)) {
			return false;
		}
		if (this.rootPath == null) {
			if (other.rootPath != null) {
				return false;
			}
		} else if (!this.rootPath.equals(other.rootPath)) {
			return false;
		}
		return true;
	}

	/**
	 * Generate the hash code using <code>name, projectId, rootPath</code>.
	 *
	 * @return hashCode
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.projectId == null) ? 0 : this.projectId
						.hashCode());
		result = prime * result + ((this.rootPath == null) ? 0 : this.rootPath
						.hashCode());
		return result;
	}
}
