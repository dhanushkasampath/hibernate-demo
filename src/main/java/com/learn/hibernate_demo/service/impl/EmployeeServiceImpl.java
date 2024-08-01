package com.learn.hibernate_demo.service.impl;

import com.learn.hibernate_demo.HibernateUtil;
import com.learn.hibernate_demo.entity.Employee;
import com.learn.hibernate_demo.repository.EmployeeRepository;
import com.learn.hibernate_demo.service.EmployeeService;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Employee employee = session.get(Employee.class, Long.parseLong("2"));
        System.out.println(employee.getFirstName() + employee.getLastName());

        List<Employee> employeeList = repository.findAll();

        Employee employee1 = session.get(Employee.class, Long.parseLong("2"));
        System.out.println(employee1.getFirstName() + employee1.getLastName());

        if(employeeList.size() > 0) {
            return employeeList;
        } else {
            return new ArrayList<>();
        }
    }

    public Employee getEmployeeById(Long id) throws Exception
    {
        Optional<Employee> employee = repository.findById(id);

        if(employee.isPresent()) {
            return employee.get();
        } else {
            throw new Exception("No employee record exist for given id");
        }
    }

    public Employee createOrUpdateEmployee(Employee entity) {
        if(entity.getId() == null) {
            //It comes in managed state.
            entity = repository.save(entity);

            return entity;
        }
        //It comes in managed state.
        Optional<Employee> employee = repository.findById(entity.getId());

        if(employee.isPresent())
        {
            Employee newEntity = employee.get();
            newEntity.setEmail(entity.getEmail());
            newEntity.setFirstName(entity.getFirstName());
            newEntity.setLastName(entity.getLastName());

            newEntity = repository.save(newEntity);

            return newEntity;
        } else {
            entity = repository.save(entity);

            return entity;
        }
    }

    public void deleteEmployeeById(Long id) throws Exception
    {
        Optional<Employee> employee = repository.findById(id);

        if(employee.isPresent())
        {
            repository.deleteById(id);
        } else {
            throw new Exception("No employee record exist for given id");
        }
    }

    public void showEntityLifeCycle() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Employee employee = session.get(Employee.class, Long.parseLong("2"));
        System.out.println(employee.getFirstName() + employee.getLastName());

        Boolean doesExist = session.contains(employee);
        System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExist);

        //session cleared, it comes in detached state
        session.clear();
        Boolean doesExistAfterCleared = session.contains(employee);
        System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExistAfterCleared);

        //remove object it goes into removed state.
        session.delete(employee);
        Boolean doesExistAfterRemoved = session.contains(employee);
        System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExistAfterRemoved);
        // this is the diff between JPA and hibernate -> hibernate allows us to remove even the detached entities.
    }

}