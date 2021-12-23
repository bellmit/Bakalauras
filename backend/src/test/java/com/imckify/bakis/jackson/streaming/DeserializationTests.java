package com.imckify.bakis.jackson.streaming;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class DeserializationTests {

    public static Employee createEmployee() {

        Employee emp = new Employee();
        emp.setId(100);
        emp.setName("David");
        emp.setPermanent(false);
        emp.setPhoneNumbers(new long[] { 123456, 987654 });
        emp.setRole("Manager");

        Address add = new Address();
        add.setCity("Bangalore");
        add.setStreet("BTM 1st Stage");
        add.setZipcode(560100);
        emp.setAddress(add);

        List<String> cities = new ArrayList<String>();
        cities.add("Los Angeles");
        cities.add("New York");
        emp.setCities(cities);

        Map<String, String> props = new HashMap<String, String>();
        props.put("salary", "1000 Rs");
        props.put("age", "28 years");
        emp.setProperties(props);

        return emp;
    }

    private final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("streaming.json")).getPath();
    private final File jsonFile = new File(path);


    @Test
    public void example() throws IOException {
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        Employee emp = objectMapper.readValue(jsonFile, Employee.class);

        System.out.println("Employee Object\n"+emp);

        //convert Object to json string
        Employee emp1 = createEmployee();
        //configure Object mapper for pretty print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //writing to console, can write to any output stream such as file
        StringWriter stringEmp = new StringWriter();
        objectMapper.writeValue(stringEmp, emp1);
        System.out.println("Employee JSON is\n"+stringEmp);
    }

    @Test
    public void exampleStreaming() throws IOException {
        //create JsonParser object
        JsonParser jsonParser = new JsonFactory().createParser(jsonFile);

        //loop through the tokens
        Employee emp = new Employee();
        Address address = new Address();
        emp.setAddress(address);
        emp.setCities(new ArrayList<String>());
        emp.setProperties(new HashMap<String, String>());
        List<Long> phoneNums = new ArrayList<Long>();
        boolean insidePropertiesObj=false;

        parseJSON(jsonParser, emp, phoneNums, insidePropertiesObj);

        long[] nums = new long[phoneNums.size()];
        int index = 0;
        for(Long l :phoneNums){
            nums[index++] = l;
        }
        emp.setPhoneNumbers(nums);

        jsonParser.close();
        //print employee object
        System.out.println("Employee Object\n\n"+emp);
    }

    private static void parseJSON(JsonParser jsonParser, Employee emp,
                                  List<Long> phoneNums, boolean insidePropertiesObj) throws JsonParseException, IOException {

        //loop through the JsonTokens
        while(jsonParser.nextToken() != JsonToken.END_OBJECT){
            String name = jsonParser.getCurrentName();
            if("id".equals(name)){
                jsonParser.nextToken();
                emp.setId(jsonParser.getIntValue());
            }else if("name".equals(name)){
                jsonParser.nextToken();
                emp.setName(jsonParser.getText());
            }else if("permanent".equals(name)){
                jsonParser.nextToken();
                emp.setPermanent(jsonParser.getBooleanValue());
            }else if("address".equals(name)){
                jsonParser.nextToken();
                //nested object, recursive call
                parseJSON(jsonParser, emp, phoneNums, insidePropertiesObj);
            }else if("street".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setStreet(jsonParser.getText());
            }else if("city".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setCity(jsonParser.getText());
            }else if("zipcode".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setZipcode(jsonParser.getIntValue());
            }else if("phoneNumbers".equals(name)){
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    phoneNums.add(jsonParser.getLongValue());
                }
            }else if("role".equals(name)){
                jsonParser.nextToken();
                emp.setRole(jsonParser.getText());
            }else if("cities".equals(name)){
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    emp.getCities().add(jsonParser.getText());
                }
            }else if("properties".equals(name)){
                jsonParser.nextToken();
                while(jsonParser.nextToken() != JsonToken.END_OBJECT){
                    String key = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    String value = jsonParser.getText();
                    emp.getProperties().put(key, value);
                }
            }
        }
    }
}
