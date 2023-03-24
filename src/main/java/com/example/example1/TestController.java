package com.example.example1;

import com.example.example1.custom.MyUserRepository;
import com.example.example1.manytomany.Blog;
import com.example.example1.manytomany.BlogRepository;
import com.example.example1.manytomany.Tag;
import com.example.example1.manytomany.TagRepository;
import com.example.example1.onetomany.ClassOfStudent;
import com.example.example1.onetomany.ClassRepository;
import com.example.example1.onetomany.Student;
import com.example.example1.onetomany.StudentRepository;
import com.example.example1.onetoone.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Predicate;

@Controller
@RequestMapping(path = "/demo")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonMethodQueryRepository personMethodQueryRepository;

    @Autowired
    private PersonAnnotationQueryRepository personAnnotationQueryRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private PersonAnnotationModifyingRepository personAnnotationModifyingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployeeJpaSpecificationExecutorRepository employeeJpaSpecificationExecutorRepository;

    @Autowired
    private MyUserRepository userCustomRepository;

    // ================================================================================================================
    // 下面这些api接口直接使用了spring jpa接口提供的功能。使用的是UserRepository接口。
    @GetMapping(path = "/add")
    public void addNewUser(@RequestParam String name, @RequestParam String email) {
        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/info")
    @ResponseBody
    public Optional<User> findOne(@RequestParam Long id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/delete")
    public void delete(@RequestParam Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 验证排序和分页查询方法，Pageable的默认实现类：PageRequest
     * @return
     */
    @GetMapping(path = "/page")
    @ResponseBody
    public Page<User> getAllUserByPage() {
        Page<User> returned =  userRepository.findAll(
                PageRequest.of(0, 20, Sort.by(new Sort.Order(Sort.Direction.ASC,"name"))));
        return returned;
    }
    /**
     * 排序查询方法，使用Sort对象
     * @return
     */
    @GetMapping(path = "/sort")
    @ResponseBody
    public Iterable<User> getAllUsersWithSort() {
        return userRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC,"name")));
    }

    // ================================================================================================================
    // 下面这些api接口通过定义spring识别的方法名，来定制了不同的查询语句。使用的是PersonRepository接口。
    @GetMapping(path = "/findByEmailAndLastName")
    @ResponseBody
    public Iterable<Person> getPersonsFindByEmailAndLastName(@RequestParam  String email, @RequestParam String lastName) {
        return personMethodQueryRepository.findByEmailAndLastName(email, lastName);
    }

    @GetMapping(path = "/findDistinctPersonByLastNameOrFirstName")
    @ResponseBody
    public Iterable<Person> getPersonsFindDistinctPersonByLastNameOrFirstName(@RequestParam  String lastName, @RequestParam String firstName) {
        return personMethodQueryRepository.findDistinctPersonByLastNameOrFirstName(lastName, firstName);
    }

    @GetMapping(path = "/findPersonDistinctByLastNameOrFirstName")
    @ResponseBody
    public Iterable<Person> getPersonsFindPersonDistinctByLastNameOrFirstName(@RequestParam  String lastName, @RequestParam String firstName) {
        return personMethodQueryRepository.findPersonDistinctByLastNameOrFirstName(lastName, firstName);
    }

    @GetMapping(path = "/findByLastNameIgnoreCase")
    @ResponseBody
    public Iterable<Person> getPersonsFindByLastNameIgnoreCase(@RequestParam String lastName) {
        return personMethodQueryRepository.findByLastNameIgnoreCase(lastName);
    }

    @GetMapping(path = "/findByLastNameAndFirstNameAllIgnoreCase")
    @ResponseBody
    public Iterable<Person> getPersonsFndByLastNameAndFirstNameAllIgnoreCase(@RequestParam String firstName, @RequestParam String lastName) {
        return personMethodQueryRepository.findByLastNameAndFirstNameAllIgnoreCase(lastName, firstName);
    }

    @GetMapping(path = "/findByLastNameOrderByFirstNameAsc")
    @ResponseBody
    public Iterable<Person> getPersonsFindByLastNameOrderByFirstNameAsc(@RequestParam String lastName) {
        return personMethodQueryRepository.findByLastNameOrderByFirstNameAsc(lastName);
    }

    // ================================================================================================================
    // 下面是关于分页和排序方法的示例
    @GetMapping(path = "/getPageableByLastName")
    @ResponseBody
    public Page<Person> getPageableByLastName(@RequestParam String lastName, @RequestParam Integer page, @RequestParam Integer size) {

        return personMethodQueryRepository.findByLastName(lastName, PageRequest.of(page, // page: 表示查询第几个page（以0开始)
                size,                                                         // size: 表示一个page包含多少个元素
                Sort.by(new Sort.Order(Sort.Direction.ASC,"age")))); // sort: 指定返回的结果如何排序，这里指定按照age升序排序

        // 上面接口的请求和响应示例：
        // request: http://localhost:8080/demo/getPageableByLastName?lastName=bbb&page=1&size=5
        // response:
        /*
        {
            "content": [
                {
                    "id": 27,
                    "firstName": "aaa6",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test6@aaa.com"
                },
                {
                    "id": 28,
                    "firstName": "aaa7",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test7@aaa.com"
                },
                {
                    "id": 29,
                    "firstName": "aaa8",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test8@aaa.com"
                }
            ],
            "pageable": {
                "sort": {
                    "unsorted": false,
                    "sorted": true,
                    "empty": false
                },
                "pageNumber": 1,
                "pageSize": 5,
                "offset": 5,
                "unpaged": false,
                "paged": true
            },
            "totalPages": 2,
            "totalElements": 8,
            "last": true,
            "numberOfElements": 3,
            "first": false,
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "size": 5,
            "number": 1,
            "empty": false
        }
         */
    }

    // 此api使用Slice返回person分页的person对象， 它和上面个page的不同是，slice返回的json是没有totalPages, totalElements的信息的。
    @GetMapping(path = "/getSliceByFirstName")
    @ResponseBody
    public Slice<Person> getSliceByFirstName(@RequestParam String firstName, @RequestParam Integer page, @RequestParam Integer size) {

        return personMethodQueryRepository.findByFirstName(firstName, PageRequest.of(page, // page: 表示查询第几个page（以0开始)
                size,                                                         // size: 表示一个page包含多少个元素
                Sort.by(new Sort.Order(Sort.Direction.ASC, "age")))); // sort: 指定返回的结果如何排序，这里指定按照age升序排序
        // 上面接口的请求和响应示例：
        // request: http://localhost:8080/demo/getSliceByFirstName?firstName=aaa&page=0&size=2
        // response:
        /*
        {
            "content": [
                {
                    "id": 22,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": false,
                    "age": 12,
                    "email": "test@aaa.com"
                },
                {
                    "id": 24,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": true,
                    "age": 23,
                    "email": "test3@aaa.com"
                }
            ],
            "pageable": {
                "sort": {
                    "unsorted": false,
                    "sorted": true,
                    "empty": false
                },
                "pageNumber": 0,
                "pageSize": 2,
                "offset": 0,
                "unpaged": false,
                "paged": true
            },
            "numberOfElements": 2,
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "last": false,
            "first": true,
            "size": 2,
            "number": 0,
            "empty": false
        }
         */
    }

    @GetMapping(path = "/getListByAge")
    @ResponseBody
    public List<Person> getListByAge(@RequestParam Integer age, @RequestParam String sortBy) {
        return personMethodQueryRepository.findByAge(age,
                Sort.by(new Sort.Order(Sort.Direction.ASC, sortBy))); // sort: 指定返回的结果如何排序，这里指定按照age升序排序
        // 上面请求的request：http://localhost:8080/demo/getListByAge?age=34&sortBy=lastName
        // 返回的response：
        /*
        [
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 26,
                "firstName": "aaa5",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test5@aaa.com"
            },
            {
                "id": 27,
                "firstName": "aaa6",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test6@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            },
            {
                "id": 29,
                "firstName": "aaa8",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test8@aaa.com"
            }
        ]
         */
    }

    @GetMapping(path = "/getListByAgeOrderByFirstName")
    @ResponseBody
    public List<Person> getListByAgeOrderByFirstName(@RequestParam Integer age, @RequestParam Integer page, @RequestParam Integer size) {
        return personMethodQueryRepository.findByAgeOrderByFirstName(age,
                PageRequest.of(page, size));
        // request: http://localhost:8080/demo/getListByAgeOrderByFirstName?page=0&size=4&age=34
        // response: JPA方法使用了Pageable对象，返回的结果就是按分页返回的：
        /*
        [
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            },
            {
                "id": 26,
                "firstName": "aaa5",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test5@aaa.com"
            },
            {
                "id": 27,
                "firstName": "aaa6",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test6@aaa.com"
            }
        ]
         */
    }

    //===============================================================================================================
    // 下面是使用top，first查询的示例
    @GetMapping(path = "/queryFirst10ByLastName")
    @ResponseBody
    public Page<Person> queryFirst10ByLastName(@RequestParam String lastName, @RequestParam Integer page, @RequestParam Integer size) {
        return personMethodQueryRepository.queryFirst5ByLastName(lastName, PageRequest.of(page, size));
        // request:http://localhost:8080/demo/queryFirst10ByLastName?size=10&page=0&lastName=bbb
        // response:
        /*
        {
    "content": [
        {
            "id": 22,
            "firstName": "aaa",
            "lastName": "bbb",
            "active": false,
            "age": 12,
            "email": "test@aaa.com"
        },
        {
            "id": 23,
            "firstName": "aaa",
            "lastName": "bbb",
            "active": false,
            "age": 34,
            "email": "test2@aaa.com"
        },
        {
            "id": 24,
            "firstName": "aaa",
            "lastName": "bbb",
            "active": true,
            "age": 23,
            "email": "test3@aaa.com"
        },
        {
            "id": 25,
            "firstName": "aaa4",
            "lastName": "bbb",
            "active": false,
            "age": 32,
            "email": "test4@aaa.com"
        },
        {
            "id": 26,
            "firstName": "aaa5",
            "lastName": "bbb",
            "active": true,
            "age": 34,
            "email": "test5@aaa.com"
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "pageNumber": 0,
        "pageSize": 10,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 5,
    "first": true,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "numberOfElements": 5,
    "size": 10,
    "number": 0,
    "empty": false
}
         */
    }

    @GetMapping("/findFirstByOrderByLastNameAsc")
    @ResponseBody
    public Person findFirstByOrderByLastNameAsc() {
        return personMethodQueryRepository.findFirstByOrderByLastNameAsc();
        // request: http://localhost:8080/demo/findFirstByOrderByLastNameAsc
        // response:
        /*
        {
            "id": 22,
            "firstName": "aaa",
            "lastName": "bbb",
            "active": false,
            "age": 12,
            "email": "test@aaa.com"
        }
         */
    }

    //===============================================================================================================
    // 下面是通过使用 Java 8 Stream 作为返回类型来逐步处理查询方法的结果，而不是简单地将查询结果包装在 Stream 数据存储中
    @Transactional // 使用返回stream对象的jpa接口时，方法必须加上这个事务的支持
    @GetMapping("/findAllByCustomQueryAndStream")
    @ResponseBody
    public List<Person> findAllByCustomQueryAndStream() {
        Stream<Person> stream = null;
        List<Person> result = new ArrayList<>();
        try {
            stream = personMethodQueryRepository.findAllByCustomQueryAndStream();
            result = stream.filter(i -> i.getAge() > 30).distinct().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream!=null){
                stream.close();
            }
        }

        return result;

        // request: http://localhost:8080/demo/findAllByCustomQueryAndStream
        // response:
        /*
        [
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            },
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 24,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 23,
                "email": "test3@aaa.com"
            },
            {
                "id": 25,
                "firstName": "aaa4",
                "lastName": "bbb",
                "active": false,
                "age": 32,
                "email": "test4@aaa.com"
            },
            {
                "id": 26,
                "firstName": "aaa5",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test5@aaa.com"
            },
            {
                "id": 27,
                "firstName": "aaa6",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test6@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            },
            {
                "id": 29,
                "firstName": "aaa8",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test8@aaa.com"
            }
        ]
         */
    }

    //===============================================================================================================
    // 下面是通过注解来操作数据库的示例
    @GetMapping("/findByEmailAddress")
    @ResponseBody
    public Person findByEmailAddress(@RequestParam String email) {
        return personAnnotationQueryRepository.findByEmailAddress(email);

        //request: http://localhost:8080/demo/findByEmailAddress?email=test@aaa.com
        //response:
        /*
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            }
         */
    }

    @GetMapping("/findByFirstNameEndWith")
    @ResponseBody
    public List<Person> findByFirstNameEndWith(@RequestParam String firstName) {
        return personAnnotationQueryRepository.findByFirstNameEndWith(firstName);
        // request: http://localhost:8080/demo/findByFirstNameEndWith?firstName=aaa
        // response:
        /*
        [
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            },
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 24,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 23,
                "email": "test3@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            }
        ]
         */
    }

    @GetMapping("/findByEmailAddressUseNativeSQL")
    @ResponseBody
    public Person findByEmailAddressUseNativeSQL(@RequestParam String email) {
        return personAnnotationQueryRepository.findByEmailAddressUseNativeSQL(email);

        //request: http://localhost:8080/demo/findByEmailAddressUseNativeSQL?email=test@aaa.com
        //response:
        /*
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            }
         */
    }

    @GetMapping("/findByFirstNameSortUseNativeSQL")
    @ResponseBody
    public List<Person> findByFirstSortUseNativeSQL(@RequestParam String firstName, @RequestParam String orderBy) {
        return personAnnotationQueryRepository.findByFirstNameSortUseNativeSQL(firstName, orderBy);

        //request: http://localhost:8080/demo/findByFirstNameSortUseNativeSQL?firstName=aaa&orderBy=age
        //response:
        /*
        [
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            },
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 24,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 23,
                "email": "test3@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            }
        ]
         */
    }

    @GetMapping("/findByFirstNameSortUseJPQL")
    @ResponseBody
    public List<Person> findByFirstNameSortUseJPQL(@RequestParam String firstName, @RequestParam String orderBy) {
        return personAnnotationQueryRepository.findByFirstNameSortUseJPQL(firstName, Sort.by(orderBy));
        // request: http://localhost:8080/demo/findByFirstNameSortUseJPQL?firstName=aaa&orderBy=age
        // response:
        /*
        [
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            },
            {
                "id": 24,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 23,
                "email": "test3@aaa.com"
            },
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            }
        ]
         */
    }

    @GetMapping("/findByAsArrayAndSort")
    @ResponseBody
    public List<Object[]> findByAsArrayAndSort(@RequestParam String lastName, @RequestParam String orderBy) {
        return personAnnotationQueryRepository.findByAsArrayAndSort(lastName, Sort.by(orderBy));
        //request: http://localhost:8080/demo/findByAsArrayAndSort?lastName=bbb&orderBy=age
        //response:
        /*
        [
            [
                22,
                3
            ],
            [
                24,
                3
            ],
            [
                25,
                4
            ],
            [
                23,
                3
            ],
            [
                26,
                4
            ],
            [
                27,
                4
            ],
            [
                28,
                3
            ],
            [
                29,
                4
            ]
        ]
         */
    }

    @GetMapping("/findByLastNamePageable")
    @ResponseBody
    public Page<Person> findByLastNamePageable(@RequestParam String lastName, @RequestParam Integer page, @RequestParam Integer size) {
        return personAnnotationQueryRepository.findByLastName(lastName, PageRequest.of(page, size));
        // request: http://localhost:8080/demo/findByLastNamePageable?page=0&lastName=bbb&size=10
        // response:
        /*
        {
            "content": [
                {
                    "id": 22,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": false,
                    "age": 12,
                    "email": "test@aaa.com"
                },
                {
                    "id": 23,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": false,
                    "age": 34,
                    "email": "test2@aaa.com"
                },
                {
                    "id": 24,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": true,
                    "age": 23,
                    "email": "test3@aaa.com"
                },
                {
                    "id": 25,
                    "firstName": "aaa4",
                    "lastName": "bbb",
                    "active": false,
                    "age": 32,
                    "email": "test4@aaa.com"
                },
                {
                    "id": 26,
                    "firstName": "aaa5",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test5@aaa.com"
                },
                {
                    "id": 27,
                    "firstName": "aaa6",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test6@aaa.com"
                },
                {
                    "id": 28,
                    "firstName": "aaa",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test7@aaa.com"
                },
                {
                    "id": 29,
                    "firstName": "aaa8",
                    "lastName": "bbb",
                    "active": true,
                    "age": 34,
                    "email": "test8@aaa.com"
                }
            ],
            "pageable": {
                "sort": {
                    "sorted": false,
                    "unsorted": true,
                    "empty": true
                },
                "pageNumber": 0,
                "pageSize": 10,
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "last": true,
            "totalPages": 1,
            "totalElements": 8,
            "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
            },
            "first": true,
            "numberOfElements": 8,
            "size": 10,
            "number": 0,
            "empty": false
        }
         */
    }

    @GetMapping("/findByLastNameOrFirstName")
    @ResponseBody
    public List<Person> findByLastNameOrFirstName(@RequestParam String lastName, @RequestParam String firstName) {
        return personAnnotationQueryRepository.findByLastNameOrFirstName(lastName, firstName);
        //request: http://localhost:8080/demo/findByLastNameOrFirstName?lastName=bbb&firstName=aaa
        //response:
        /*
        [
            {
                "id": 22,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 12,
                "email": "test@aaa.com"
            },
            {
                "id": 23,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": false,
                "age": 34,
                "email": "test2@aaa.com"
            },
            {
                "id": 24,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 23,
                "email": "test3@aaa.com"
            },
            {
                "id": 25,
                "firstName": "aaa4",
                "lastName": "bbb",
                "active": false,
                "age": 32,
                "email": "test4@aaa.com"
            },
            {
                "id": 26,
                "firstName": "aaa5",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test5@aaa.com"
            },
            {
                "id": 27,
                "firstName": "aaa6",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test6@aaa.com"
            },
            {
                "id": 28,
                "firstName": "aaa",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test7@aaa.com"
            },
            {
                "id": 29,
                "firstName": "aaa8",
                "lastName": "bbb",
                "active": true,
                "age": 34,
                "email": "test8@aaa.com"
            }
        ]
         */
    }

    //===============================================================================================================
    // 以下是使用了CommonRepository定义了公用的DB操作，并且是针对公用的列的，表的公用列在AbstractCommonEntity中定义。
    // 继承自AbstractCommonEntity的具体实体类是Dog和Cat
    @GetMapping("/findDogByAgeGreaterThan")
    @ResponseBody
    public List<Dog> findDogByAgeGreaterThan(@RequestParam Integer age) {
        return dogRepository.findByAgeGreaterThan(age);
    }

    @GetMapping("/findDogByName")
    @ResponseBody
    public List<Dog> findDogByName(@RequestParam String name) {
        return dogRepository.findAllByName(name);
    }

    @GetMapping("/findDogByContainFood")
    @ResponseBody
    public Page<Dog> findDogByContainFood(@RequestParam String food, @RequestParam Integer page, @RequestParam Integer size) {
        return dogRepository.findByFoodContaining(food, PageRequest.of(page, size));
    }


    //===============================================================================================================
    // 以下是使用@Modifying注解修改和删除数据
    @GetMapping("/setFirstNameByLastName")
    @ResponseBody
    public Integer setFirstNameByLastName(@RequestParam String firstName, @RequestParam String lastName) {
        return personAnnotationModifyingRepository.setFirstNameByLastName(firstName, lastName);
        // request: http://localhost:8080/demo/setFirstNameByLastName?lastName=bbb111&firstName=updated
        // response: 2
    }

    @GetMapping("/deleteByLastName")
    @ResponseBody
    public void deleteByLastName(@RequestParam String lastName) {
        personAnnotationModifyingRepository.deleteByLastName(lastName);
        // request: http://localhost:8080/demo/deleteByLastName?lastName=bbb111
    }

    //===============================================================================================================
    // 以下是使用OneToOne mapping获取employee表和它的关联表department的数据
    @GetMapping("/findEmployeeByFirstNameContains")
    @ResponseBody
    public List<Employee> findEmployeeByFirstNameContains(@RequestParam String firstNameContains) {
        List<Employee> employees = employeeRepository.findAllByFirstNameContains(firstNameContains);
        for(Employee employee: employees) {
            System.out.println("========================");
            System.out.println(employee.toString());
            System.out.println("\temployee --> department:");
            System.out.println(employee.getDepartment().toString());
        }
        return employees;
    }

    // 这个API是使用级联删除，来把department关联的employee也一并删除掉
    @GetMapping("/deleteDepartmentAndRelatedEmployee")
    @ResponseBody
    public void deleteDepartmentAndRelatedEmployee(@RequestParam String departmentName) {
        departmentRepository.deleteByName(departmentName);
    }

    @GetMapping("/createEmployeeAndDepartment")
    @ResponseBody
    public void createEmployeeAndDepartment(@RequestParam String firstName, @RequestParam String lastName,
                                            @RequestParam Integer age, @RequestParam String gender, @RequestParam String email,
                                            @RequestParam String departmentName) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAge(age);
        employee.setEmail(email);
        employee.setGender(Gender.valueOf(gender));

        Department department = new Department();
        department.setName(departmentName);
        department.setEmployee(employee); // 在这里把department关联的employee对象设置上
        employee.setDepartment(department); //这里把employee关联的department对象也设置上

        employeeRepository.save(employee);

        System.out.println(employee);
        System.out.println(employee.getDepartment());
        System.out.println(department);
        System.out.println(department.getEmployee());

        //上面的save会同时执行两个sql语句，首先是insert department表，之后是insert employee表：
        /*
        Hibernate:
            insert
            into
                department_one_to_one
                (name)
            values
                (?)
        Hibernate:
            insert
            into
                employee_one_to_one
                (age, email, first_name, gender, last_name)
            values
                (?, ?, ?, ?, ?)
                 */

        //http request: http://localhost:8080/demo/createEmployeeAndDepartment?age=34&lastName=bbb111&departmentName=dep_123&firstName=aadsf&email=test123@test.com&gender=MAIL
    }

    //===============================================================================================================
    // 以下是使用OneToMany, ManyToOne mapping获取employee表和它的关联表department的数据
    @GetMapping("/findClassByName")
    @ResponseBody
    public ClassOfStudent findClassByName(@RequestParam String className) {
        ClassOfStudent returnedClassOfStudent = classRepository.findByName(className);
        System.out.println("Done");
        System.out.println(returnedClassOfStudent.getId());
        System.out.println(returnedClassOfStudent.getName());
        System.out.println("Before getStudents, because class.students using lazy load to fetch related students, will query sql only when getting related students data. ");
        System.out.println("Fetching related students...");
        Set<Student> students = returnedClassOfStudent.getStudents();
        for(Student s: students) {
            System.out.println("Age: " + s.getAge());
        }

        System.out.println("Fetched related students.");

        return returnedClassOfStudent;
        //request: http://localhost:8080/demo/findClassByName?className=1-1
        //response:
        /*
        {
            "id": 1,
            "name": "1-1",
            "teacher": "liupeng",
            "students": [
                {
                    "id": 6,
                    "firstName": "aaa",  << ClassOfStudent.students使用了orderby，按firstname的升序排序输出所有class下的students列表
                    "lastName": "ddd",
                    "age": 12
                    //注意：这里没有输出student的class字段信息，是因为加了@JsonBackReference注解
                    //student对象有对class对象的引用，如果不加@JsonBackReference注解，那么json序列化返回的时候，就又会再去获取class对象的字段，
                    //那么又会再次获取所有students对象，这样就会形成循环引用并在程序驱动时报错。
                },
                {
                    "id": 7,
                    "firstName": "bbb",
                    "lastName": "dsfdf",
                    "age": 22
                },
                {
                    "id": 8,
                    "firstName": "ccc",
                    "lastName": "ewqe",
                    "age": 2
                },
                {
                    "id": 1,
                    "firstName": "minghe",
                    "lastName": "liu",
                    "age": 7
                },
                {
                    "id": 3,
                    "firstName": "peng",
                    "lastName": "he",
                    "age": 22
                },
                {
                    "id": 4,
                    "firstName": "xieming",
                    "lastName": "da",
                    "age": 33
                }
            ]
        }
         */
    }

    //===============================================================================================================
    // 以下是使用ManyToMany的mapping关系来查询blog的信息，并且会返回blog的关联的tag的信息
    @GetMapping("/findByBlogTitle")
    @ResponseBody
    public Blog findByBlogTitle(@RequestParam String blogTitle) {
        Blog blog = blogRepository.findByTitle(blogTitle);
        /*
        Hibernate:
        select
            blog0_.id as id1_0_,
            blog0_.content as content2_0_,
            blog0_.description as descript3_0_,
            blog0_.title as title4_0_
        from
            blog_many_to_many blog0_
        where
            blog0_.title=?
         */
        System.out.println("Done");
        return blog;
        /*
        上面的return blog会触发blog对象的序列化操作，由于blog和tag有关联，会触发hibernate去联表查询来获取blog关联的tag的信息：
        Hibernate:
        select
            tags0_.blog_id as blog_id1_1_0_,
            tags0_.tag_id as tag_id2_1_0_,
            tag1_.id as id1_9_1_,
            tag1_.name as name2_9_1_
        from
            blog_tag_relation_many_to_many tags0_
        inner join
            tag_many_to_many tag1_
                on tags0_.tag_id=tag1_.id
        where
            tags0_.blog_id=?
         */

        // request：http://localhost:8080/demo/findByBlogTitle?blogTitle=aaa
        // response:
        /*
        {
            "id": 1,
            "title": "aaa",
            "description": "sdfasdf",
            "content": null,
            "tags": [
                {
                    "id": 3,
                    "name": "eee"
                },
                {
                    "id": 4,
                    "name": "rrrr"
                }
            ]
        }
         */
    }

    @GetMapping("/saveBlogAndTag")
    @ResponseBody
    public void saveBlogAndTag() {
        Blog blog = new Blog();
        blog.setTitle("blog title");
        blog.setContent("blog content");

        Tag tag1 = new Tag();
        tag1.setName("tag1");

        Tag tag2 = new Tag();
        tag2.setName("tag2");

        Tag tag3 = new Tag();
        tag3.setName("tag3");

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        blog.setTags(tags);

        blogRepository.save(blog);

        /*
        先插入到blog表:
        >> Hibernate:
        insert com.example.example1.manytomany.Blog
        insert
                into
        blog_many_to_many
                (content, description, title)
        values
                (?, ?, ?)
        2022-07-20 20:52:41.072 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [blog content]
        2022-07-20 20:52:41.072 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [VARCHAR] - [null]
        2022-07-20 20:52:41.073 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [blog title]

        再插入多次到tag表:
        >> Hibernate:
        insert com.example.example1.manytomany.Tag
        insert
                into
        tag_many_to_many
                (name)
        values
                (?)
        2022-07-20 20:52:41.090 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [tag1]

        >> Hibernate:
        insert com.example.example1.manytomany.Tag
        insert
                into
        tag_many_to_many
                (name)
        values
                (?)
        2022-07-20 20:52:41.094 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [tag2]

        >> Hibernate:
        insert com.example.example1.manytomany.Tag
        insert
                into
        tag_many_to_many
                (name)
        values
                (?)
        2022-07-20 20:52:41.097 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [tag3]

       最后再插入关联表:
        >> Hibernate:
        insert collection row com.example.example1.manytomany.Blog.tags
        insert
                into
        blog_tag_relation_many_to_many
                (blog_id, tag_id)
        values
                (?, ?)
        2022-07-20 20:52:41.113 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [8]
        2022-07-20 20:52:41.113 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [16]

        >> Hibernate:
        insert collection row com.example.example1.manytomany.Blog.tags
        insert
                into
        blog_tag_relation_many_to_many
                (blog_id, tag_id)
        values
                (?, ?)
        2022-07-20 20:52:41.116 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [8]
        2022-07-20 20:52:41.116 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [17]

        >> Hibernate:
        insert collection row com.example.example1.manytomany.Blog.tags
        insert
                into
        blog_tag_relation_many_to_many
                (blog_id, tag_id)
        values
                (?, ?)
        2022-07-20 20:52:41.118 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [8]
        2022-07-20 20:52:41.118 TRACE 8657 --- [nio-8080-exec-9] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [18]
    */
    }

    @GetMapping("/deleteBlogByTitle")
    @ResponseBody
    @Transactional
    public void deleteByBlogTitle(@RequestParam String blogTitle) {
        blogRepository.deleteByTitle(blogTitle);
    /*
        >> Hibernate:
        delete FKs in join table
        先删除关联表
        delete from blog_tag_relation_many_to_many where ( blog_id ) in ( select id from blog_many_to_many where title=? )
        2022-07-20 21:16:39.113 TRACE 8657 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [blog title]

        >> Hibernate:
        delete from Blog b where b.title = ?1
        再删除blog表
        delete from blog_many_to_many where title=?
        2022-07-20 21:16:39.116 TRACE 8657 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [blog title]
     */
    }

    @GetMapping("/deleteTagByName")
    @ResponseBody
    @Transactional
    public void deleteTagByName(@RequestParam String tagName) {
        tagRepository.deleteByName(tagName);
    }

    //===============================================================================================================
    //使用QueryByExampleExecutor接口进行基于Example的查询
    //===============================================================================================================
    @GetMapping("/findStudentsByExample")
    @ResponseBody
    public List<Student> findStudentsByExample() {
        Student studentExample = new Student();
        studentExample.setFirstName("ming"); //查询firstname是ming
        studentExample.setAge(123); //这里设置的age会被忽略，不会用来做查询条件

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.startsWith()) //firsName按照startWith查询
                .withIgnorePaths("age"); //这里配置了不查询age，所以上面的setAge是没有用的

        Example<Student> ex = Example.of(studentExample, matcher);

        return studentRepository.findAll(ex); //查询数据库，返回符合上面条件的student记录

        /*
        >> Hibernate:
        select
        student0_.id as id1_8_,
                student0_.age as age2_8_,
        student0_.class_id as class_id5_8_,
                student0_.first_name as first_na3_8_,
        student0_.last_name as last_nam4_8_
                from
        student_many_to_one student0_
        where
        student0_.first_name like ? escape ?
         */
    }

    //===============================================================================================================
    //使用QueryByExampleExecutor接口进行基于Example的多种条件组合查询
    //===============================================================================================================
    @GetMapping("/findStudentsByExample2")
    @ResponseBody
    public List<Student> findStudentsByExample2() {
        //创建查询对象
        Student studentExample = new Student();
        studentExample.setFirstName("ming");
        studentExample.setLastName("aaa");
        studentExample.setAge(32);

        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.startsWith()) //LastName采用“开始匹配”的方式查询
                .withIgnorePaths("age"); //忽略属性

        Example<Student> ex = Example.of(studentExample, matcher);

        return studentRepository.findAll(ex); //查询数据库，返回符合上面条件的student记录
        /*
        >> Hibernate:
        select
            student0_.id as id1_8_,
            student0_.age as age2_8_,
            student0_.class_id as class_id5_8_,
            student0_.first_name as first_na3_8_,
            student0_.last_name as last_nam4_8_
        from
            student_many_to_one student0_
        where
            (
                lower(student0_.first_name) like ? escape ?
            )
            and (
                lower(student0_.last_name) like ? escape ?
            )
         */
    }

    //===============================================================================================================
    //使用QueryByExampleExecutor接口进行基于Example的null值的查询
    //===============================================================================================================
    @GetMapping("/findPersonsWithNullByExample")
    @ResponseBody
    public List<Person> findPersonsWithNullByExample() {
        //创建查询对象
        Person personExample = new Person();

        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withIncludeNullValues() //设置查询保护null的记录
                .withIgnorePaths("id", "lastName", "active", "age", "email"); //忽略掉你不想查询是null的字段，只保留了firstName

        Example<Person> ex = Example.of(personExample, matcher);

        return personAnnotationModifyingRepository.findAll(ex); //查询数据库，返回符合上面条件的person记录
        /*
        >> Hibernate:
        select
            person0_.id as id1_7_,
            person0_.active as active2_7_,
            person0_.age as age3_7_,
            person0_.email as email4_7_,
            person0_.firstname as firstnam5_7_,
            person0_.lastname as lastname6_7_
        from
            person person0_
        where
            person0_.firstname is null
         */

        /*
        Http Request: GET http://localhost:8080/demo/findPersonsWithNullByExample

        Http Response:
        [
          {
            "id": 26,
            "firstName": null, <<<< 查出来了null的值
            "lastName": "bbb",
            "active": true,
            "age": 34,
            "email": "test5@aaa.com"
          },
          {
            "id": 29,
            "firstName": null, <<<< 查出来了null的值
            "lastName": "bbb",
            "active": true,
            "age": 34,
            "email": "test8@aaa.com"
          }
        ]
         */
    }

    //===============================================================================================================
    //使用JpaSpecificationExecutor接口进行数据库的查询
    //===============================================================================================================
    @GetMapping("/findEmployeeWithJpaSpecificationExecutor")
    @ResponseBody
    public List<Employee> findEmployeeWithJpaSpecificationExecutor(@RequestParam String firstName,
                                                                   @RequestParam String lastName,
                                                                   @RequestParam Integer ageFrom,
                                                                   @RequestParam Integer ageTo,
                                                                   @RequestParam String departmentName) {
        return employeeJpaSpecificationExecutorRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(StringUtils.hasText(firstName)) {
                //liked的查询条件
                predicates.add(cb.like(root.get("firstName"),"%" + firstName  +"%"));
            }

            if(StringUtils.hasText(lastName)) {
                //equal查询
                predicates.add(cb.equal(root.get("lastName"), lastName));
            }

            if(ageFrom > 0 && ageTo > 0) {
                //between查询
                predicates.add(cb.between(root.get("age"), ageFrom, ageTo));
            }

            if(StringUtils.hasText(departmentName))  {
                //连表查询department表中对应的name
                predicates.add(cb.equal(root.join("department").get("name"), departmentName));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });


        /*
        >> HttpRequest: GET http://localhost:8080/demo/findEmployeeWithJpaSpecificationExecutor?firstName=peng&lastName=liu2&ageFrom=11&ageTo=43&departmentName=dep_3
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Fri, 12 Aug 2022 09:06:36 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        >> HttpResponse:
        [
          {
            "id": 6,
            "firstName": "peng2",
            "lastName": "liu2",
            "age": 33,
            "department": {
              "id": 6,
              "name": "dep_3"
            },
            "gender": "FMAIL",
            "email": "test2@gg.com"
          }
        ]
         */

        /*
        >> Hibernate:
        select
            employee0_.id as id1_6_,
            employee0_.age as age2_6_,
            employee0_.email as email3_6_,
            employee0_.first_name as first_na4_6_,
            employee0_.gender as gender5_6_,
            employee0_.last_name as last_nam6_6_
        from
            employee_one_to_one employee0_
        inner join
            department_one_to_one department1_
                on employee0_.id=department1_.id
        where
            (
                employee0_.first_name like ?
            )
            and employee0_.last_name=?
            and (
                employee0_.age between 11 and 43
            )
            and department1_.name=?
        2022-08-12 17:47:23.528 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [%peng%]
        2022-08-12 17:47:23.528 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [VARCHAR] - [liu2]
        2022-08-12 17:47:23.528 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [dep_3]
        2022-08-12 17:47:23.530 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id1_6_] : [BIGINT]) - [6]
        2022-08-12 17:47:23.530 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([age2_6_] : [INTEGER]) - [33]
        2022-08-12 17:47:23.530 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([email3_6_] : [VARCHAR]) - [test2@gg.com]
        2022-08-12 17:47:23.531 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([first_na4_6_] : [VARCHAR]) - [peng2]
        2022-08-12 17:47:23.531 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([gender5_6_] : [VARCHAR]) - [FMAIL]
        2022-08-12 17:47:23.531 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([last_nam6_6_] : [VARCHAR]) - [liu2]

        >>Hibernate:
        select
            department0_.id as id1_4_0_,
            department0_.name as name2_4_0_
        from
            department_one_to_one department0_
        where
            department0_.id=?
        2022-08-12 17:47:23.531 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [6]
        2022-08-12 17:47:23.533 TRACE 3414 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([name2_4_0_] : [VARCHAR]) - [dep_3]
         */
    }

    //===============================================================================================================
    //使用自定义的com.example.example1.custom.UserRepositoryCustomImpl来实现一个查询User表的方法
    //===============================================================================================================
    @GetMapping(path = "/customer")
    @ResponseBody
    public Iterable<User> findCustomerMethodNamesLike(@RequestParam String name) {
        return this.userCustomRepository.customerMethodNamesLike(name);

        /*
        >> HttpRequest:
        GET http://localhost:8080/demo/customer?name=123

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Fri, 12 Aug 2022 14:00:21 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        >> HttpResponse:
        [
          {
            "id": 10,
            "name": "123sdlfkajsdfl",
            "email": "someemail@someemailprovider.com"
          },
          {
            "id": 17,
            "name": "123123123",
            "email": "someemail@someemailprovider.com"
          }
        ]
         */

        /*
        >> Hibernate:
        SELECT
        u.*
                FROM
        user as u
                WHERE
        u.name LIKE ?

        2022-08-12 22:00:21.513 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [123%]
        2022-08-12 22:00:21.516 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id] : [BIGINT]) - [10]
        2022-08-12 22:00:21.516 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([email] : [VARCHAR]) - [someemail@someemailprovider.com]
        2022-08-12 22:00:21.517 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([name] : [VARCHAR]) - [123sdlfkajsdfl]
        2022-08-12 22:00:21.517 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id] : [BIGINT]) - [17]
        2022-08-12 22:00:21.517 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([email] : [VARCHAR]) - [someemail@someemailprovider.com]
        2022-08-12 22:00:21.517 TRACE 4499 --- [nio-8080-exec-5] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([name] : [VARCHAR]) - [123123123]
         */
    }
}
