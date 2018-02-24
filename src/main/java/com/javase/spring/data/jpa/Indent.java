package com.javase.spring.data.jpa;

import javax.persistence.*;

/**
 * @author fzm
 * @date 2018/2/24
 **/
@Table(name = "JPA_INDENTS")
@Entity
public class Indent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "INDENT_NAME")
    private String indentName;

    @JoinColumn(name = "PERSON_ID")
    @ManyToOne(targetEntity = Person.class)
    private Person person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndentName() {
        return indentName;
    }

    public void setIndentName(String indentName) {
        this.indentName = indentName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
