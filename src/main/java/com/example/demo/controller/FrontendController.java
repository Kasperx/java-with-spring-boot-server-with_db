package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.InvalidParameterException;
import java.util.List;

import static com.example.demo.service.PersonService.filterPersonData;
import static com.example.demo.service.PersonService.isAdminAccount;

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
    final String htmlFile = "tutorials";

    //public FrontendController(PersonRepository frontendController) { this.personRepository = frontendController; }

    @PostMapping("/createMoreData")
    public void createMoreData(@RequestParam String username, @RequestParam String pw) throws InvalidParameterException {
        if(isAdminAccount(username, pw)) {
            List<Person> personList = PersonService.filterPersonData();
            log.info("Saving all data ("+personList.size()+") to database.");
            personRepository.saveAll(personList);
        }
    }

    @GetMapping("")
    public String loadData(@RequestParam(required = false) String username, @RequestParam(required = false) String pw, Model model){
    //public String loadData(Model model){
        //return "forward:/resources/templates/index.html";
        createNewDataIfNotCreated();
        try {
            List<Person> personList = filterPersonData(isAdminAccount(username, pw), personRepository.findAll());
            //List<Person> personList = PersonService.getData(personRepository.findAll());
            model.addAttribute("persons", personList);
        } catch (InvalidParameterException ipe){
            List<Person> personList = PersonService.filterPersonData(personRepository.findAll());
            model.addAttribute("persons", personList);
            model.addAttribute("message", ipe.getMessage());
            log.error(ipe.getMessage());
        } catch (Exception e){
            model.addAttribute("message", e.getMessage());
            log.error("", e);
        }
        return htmlFile;
    }
    void createNewDataIfNotCreated(){
        if(personRepository.findAll().isEmpty()){
            List<Person> personList = PersonService.filterPersonData();
            log.info("Saving all data ("+personList.size()+") to database.");
            personRepository.saveAll(personList);
        }
    }
    /*
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
     */
    /*
    @GetMapping(value = "/4")
    public ModelAndView load4(){
        ModelAndView view = new ModelAndView();
        view.setViewName("index.html");
        return view;
    }
     */
    /*
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
     */
}
