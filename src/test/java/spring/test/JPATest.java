package spring.test;

import com.javase.spring.data.jpa.Indent;
import com.javase.spring.data.jpa.Person;
import com.javase.spring.data.jpa.PersonRepository;
import com.javase.spring.data.jpa.service.IndentService;
import com.javase.spring.data.jpa.service.PersonService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.testng.annotations.Test;

import javax.persistence.criteria.*;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fzm
 * @date 2018/2/23
 **/
public class JPATest {
    private ApplicationContext ctx = null;
    private PersonRepository personRepository;
    private PersonService personService;
    private IndentService indentService;
    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        personRepository = ctx.getBean(PersonRepository.class);
        personService = ctx.getBean(PersonService.class);
        indentService = ctx.getBean(IndentService.class);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    /**
     * 测试自定义方法
     */
    @Test
    public void testFind(){
        personRepository.find();
    }

    /**
     * 测试Modifying和自定义update
     */
    @Test
    public void testModifying(){
        personRepository.update("aaa",1);
    }

    /**
     * 测试简单查询
     */
    @Test
    public void testGet(){


        List persons = personRepository.getByLastNameStartingWithAndIdLessThan("J",10);
        Person person = personRepository.getMaxPerson();
        System.out.println(persons);
        System.out.println(person);
    }

    /**
     * 测试CrudRepository中的saveAll()方法
     */
    @Test
    public void testCrudRepository(){

        List<Person> people = new ArrayList<Person>();

        for (int i = 'a'; i <= 'z'; i ++){
            Person person = new Person();
            person.setBirth(new Date());
            person.setEmail((char)i+""+(char)i+"@163.com");
            person.setLastName((char)i+""+(char)i);
            person.setAge(i);
            people.add(person);
        }
        personService.savePersons(people);

    }

    @Test
    public void testIndentCrud(){
        List<Person> people = personRepository.findAll();
        List<Indent> indents = new ArrayList<Indent>();
        for (Person aPeople : people) {
            Indent indent = new Indent();
            indent.setIndentName(aPeople.getLastName());
            indent.setPerson(aPeople);
            indents.add(indent);
        }
        indentService.saveIndents(indents);
    }

    /**
     * 测试PagingAndSortingRepository中的查询结果分页
     */
    @Test
    public void testPagingAndSortingRepository(){
        //页面编号
        int pageNo = 3;
        //页面大小
        int pageSize = 5;

        //使用Order类选择排序测略，Direction包含两个枚举，即升序和降序，指定哪一个属性使用排序测量。
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC,"id");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC,"email");
        Sort sort = Sort.by(order1,order2);

        //使用PageRequest类传入分页和排序的策略，然后传入到findAll方法。得到需要的Page
        PageRequest pageRequest = PageRequest.of(pageNo,pageSize,sort);
        Page<Person> page =  personRepository.findAll(pageRequest);


        System.out.println("总记录数: "+ page.getTotalElements());
        System.out.println("当前第几页："+ page.getNumber());
        System.out.println("总页数："+ page.getTotalPages());
        System.out.println("当前页面List："+ page.getContent());
        System.out.println("当前页面记录数："+ page.getNumberOfElements());


    }

    /**
     * 测试JpaSpecificationExecutor
     */
    @Test
    public void testJpaSpecificationExecutor(){
        int pageNo = 3;
        int pageSize = 5;

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC,"id");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC,"email");
        Sort sort = Sort.by(order1,order2);

        PageRequest pageRequest = PageRequest.of(pageNo,pageSize,sort);
        Specification<Person> specification = new Specification<Person>() {
            /**
             *
             * @param root 代表查询的实体类
             * @param query 从中可以得到Root对象，即告知JPA Criteria 要查询哪一个实体类
             * @param criteriaBuilder 用于创建criteria相关对象的工厂
             * @return Predicate类型，代表一个查询条件
             */
            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Path path = root.get("id");
                Predicate predicate = criteriaBuilder.gt(path,5);
                return predicate;
            }
        };

        Page<Person> page =  personRepository.findAll(specification,pageRequest);

        System.out.println("总记录数: "+ page.getTotalElements());
        System.out.println("当前第几页："+ page.getNumber());
        System.out.println("总页数："+ page.getTotalPages());
        System.out.println("当前页面List："+ page.getContent());
        System.out.println("当前页面记录数："+ page.getNumberOfElements());


    }
}
