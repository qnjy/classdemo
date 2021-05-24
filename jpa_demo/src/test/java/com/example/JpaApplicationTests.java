package com.example;

import com.example.entity.AyUser;
import com.example.entity.Grade;
import com.example.entity.Student;
import com.example.repositoty.GradeRepository;
import com.example.repositoty.StudentRepository;
import com.example.repositoty.UserRepository;
import com.example.service.AyUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class JpaApplicationTests {

    @Autowired
    AyUserService ayUserService;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
//    @Rollback(false)
    void contextLoads() {

//        AyUser ayUser = ayUserService.findByName("阿昌");
//        System.out.println(ayUser);
        ayUserService.deleteById(4);
    }


    /**
     * 测试排序
     */
    @Test
    void testSort(){
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC,"id"));

        List<AyUser> ayUsers = userRepository.findAll(sort);

        ayUsers.forEach(System.out::println);

    }

    /*测试分页 + 排序 */
    @Test
    void testPage(){
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC,"id"));
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC,"id");
        Page<AyUser> page = userRepository.findAll(pageRequest);

        page.forEach(System.out::println);
    }

    /*测试保存数据时主键生成策略*/
    @Test
    void testSave(){
        AyUser ayUser = new AyUser(null, "阿星", "123456", "710395795@qq.com");
        AyUser user = userRepository.save(ayUser);

    }

    /*
    测试自定义条件
    ExampleMatcher :  条件适配规范
    GenericPropertyMatchers ： 内部类，表示预定义的属性匹配器
        contains ： 包含
        exact : 相等
        caseSensitive : 区分大小写
        ......
     */
    @Test
    void testExample(){
        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("id",ExampleMatcher.GenericPropertyMatchers.exact())
//                .withIncludeNullValues()
//                .withIgnorePaths("name","password")
                .withIgnoreNullValues()
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains())
                ;

        AyUser ayUser = new AyUser();
//        ayUser.setName("阿毅");
        ayUser.setId(1);
        Example<AyUser> example = Example.of(ayUser, matcher);
        List<AyUser> ayUsers = userRepository.findAll(example);
        ayUsers.forEach(ayUser1 -> System.out.println(ayUser1));
    }


    /*测试根据条件分页查询*/
    @Test
    void testPageByExample(){
        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
//                .withIgnoreCase()
//                .withStringMatcher(ExampleMatcher.StringMatcher.DEFAULT)
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains())
                ;
        AyUser ayUser = new AyUser();
        ayUser.setName("阿");
        Example<AyUser> example = Example.of(ayUser,matcher);

        Page<AyUser> page = userRepository.findAll(example, PageRequest.of(0, 3));
        page.forEach(ayUser1 -> System.out.println(ayUser1));
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
    }

    /**
     * 测试动态查询+分页+排序
     */

    @Test
    void testJpaSpecification(){

        AyUser ayUser = new AyUser();
        ayUser.setName("阿");

        Page<AyUser> page = userRepository.findAll(new Specification<AyUser>() {
            @Override
            public Predicate toPredicate(Root<AyUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //获取条件对象
                Predicate predicate = cb.conjunction();
                //获取条件表达式，添加条件
                if (ayUser != null){
                    if (ayUser.getName() != null && !ayUser.getName().equals("")){
                        //cb中封装的有like、ge、gt、or等等
                        predicate.getExpressions().add(cb.like(root.get("name"),"%"+ayUser.getName()+"%"));

//                        predicate.getExpressions().
//                                add(cb.or(cb.like(root.get(""),""),
//                                        cb.like(root.get(""),"")));
                    }
                }

                return predicate;
            }
        }, PageRequest.of(0, 2, Sort.Direction.DESC, "id"));
        page.forEach(ayUser1 -> {
            System.out.println(ayUser1);
        });

    }


    @Autowired
    StudentRepository studentRepository;

    /**
     * 关联查询 : 一对一测试
     */

    @Test
    void testAssociated(){
        Grade grade = new Grade();
//        grade.setId(1);
        grade.setGradeName("三年级");
        Student student = new Student();
        student.setName("天宇");
        student.setGrade(grade);
        //测试添加  联级添加
        studentRepository.save(student);

    }


    @Resource
    private GradeRepository gradeRepository;

    /**
     * 一对多
     */
    @Test
    void testOneToMany(){
        Grade grade = new Grade();
//        grade.setId(1);
        grade.setGradeName("四年级");
        Student student = new Student();
        student.setName("星驰");
        student.setGrade(grade);
        Student student1 = new Student();
        student1.setName("德华");
        student1.setGrade(grade);
        grade.getStudent().add(student);
        grade.getStudent().add(student1);

        gradeRepository.save(grade);

    }


    @Test
    void testFindOnetoOne(){
        Optional<Student> optional = studentRepository.findById(2);
        System.out.println(optional.get());
        System.out.println(optional.get().getGrade());

    }

    @Test
    void testFindOnetoMany(){
        Optional<Grade> optional = gradeRepository.findById(8);
        System.out.println(optional.get());
        optional.get().getStudent().forEach(System.out::println);
    }




}
