package com.javase.spring.data.jpa.service;

import com.javase.spring.data.jpa.Indent;
import com.javase.spring.data.jpa.IndentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/24
 **/
@Service
public class IndentService {

    @Autowired
    private IndentRepository indentRepository;

    @Transactional
    public void saveIndents(List<Indent> indents){
        indentRepository.saveAll(indents);
    }
}
