package com.jakeapp.core.services;

import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.User;


/**
 * Interface for handling <code>Invitation</code>s. Implementations of
 * <code>IProjectInvitationListener</code> can be added to <code>IInvitationHandler</code>s.
 * When an <code>Invitation</code> arrives, the methods get called with the appropriate parameters.
 */
public interface IProjectInvitationListener {

	/**
	 * You have been invited by user to the Project p
	 * We store the project p in the local database as state invited
	 * @param inviter The <code>User</code> who invited us to the <code>Project</code>
	 * @param project The <code>Project</code> we've been invited to.
	 */
	public void invited(User inviter, Project project);

	/**
	 * the user has accepted the invitation
	 * we create a logentry that the user is now in the project
	 * @param invitee The <code>User</code> that accepted our <code>Invitation</code>
	 * @param project The <code>Project</code> belonging to the <code>Invitation</code>
	 */
	public void accepted(User invitee, Project project);

	/**
	 * the user has rejected the invitation
	 * we create a logentry that the user rejected our request.
	 * @param invitee The <code>User</code> that rejected our <code>Invitation</code>
	 * @param project The <code>Project</code> we wanted the <code>User</code> to join.
	 */
	public void rejected(User invitee, Project project);

}
