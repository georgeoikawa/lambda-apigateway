package com.amazonaws.lambda.demo;

import org.apache.commons.beanutils.BeanUtils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;

public class App {

    public static Object handleRequest(Request request, Context context) throws ResourceNotFoundException {

    	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
    	
    	DynamoDBMapper mapper = new DynamoDBMapper(client);
    	
    	Student student = null;
    	
    	switch(request.getHttpMethod()) {
    		
    		case "GET" :
    			student = mapper.load(Student.class, request.getId());
    			
    			if(student == null) {
    				throw new ResourceNotFoundException("Resource ID notfound " + request.getId());
    			}
    			
    			return student;
    		case "POST" :
    			student = request.getStudent();
    			
    			mapper.save(student);
    			
    			return student;
    			
    		case "PUT" :
    			student = mapper.load(Student.class, request.getStudent().getId());
    			
    			try {
    				BeanUtils.copyProperties(student, request.getStudent());
    			}catch (Exception e) {
					throw new ResourceNotFoundException("Error UPDATE " + e.getMessage());
				}
    			
    			mapper.save(student);
    			
    			return student;
    			
    			
    		case "DELETE" :
    			
    			student = mapper.load(Student.class, request.getId());
    				
    			mapper.delete(student);
    			
    			return "Success!";
    		default : 
    			
    			break;
    	}
    	
    	return null;
    }
}
