package com.javase.spring.data.jpa;

import javax.persistence.*;
import java.util.Date;

/**
 * @author fzm
 * @date 2018/2/23
 **/
@Table(name = "JPA_PERSONS")
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "LAST_NAME")
    private String lastName;
    private String email;
    private int age;
    @Temporal(TemporalType.DATE)
    private Date birth;

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"lastName\":\"")
                .append(lastName).append('\"');
        sb.append(",\"email\":\"")
                .append(email).append('\"');
        sb.append(",\"age\":")
                .append(age);
        sb.append(",\"birth\":\"")
                .append(birth).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
