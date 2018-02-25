package com.javase.spring.data.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author fzm
 * @date 2018/2/25
 **/
public class PersonRepositoryImpl implements PersonDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void find() {
        Person person = entityManager.find(Person.class,12);
        System.out.println("--->" + person.toString());
    }
}
