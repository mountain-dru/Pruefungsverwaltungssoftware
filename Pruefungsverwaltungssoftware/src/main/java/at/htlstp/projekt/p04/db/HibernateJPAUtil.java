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
            //JPA-konforme Implementierung 
           em = Persistence.createEntityManagerFactory("VMM_nost_pu");      //Name der Persistenzeinheit
        } catch (Throwable ex) {        //Alle Fehler erkennen und
            // Exception loggen  
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
