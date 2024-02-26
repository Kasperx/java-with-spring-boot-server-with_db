package com.example.demo.service;

import com.example.demo.controller.PersonController;
import com.example.demo.entity.Person;
import jakarta.persistence.Column;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonService {

    public static final String MESSAGE_ERROR_WRONG_PARAMETERS = "Wrong parameter for admin account.";
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
}
