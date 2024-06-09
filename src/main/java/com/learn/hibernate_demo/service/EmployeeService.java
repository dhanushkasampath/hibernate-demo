package com.learn.hibernate_demo.service;

import com.learn.hibernate_demo.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id) throws Exception;
    Employee createOrUpdateEmployee(Employee entity);
    void deleteEmployeeById(Long id) throws Exception;
    void showEntityLifeCycle();
}
