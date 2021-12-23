package com.imckify.bakis.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 *  https://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial
 */
public class JsonExampleDeserializationTests {

    public Employee createEmployee() {

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

    private void parseJSON(JsonParser jsonParser, Employee emp,
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

    public static class Employee {

        private int id;
        private String name;
        private boolean permanent;
        private Address address;
        private long[] phoneNumbers;
        private String role;
        private List<String> cities;
        private Map<String, String> properties;

        public Employee() {
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isPermanent() {
            return permanent;
        }
        public void setPermanent(boolean permanent) {
            this.permanent = permanent;
        }
        public Address getAddress() {
            return address;
        }
        public void setAddress(Address address) {
            this.address = address;
        }
        public long[] getPhoneNumbers() {
            return phoneNumbers;
        }
        public void setPhoneNumbers(long[] phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }
        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("***** Employee Details *****\n");
            sb.append("ID="+getId()+"\n");
            sb.append("Name="+getName()+"\n");
            sb.append("Permanent="+isPermanent()+"\n");
            sb.append("Role="+getRole()+"\n");
            sb.append("Phone Numbers="+ Arrays.toString(getPhoneNumbers())+"\n");
            sb.append("Address="+getAddress()+"\n");
            sb.append("Cities="+Arrays.toString(getCities().toArray())+"\n");
            sb.append("Properties="+getProperties()+"\n");
            sb.append("*****************************");

            return sb.toString();
        }
        public List<String> getCities() {
            return cities;
        }
        public void setCities(List<String> cities) {
            this.cities = cities;
        }
        public Map<String, String> getProperties() {
            return properties;
        }
        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }

    public static class Address {
        private String street;
        private String city;
        private int zipcode;

        public Address() {
        }

        public Address(String street, String city, int zipcode) {
            this.street = street;
            this.city = city;
            this.zipcode = zipcode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getZipcode() {
            return zipcode;
        }

        public void setZipcode(int zipcode) {
            this.zipcode = zipcode;
        }

        @Override
        public String toString() {
            return getStreet() + ", " + getCity() + ", " + getZipcode();
        }
    }
}
