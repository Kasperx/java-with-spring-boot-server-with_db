package com.example.demo.service;

import com.example.demo.controller.PersonController;
import com.example.demo.entity.Person;
import com.github.javafaker.Faker;
import jakarta.persistence.Column;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
//@EnableWebMvc
public class PersonService implements WebMvcConfigurer {

    public static final String MESSAGE_ERROR_WRONG_PARAMETERS = "ERROR: Wrong parameter for admin account.";
    public static final String MESSAGE_ERROR_EMPTY_PARAMETERS = "ERROR: Parameters are empty.";
    public static final String TEXT_EMAIL_ADDRESS_FOR_PERSON = "@gmail.com";
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    public static final String databaseName = "PERSON";
    public static final boolean careAboutPersonalData = true;


    public static boolean isAdminAccount (String username, String pw) throws InvalidParameterException {
        if(isParameterEmpty(username, pw)) {
            throw new InvalidParameterException(MESSAGE_ERROR_EMPTY_PARAMETERS);
        } else if (username.equals("admin") && pw.equals("secret")) {
            return true;
        } else {
            throw new InvalidParameterException(MESSAGE_ERROR_WRONG_PARAMETERS);
        }
    }

    static boolean isParameterEmpty (String username, String pw) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(pw);
    }

    public static List<Person> convertObjectToList (List<Map<String, Object>> mapList){
        List<Person> personListTemp = new ArrayList<>();
        for(Map obj: mapList){
            Person person = new Person();
            for(Object temp: obj.entrySet()) {
                if(temp.toString().split("=")[0].equalsIgnoreCase("FIRSTNAME")) {
                    person.setFirstName(
                            temp.toString().split("=")[1]);
                } else if(temp.toString().split("=")[0].equalsIgnoreCase("LASTNAME")) {
                    person.setLastName(
                            temp.toString().split("=")[1]);
                } else if(temp.toString().split("=")[0].equalsIgnoreCase("EMAIL")) {
                    person.setEmail(
                            temp.toString().split("=")[1]);
                } else if(temp.toString().split("=")[0].equalsIgnoreCase("AGE")) {
                    person.setAge(
                            Integer.parseInt(temp.toString().split("=")[1]));
                } else if(temp.toString().split("=")[0].equalsIgnoreCase("PASSWORD")) {
                    if(careAboutPersonalData){
                        person.setPassword("***");
                    } else {
                        person.setPassword(
                                temp.toString().split("=")[1]);

                    }
                }
            }
            personListTemp.add(person);
            log.info(obj.toString());
        }
        return personListTemp;
    }



    public static List<Person> convertObjectToList (Iterable<Person> personIterable){
        List<Person> personListTemp = new ArrayList<>();
        for(Person temp: personIterable){
            Person person = new Person();
            person.setFirstName(
                    temp.getFirstName());
            person.setLastName(
                    temp.getLastName());
            person.setEmail(
                    temp.getEmail());
            person.setAge(
                    temp.getAge());
            if(careAboutPersonalData){
                    person.setPassword("***");
            } else {
                    person.setPassword(
                            temp.getPassword());
            }
            log.info(person.toString());
            personListTemp.add(person);
        }
        return personListTemp;
    }

    List<String> getTableColumnNames () {
        List<String> columns = new ArrayList<String>();
        for (Field field : Person.class.getDeclaredFields()) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                columns.add(col.name());
                System.out.println("Columns: "+col);
            }
        }
        return columns;
    }
    public static String getTabsForConsoleOut(String text){
        final int countCharsFor1Tab = 6;
        final int countCharsFor2Tabs = 10;
        if(StringUtils.isBlank(text)){
            return "";
        } else {
            if (text.length() < countCharsFor1Tab) {
                return "\t\t\t";
            } else if (text.length() < countCharsFor2Tabs) {
                return "\t\t";
            } else {
                return "\t";
            }
        }
    }
    private static final String pathToWebFiles = "/resources/webapp";
    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(pathToWebFiles+"/**")
                .addResourceLocations(pathToWebFiles);
    }
     */
    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(pathToWebFiles+"/**")
                .addResourceLocations(pathToWebFiles)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
     */
    /*
    @Bean
    ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(pathToWebFiles+"/");
        viewResolver.setSuffix("");
        return viewResolver;
    }
     */
    /*
    ApplicationContext applicationContext;
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix(pathToWebFiles+"/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        return templateResolver;
    }
     */
    /*
    @Bean
    public SpringTemplateEngine templateEngine(){
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
     */
    public static List<Person> getNewData () {
        List<Person> personList = new ArrayList<>();
        // Create admin person
        log.info("Creating data for admin.");
        personList.add(getNewPerson(true));
        // Create random person
        personList.addAll(getData());
        log.info("Saving all "+personList.size()+" data to database.");
        return personList;
    }

    public static List<Person> getData () {
        List<Person> personList = new ArrayList<>();
        // Create random person
        for (int i = 0; i < 10; i++) {
            Person person = getNewPerson();
            log.info("Creating data for random person #"+(i+1)+". ["+person.toString()+"]");
            //personList.add(getNewPerson(false));
            personList.add(person);
        }
        //log.info("Saving all "+personList.size()+" to database.");
        return personList;
    }

    static Person getNewPerson () {
        return getNewPerson(false);
    }
    static Person getNewPerson (boolean isAdmin) {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        return new Person(
                firstName,
                lastName,
                firstName.charAt(0) + "." + lastName + PersonService.TEXT_EMAIL_ADDRESS_FOR_PERSON,
                RandomStringUtils.random(10, true, true),
                ThreadLocalRandom.current().nextInt(1, 100 + 1),
                isAdmin
        );
    }

    public static List<Person> convertObjectToPerson(List<Object> objects){
        if(objects == null){
            return null;
        } else {
            List<Person> temp = new ArrayList<>();

            for(Object obj: objects){
                log.info(obj.toString());
            }
            return temp;
        }
    }

    public static List<Person> getAllWithoutPrivateData(List<Person> personList){
        List<Person> temp = new ArrayList<>();
        for(Person person: personList){
            if(!person.isAdmin()){
                temp.add(new Person(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAge()
                ));
            }
        }
        return temp;
    }
}
