package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    // load employee data
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

	/*
	private List<Employee> theEmployees;

	@PostConstruct
	private void loadData() {

		// create employees
		Employee emp1 = new Employee("Leslie", "Andrews", "leslie@luv2code.com");
		Employee emp2 = new Employee("Emma", "Baumgarten", "emma@luv2code.com");
		Employee emp3 = new Employee("Avani", "Gupta", "avani@luv2code.com");

		// create the list
		theEmployees = new ArrayList<>();

		// add to the list
		theEmployees.add(emp1);
		theEmployees.add(emp2);
		theEmployees.add(emp3);
	}
    */

    // add mapping for "/list"

    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        // get the employees from db
        List<Employee> theEmployees = employeeService.findAll();

        // add to the spring model
        theModel.addAttribute("employees", theEmployees);

        return "employees/list-employees";
    }

    // To Show Form to Add
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // Create model attribute to bind form data
        Employee theEmployee = new Employee();
        theModel.addAttribute("employee", theEmployee);
        return "employees/employee-form";
    }

    // To Save Employee
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
        // Save the employee
        employeeService.save(theEmployee);

        // Use a redirect to prevent duplicate submission
        return "redirect:/employees/list";
    }

    // To Show Form to Update
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel) {
        // Get the employee from the service
        Employee theEmployee = employeeService.findById(theId);

        // Set employee in the model to prepopulate the form
        theModel.addAttribute("employee", theEmployee);

        // Send over to our form
        return "employees/employee-form";
    }

    // To Delete
    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId, Model theModel) {
        // Delete the employee
        employeeService.deleteById(theId);

        // Redirect to the /employees/list
        return "redirect:/employees/list";
    }
}









