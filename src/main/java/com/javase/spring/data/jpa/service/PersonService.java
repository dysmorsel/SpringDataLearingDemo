package com.javase.spring.data.jpa.service;

import com.javase.spring.data.jpa.Person;
import com.javase.spring.data.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/24
 **/
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public void savePersons(List<Person> people){
        personRepository.saveAll(people);
    }
}
