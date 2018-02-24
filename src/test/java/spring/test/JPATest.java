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

    @Test
    public void testPersonService(){
        Person p1 = new Person();
        p1.setAge(11);
        p1.setLastName("Tom");
        p1.setEmail("aa@163.com");

        Person p2 = new Person();
        p2.setAge(13);
        p2.setLastName("Jack");
        p2.setEmail("bb@163.com");

    }

    @Test
    public void testGet(){


        List persons = personRepository.getByLastNameStartingWithAndIdLessThan("J",10);
        Person person = personRepository.getMaxPerson();
        System.out.println(persons);
        System.out.println(person);
    }

    @Test
    public void testCrud(){

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
        List<Person> people = (List<Person>) personRepository.findAll();
        List<Indent> indents = new ArrayList<Indent>();
        for (int i = 0; i < people.size(); i ++){
            Indent indent = new Indent();
            indent.setIndentName(people.get(i).getLastName());
            indent.setPerson(people.get(i));
            indents.add(indent);
        }
        indentService.saveIndents(indents);
    }

    @Test
    public void testPage(){
        int pageNo = 3;
        int pageSize = 5;

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC,"id");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC,"email");
        Sort sort = Sort.by(order1,order2);

        PageRequest pageRequest = PageRequest.of(pageNo,pageSize,sort);
        Page<Person> page =  personRepository.findAll(pageRequest);

        System.out.println("总记录数: "+ page.getTotalElements());
        System.out.println("当前第几页："+ page.getNumber());
        System.out.println("总页数："+ page.getTotalPages());
        System.out.println("当前页面List："+ page.getContent());
        System.out.println("当前页面记录数："+ page.getNumberOfElements());


    }

    @Test
    public void testJpa(){
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
