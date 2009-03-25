package org.openiam.idm.connector.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.connector.ConnectionMgr;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

import java.util.*;

/**
 * Manages connections to LDAP
 * @author Suneet Shah
 *
 */
public class LdapConnectionMgr implements ConnectionMgr {

	private String keystore;
	LdapContext ctxLdap = null;
	
	 static protected ResourceBundle res = ResourceBundle.getBundle("ldapconf");
	 static protected ResourceBundle secres = ResourceBundle.getBundle("securityconf");
	
    private static final Log log = LogFactory.getLog(LdapConnectionMgr.class);

    public LdapConnectionMgr() {
    	keystore = secres.getString("KEYSTORE");
    }
    


	public LdapContext connect() {

		//LdapContext ctxLdap = null;
		Hashtable envDC = new Hashtable();
	
		keystore = secres.getString("KEYSTORE");
		//String keystore = "C:\\devtool\\Java\\jdk1.6.0_06\\jre\\lib\\security\\cacerts";
		System.setProperty("javax.net.ssl.trustStore",keystore);
		
		
		String urlLdap = res.getString("hostname");
		String adminName = res.getString("accountid");
		String password = res.getString("accountpassword");
		
		System.out.println("**accountid = " + adminName);

		
	//	ocesam1.ocfl.net
		
		envDC.put(Context.PROVIDER_URL,urlLdap);
		envDC.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");		
		envDC.put(Context.SECURITY_AUTHENTICATION, "simple" ); // simple
		envDC.put(Context.SECURITY_PRINCIPAL,adminName);  //"administrator@diamelle.local"
		envDC.put(Context.SECURITY_CREDENTIALS,password);
		
		
		envDC.put(Context.SECURITY_PROTOCOL,"ssl");
		try {
			System.out.println("Pre-context ....");
			ctxLdap = new InitialLdapContext(envDC,null);
			System.out.println("Directory context = " + ctxLdap);
			return ctxLdap;
		}catch(NamingException ne) {
			log.error(ne.getMessage(), ne);
			ne.printStackTrace();
		}

		
		return null;
	}

	public void close() {

		if (this.ctxLdap != null) { 
			try {
			ctxLdap.close();
			}catch(NamingException ne ) {
				log.error(ne.getMessage(), ne);
				ne.printStackTrace();
			}
		}
		ctxLdap = null;
		
	}

}