package com.nice.avishkar;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Basic test cases for candidates
 * */
public class CareConnectTests {

	private static Path attributeInfoFilePath = Paths.get("src/main/resources/AttributeInfo.csv");
	private static Path connectionsFilePath = Paths.get("src/main/resources/ExistingConnections.csv");
	private static Path personInfoFilePath = Paths.get("src/main/resources/MasterDataFeed.csv");
    
	private static Instant start;
	private static Instant finish;
	
    @BeforeClass
    public static void oneTimeSetUp() {
		start = Instant.now();
    }
    
    @AfterClass
    public static void oneTimeTearDown() {
    	finish = Instant.now();
    	long timeElapsed = Duration.between(start, finish).toMillis();
    	System.err.println("Execution took "+ timeElapsed + " millis");
    }
    
    @Test
    public void test1() {
    	CareConnect careConnect = new CareConnectImpl();
    	List<Suggestion> result = careConnect.getSuggestions("NCE00004", 2, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
		Assert.assertTrue(result.get(0).getId().equals("NCE00001"));
		Assert.assertTrue(result.size() == 1);
    }
    
    @Test
    public void test2() {
    	CareConnect careConnect = new CareConnectImpl();
    	List<Suggestion> result = careConnect.getSuggestions("NCE00001", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
    	Assert.assertTrue(result.get(0).getId().equals("NCE00005"));
    	Assert.assertTrue(result.get(1).getId().equals("NCE00004"));
    }
    
    @Test
    public void test3() {
    	CareConnect careConnect = new CareConnectImpl();
    	List<Suggestion> result = careConnect.getSuggestions("NCE00006", 2, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
    	Assert.assertTrue(result.size() == 0);
    }
    
    @Test
    public void test4() {
    	CareConnect careConnect = new CareConnectImpl();
    	List<Suggestion> result = careConnect.getSuggestions("NCE00003", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
    	Assert.assertTrue(result.get(0).getScore() == 20);
    	Assert.assertTrue(result.get(0).getName().equals("Johnny Mohr"));
    }
    
}
