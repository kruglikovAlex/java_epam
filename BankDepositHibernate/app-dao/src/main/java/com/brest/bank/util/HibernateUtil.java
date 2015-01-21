package com.brest.bank.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class HibernateUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try{
            //-- создание SessionFactory из hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory(
                    new StandardServiceRegistryBuilder().build());
        } catch (Throwable ex){
            LOGGER.error("Initial SessionFactory creation ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
