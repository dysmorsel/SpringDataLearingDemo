package com.javase.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/24
 **/
public interface PersonRepository extends
        JpaRepository<Person,Integer>,JpaSpecificationExecutor<Person>,PersonDao {

    /**
     * 根据lastName找到相应的Person
     * @param lastName 实体类中的lastName属性
     * @return 返回对应的Person类
     */
    Person getByLastName(String lastName);

    /**
     *找出同时满足lastName以"XX"开头并且id小于"YY"时的所有Person
     * @param lastName 设定lastName需要以该字段开头
     * @param id 设定id需要小于该数字
     * @return 返回对应的集合
     */
    List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

    /**
     * 使用自定义条件查询
     * @return 返回id最大的Person
     */
    @Query("SELECT p FROM Person p WHERE p.id =(SELECT max (p2.id) FROM Person p2)")
    Person getMaxPerson();

    @Transactional
    @Modifying
    @Query("update Person p set p.lastName =:lastName where p.id =:id")
    void update(@Param("lastName") String lastName, @Param("id") Integer id);
}
