package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
//import com.example.demo.service.Database;
import com.example.demo.service.PersonService;
import jakarta.persistence.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demo.service.PersonService.*;

//@RestController
@Controller
//@RequestMapping("/index.html")
//@RequestMapping("/")
@RequestMapping("")
public class FrontendController {

    private static final Logger log = LoggerFactory.getLogger(FrontendController.class);

    //private final PersonRepository personRepository;

    @Autowired
    Environment environment;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PersonRepository personRepository;

    //public FrontendController(PersonRepository frontendController) { this.personRepository = frontendController; }

    @PostMapping("/createMoreData")
    public void createMoreData(@RequestParam String username, @RequestParam String pw) throws InvalidParameterException {
        if(isAdminAccount(username, pw)) {
            List<Person> personList = getData();
            log.info("Saving all data ("+personList.size()+") to database.");
            personRepository.saveAll(personList);
        }
    }

    @GetMapping(value = "/1")
    public String load1(Model model){
        //return "forward:/resources/templates/index.html";
        if(personRepository.findAll().isEmpty()){
            List<Person> personList = getData();
            log.info("Saving all data ("+personList.size()+") to database.");
            personRepository.saveAll(personList);
        }
        try {
            List<Object> temp = personRepository.findAllWithoutPermission();
            //List<Person> personList = convertObjectToPerson(temp);
            List<Person> personList = getAllWithoutPrivateData(personRepository.findAll());
            model.addAttribute("persons", personList);
        } catch (Exception e){
            model.addAttribute("message", e.getMessage());
            log.error("", e);
        }
        return htmlFile;
    }
    @GetMapping(value = "/2")
    public String load2(){
        //return "forward:/resources/templates/index.html";
        return htmlFile;
    }
    @GetMapping(value = "/3")
    public ModelAndView load3(){
        ModelAndView view = new ModelAndView();
        view.setViewName(htmlFile);
        return view;
    }
    /*
    @GetMapping(value = "/4")
    public ModelAndView load4(){
        ModelAndView view = new ModelAndView();
        view.setViewName("index.html");
        return view;
    }
     */
    final String htmlFile = "tutorials";
    @GetMapping(value = "/5/index.html")
    public ModelAndView load5(){
        ModelAndView view = new ModelAndView();
        view.setViewName(htmlFile);
        return view;
    }
    @GetMapping(value = "/5")
    public ModelAndView load5test(){
        ModelAndView view = new ModelAndView();
        view.setViewName(htmlFile);
        return view;
    }
    @GetMapping(value = "/6")
    public ModelAndView load6(){
        ModelAndView view = new ModelAndView();
        view.setViewName(htmlFile);
        return view;
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", personRepository.findAll());
        return htmlFile;
    }
}
