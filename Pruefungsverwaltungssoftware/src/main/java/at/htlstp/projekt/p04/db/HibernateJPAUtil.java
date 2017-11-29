/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.db; 

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Dru
 */
public class HibernateJPAUtil {

    private static final EntityManagerFactory em; 
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
           em = Persistence.createEntityManagerFactory("VMM_nost_pu"); 
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial EntityManager creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        return em;
    }
    
    public static void close(){
        em.close();       
    }
}
