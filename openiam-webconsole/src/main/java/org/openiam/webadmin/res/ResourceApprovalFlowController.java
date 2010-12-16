package org.openiam.webadmin.res;

/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the Lesser GNU General Public License 
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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;

import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyConstants;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.dto.User;


/**
 * Controller to manage connectivity information for a managed system
 * @author suneet
 *
 */
public class ResourceApprovalFlowController extends SimpleFormController {


	private static final Log log = LogFactory.getLog(ResourceApprovalFlowController.class);

	protected ResourceDataService resourceDataService;
	protected NavigatorDataWebService navigationDataService;
	private PolicyDataService policyDataService;
	private String redirectView;
	private ManagedSystemDataService managedSysService;
	private UserDataWebService userMgr;
	

	public ResourceApprovalFlowController() {
		super();
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true) );
				
	}
	

	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		String mode = request.getParameter("mode");
		if (mode != null && mode.equalsIgnoreCase("1")) {
			request.setAttribute("msg", "Information has been successfully updated.");
		}
		
		HttpSession session =  request.getSession();
		String userId = (String)session.getAttribute("userId");
		
		String resId = request.getParameter("objId");
		log.info("ReourceId using objId param = " + resId);
		if (resId != null && resId.length() > 0) {
			request.setAttribute("objId",resId);
		}else {
			resId = request.getParameter("resId");
			request.setAttribute("objId",resId);
		}
		log.info("Loading resourceId = " + resId);
		
		String menuGrp = request.getParameter("menugrp");
		
		Resource res = resourceDataService.getResource(resId);
		
		List<ApproverAssociation> assocList  = managedSysService.getAllApproversByRequestType(res.getResourceId());			
		if (assocList == null) {
			assocList = new ArrayList<ApproverAssociation>();
		}

		log.info("Assoc List:" + assocList.size());
		ApproverAssociation newAssoc = new ApproverAssociation();
		newAssoc.setApproverAssocId("NEW");
		assocList.add(newAssoc);

		// add the user names in the approval flow
		if (assocList != null) {
			for (ApproverAssociation assoc : assocList) {
				if (assoc.getApproverUserId() != null && assoc.getApproverUserId().length() > 0) {
					User usr = this.userMgr.getUserWithDependent(assoc.getApproverUserId(), false).getUser();
					assoc.setApproverName(usr.getFirstName() + " " + usr.getLastName());
				}
			}
		}
		
		List<Menu> level3MenuList =  navigationDataService.menuGroupByUser(menuGrp, userId, "en").getMenuList();
		request.setAttribute("menuL3", level3MenuList);			

		ResourceApprovalFlowCommand cmd = new ResourceApprovalFlowCommand();
		
		
		cmd.setApproverAssoc(assocList);
		cmd.setResourceName(res.getName());
		cmd.setResId(resId);
		cmd.setManagedSysId(res.getManagedSysId());

		return cmd;
	}



	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.info("onSubmit called");
		
		ResourceApprovalFlowCommand assocCmd = (ResourceApprovalFlowCommand)command;
		String resId = assocCmd.getResId();		
	

		
		String btn = request.getParameter("btn");
		if (btn.equalsIgnoreCase("Delete")) {
			List<ApproverAssociation> assocList =  assocCmd.getApproverAssoc();
			if (assocList != null) {
				
				for ( ApproverAssociation a : assocList) {
					if (a.getSelected()) {
						String assocId = a.getApproverAssocId();
						if (!assocId.equalsIgnoreCase("NEW")) {
							managedSysService.removeApproverAssociation(assocId);
						}
					}
				}
				
			}
		}else {
		

			List<ApproverAssociation> assocList =  assocCmd.getApproverAssoc();
			
			if (assocList != null) {
			
				for ( ApproverAssociation a : assocList) {
					if (a.getApproverAssocId() == null || a.getApproverAssocId().equalsIgnoreCase("NEW")) {
						// new 
						if (a.getAssociationType() != null && a.getAssociationType().length() > 0) {
							a.setApproverAssocId(null);
							a.setRequestType(resId);
							managedSysService.addApproverAssociation(a);
						}
					}else {
						// update
						if (a.getAssociationType() != null && a.getAssociationType().length() > 0) {
							managedSysService.updateApproverAssociation(a);
						}
					}
				}
				
			}
		}
		log.info("refreshing attr list for resourceId=" + resId);
		String view = redirectView + "&menuid=RESAPPROVER&menugrp=SECURITY_RES&objId=" +resId;
		log.info("redirecting to=" + view);
	
		return new ModelAndView(new RedirectView(view, true));

		
	}


	public ResourceDataService getResourceDataService() {
		return resourceDataService;
	}

	public void setResourceDataService(ResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}

	public NavigatorDataWebService getNavigationDataService() {
		return navigationDataService;
	}

	public void setNavigationDataService(
			NavigatorDataWebService navigationDataService) {
		this.navigationDataService = navigationDataService;
	}

	public PolicyDataService getPolicyDataService() {
		return policyDataService;
	}

	public void setPolicyDataService(PolicyDataService policyDataService) {
		this.policyDataService = policyDataService;
	}

	public String getRedirectView() {
		return redirectView;
	}

	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}

	public ManagedSystemDataService getManagedSysService() {
		return managedSysService;
	}

	public void setManagedSysService(ManagedSystemDataService managedSysService) {
		this.managedSysService = managedSysService;
	}

	public UserDataWebService getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserDataWebService userMgr) {
		this.userMgr = userMgr;
	}


	

}