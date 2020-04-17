package com.example.springreactivewebfluxactuatorsecurityexample.reactive.functional;


import com.example.springreactivewebfluxactuatorsecurityexample.webflux.Employee;
import com.example.springreactivewebfluxactuatorsecurityexample.webflux.EmployeeRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EmployeeSpringFunctionalApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeSpringFunctionallIntegrationTest {

    @Autowired
    private EmployeeFunctionalConfig config;

    @MockBean
    private EmployeeRepository employeeRepository;


    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenCorrectEmployee() {
        WebTestClient testClient = WebTestClient
                .bindToRouterFunction(config.getEmployeeByIdRoute())
                .build();

        Employee employee = new Employee("1", "Employee 1");

        given(employeeRepository.findEmployeeById("1")).willReturn(Mono.just(employee));

        testClient
                .get()
                .uri("/employees/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Employee.class)
                .isEqualTo(employee);
    }


    @Test
    public void whenGetAllEmployees_thenCorrectEmployees() {
        WebTestClient testClient = WebTestClient
                .bindToRouterFunction(config.getAllEmployeesRoute())
                .build();

        List<Employee> expected = Arrays.asList(new Employee("1", "Employee 1"), new Employee("2", "Employee 2"));

        Flux<Employee> employeeFlux =Flux.fromIterable(expected);
        given(employeeRepository.findAllEmployees()).willReturn(employeeFlux);

        testClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Employee.class)
                .isEqualTo(expected);
    }


    @Test
    public void whenUpdateEmployee_thenEmployeeUpdate() {

        WebTestClient testClient = WebTestClient
                .bindToRouterFunction(config.updateEmployeeRoute())
                .build();

        Employee employee = new Employee("1", "Employee 1 Update");

        testClient.post()
                .uri("/employees/update")
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(employeeRepository).updateEmployee(employee);


    }
}
