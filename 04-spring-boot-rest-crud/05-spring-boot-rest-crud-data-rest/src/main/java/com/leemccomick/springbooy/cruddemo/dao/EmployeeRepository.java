package com.leemccomick.springbooy.cruddemo.dao;

import com.leemccomick.springbooy.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/* HOW TO USE --> Spring DATA JPA (Get CRUD Methods for free)
1) Delete DAO
2) Create new class "EmployeeRepository" and extends JpaRepository
3) Use EmployeeRepository in EmployeeServiceImpl
*/

/* HOW TO USE -->  Spring DATA REST
1) Add Dependency in pom.xml
	    <!-- Add Dependency for Spring Data Rest -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
2) Delete service and rest packages --> We only need JpaRepository to work for free code
3) In application.properties, we can control the endpoint for Spring DATA REST
   - spring.data.rest.base-path=/magic-api --> Change base endpoint
   - spring.data.rest.default-page-size=3 --> Change page size, by defaults page size is 20
 4) With Spring DATA REST, we can use sorting, pagination, metadata for endpoint without writing code for it
   - http://localhost:8080/magic-api/members?sort=lastName,acs
   - http://localhost:8080/magic-api/members?sort=firstName,desc
   {
    "_embedded": {
        "employees": [
            {
                "firstName": "Juan",
                "lastName": "Vega",
                "email": "juan@luv2code.com",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/magic-api/members/5"
                    },
                    "employee": {
                        "href": "http://localhost:8080/magic-api/members/5"
                    }
                }
            },
            {
                "firstName": "Lee",
                "lastName": "Mc",
                "email": "lee@luv2code.com",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/magic-api/members/6"
                    },
                    "employee": {
                        "href": "http://localhost:8080/magic-api/members/6"
                    }
                }
            },
            {
                "firstName": "Lee",
                "lastName": "Mc",
                "email": "lee@luv2code.com",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/magic-api/members/7"
                    },
                    "employee": {
                        "href": "http://localhost:8080/magic-api/members/7"
                    }
                }
            }
        ]
    },
    "_links": {
        "first": {
            "href": "http://localhost:8080/magic-api/members?page=0&size=3"
        },
        "prev": {
            "href": "http://localhost:8080/magic-api/members?page=0&size=3"
        },
        "self": {
            "href": "http://localhost:8080/magic-api/members?page=1&size=3"
        },
        "last": {
            "href": "http://localhost:8080/magic-api/members?page=1&size=3"
        },
        "profile": {
            "href": "http://localhost:8080/magic-api/profile/members"
        }
    },
    "page": {
        "size": 3,
        "totalElements": 6,
        "totalPages": 2,
        "number": 1
    }
}
*/

// <Employee, Integer> --> Employee == EntityType, Integer == Primary Key
// @RepositoryRestResource --> Change the path from employees to members
@RepositoryRestResource(path = "members")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // That's it. No need to write any code. 🎉
}
