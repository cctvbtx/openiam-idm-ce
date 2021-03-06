/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.idm.srvc.role.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;

/**
 * Response object for a web service operation that returns a list of role objects
 * @author suneet
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoleAttrArrayResponse", propOrder = {
    "roleAttrAry"
})
public class RoleAttributeArrayResponse extends Response{
	
	RoleAttribute[] roleAttrAry;
	
	public RoleAttributeArrayResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleAttributeArrayResponse(ResponseStatus s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public RoleAttribute[] getRoleAttrAry() {
		return roleAttrAry;
	}

	public void setRoleAttrAry(RoleAttribute[] roleAttrAry) {
		this.roleAttrAry = roleAttrAry;
	}










	
	
}
