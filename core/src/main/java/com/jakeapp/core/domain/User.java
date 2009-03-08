package com.jakeapp.core.domain;
import org.apache.log4j.Logger;


/**
 * Identifies a user
 */
public class User implements ILogable, Comparable<User> {
	private static final long serialVersionUID = 3356457614479149943L;

	public User() {
		super();
	}

	public User(ProtocolType protocolType, String userId) {
		super();
		this.protocolType = protocolType;
		this.userId = userId;
	}

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(User.class);

	private String userId;
	private ProtocolType protocolType;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ProtocolType getProtocolType() {
		return this.protocolType;
	}
	
	@Override
	public String toString() {
		return this.protocolType + ":" + getUserId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((protocolType == null) ? 0 : protocolType.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (protocolType == null) {
			if (other.protocolType != null)
				return false;
		} else if (!protocolType.equals(other.protocolType))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int compareTo(User o) {
		if (this.equals(o)) return 0;
		else if (o==null) return 1;
		else return this.getUserId().compareTo(o.getUserId());
	}
}