package com.javase.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/24
 **/
public interface PersonRepository extends PagingAndSortingRepository<Person,Integer>,JpaSpecificationExecutor<Person> {

    Person getByLastName(String lastName);

    List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

    @Query("SELECT p FROM Person p WHERE p.id =(SELECT max (p2.id) FROM Person p2)")
    Person getMaxPerson();


}
