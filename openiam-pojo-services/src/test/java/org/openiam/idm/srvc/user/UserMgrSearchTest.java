package org.openiam.idm.srvc.user;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.*;

import org.openiam.idm.srvc.user.service.*;
import org.openiam.idm.srvc.user.dto.*;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.util.db.*;
import org.openiam.idm.srvc.user.dto.UserSearchField;
import org.openiam.util.db.Search;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class UserMgrSearchTest extends AbstractDependencyInjectionSpringContextTests  {

	ApplicationContext ctx = null;

	UserDataService userMgr;
	User user, user10;

	static String userId;

	
	@Override

	protected void onSetUp() throws Exception {
		// TODO Auto-generated method stub
		super.onSetUp();
		ctx = new ClassPathXmlApplicationContext(
					new String[] {"/applicationContext.xml",
								  "/userTest-applicationContext.xml"} ) ;
		userMgr = (UserDataService)ctx.getBean("userManager");
		user = (User)ctx.getBean("userBean");
		user10 = (User)ctx.getBean("userBean10");
	
		
	} 



	public void testUserSearch_lastName() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.like(UserSearchField.LastName, "s%");
		search.addSearchCriteria(qc);
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(4, results.size());
		
		// iterate through list to ensure fetching.
		
		int size = results.size();
		for (int i = 0; i < size; i++) {
			User usr = (User) results.get(i);
			System.out.println ( usr.getFirstName() + " " + usr.getLastName() );
		 	Set emailSet = usr.getEmailAddress();
		 	Iterator<EmailAddress> it = emailSet.iterator();
		 	while (it.hasNext()) {
		 		EmailAddress em = it.next();
		 		System.out.println( "- " + em.getEmailAddress());
		 	}
		}
		
	}

	public void testUserSearch_firstName() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.like(UserSearchField.FirstName, "s%");
		search.addSearchCriteria(qc);
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(4, results.size());
		
	}

	public void testUserSearch_firstAndLastName() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.like(UserSearchField.FirstName, "A%");
		search.addSearchCriteria(qc);
		
		QueryCriteria qc2 = new QueryCriteria();
		qc2.like(UserSearchField.LastName, "S%");
		search.addSearchCriteria(qc2);
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(2, results.size());
		
	}


	public void testUserSearch_Status() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.eq(UserSearchField.Status, "PENDING");
		search.addSearchCriteria(qc);
		
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(17, results.size());
		
	}

	public void testUserSearch_Dept() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.eq(UserSearchField.Department, "003");
		search.addSearchCriteria(qc);
		
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(2, results.size());
		
	}

	public void testUserSearch_MiddleInit() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.eq(UserSearchField.MiddleInitial, "M");
		search.addSearchCriteria(qc);
		
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(4, results.size());
		
	}

	public void testUserSearch_Phone() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.like(UserSearchField.PhoneNumber, "%714%");
		search.addSearchCriteria(qc);
		
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(8, results.size());
		
	}
	
	public void testUserSearch_Email() {
		
		QueryCriteria qc = new QueryCriteria();
		
		Search search = new SearchImpl();
		qc.like(UserSearchField.EmailAddress, "aan%");
		search.addSearchCriteria(qc);
		
		
		List results = userMgr.search(search);
		assertNotNull(results);
		this.assertEquals(5, results.size());
		
	}
	
}







