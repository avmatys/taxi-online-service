package by.bsuir.matys_rozin_zarudny.layer.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import by.bsuir.matys_rozin_zarudny.configuration.ConfigService;

/**
 * JpaUtils is a singleton class to provide useful methods to main entity factories.
 */
public class EntityManagerFactoryUtils {
    
    private static volatile EntityManagerFactory emf;
   
    private EntityManagerFactoryUtils(){
        // empty as utility close
    }
    
    /**
     * Return an instance of an application managed entity manager.
     * If the entity manager factory does not exist create a new instance.
     * 
     * @return an instance of an application managed entity manager.
     */
    public static EntityManager getEntityManager(){
        /* 
        create entity manager will throw an IllegalStateException 
        if the factory is closed and hence a chck is needed.
        */
        if(EntityManagerFactoryUtils.emf == null || !EntityManagerFactoryUtils.emf.isOpen()){
            synchronized(EntityManagerFactoryUtils.class){
                EntityManagerFactoryUtils.emf = Persistence.createEntityManagerFactory(
                        ConfigService.getConfig("application.properties")
                                .getProperty("taxi.server.db.persistent.unit")
                );
            }
        }
        return EntityManagerFactoryUtils.emf.createEntityManager();
    }
    
    /**
     * Close entity manager factory.
     * Entity manager factory needs to be closed for resources to be set free.
     * 
     * @return true if closed, false if already closed.
     */
    public static boolean close(){
      if(EntityManagerFactoryUtils.emf != null && EntityManagerFactoryUtils.emf.isOpen()){
          EntityManagerFactoryUtils.emf.close();
          return true;
      }else{
          return false;
      }
    }
}
