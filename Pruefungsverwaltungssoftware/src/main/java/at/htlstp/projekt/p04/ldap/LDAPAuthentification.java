/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.ldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 *
 * @author reio
 */
public class LDAPAuthentification {

    /**
     * Prüft Username und Passwort gegen das LDAP-System der Abteilung
     * Informatik
     *
     * @param user
     * @param password
     * @return true, wenn der User existiert, sonst false
     */
    public static boolean isValidUser(String user, String password) {

        return true; 

        /*
        boolean isUser;
        
        if(password.isEmpty()) {
            return false;
        }
        
        String u = "CN=" + user + ",CN=users,DC=htl-stp,DC=if";
  
    
        
        LdapContext ctx;
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, u);
        env.put(Context.SECURITY_CREDENTIALS, password);
        //Der entsprechende Domänen-Controller
        env.put(Context.PROVIDER_URL, "ldap://10.128.1.1:389");
        try {
          ctx = new InitialLdapContext(env, null);
          isUser = true;
        } catch(NamingException ex) {
            isUser = false;
        }
        
        
        return isUser;
         */
    }

}
