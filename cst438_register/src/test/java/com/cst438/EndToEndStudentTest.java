package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 */

@SpringBootTest
public class EndToEndStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "/opt/WebDriver/bin/chromedriver";

	public static final String URL = "https://cst438register-fe-jordancruz.herokuapp.com/";

	public static final String TEST_USER_EMAIL = "tsunamiJ@csumb.edu";

	public static final String TEST_USER_NAME = "Johnny Tsunami";

	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Autowired
	StudentRepository studentRepository;

	@Test
	public void addCourseTest() throws Exception {

		Student x = studentRepository.findByEmail(TEST_USER_EMAIL);
		while(x != null) {
			studentRepository.delete(x);
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
		}
		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// select the last button, set to be student
			WebElement we = driver.findElement(By.xpath("(//a[@role='button'])[last()]"));
			we.click();

			// Locate "Add student" button and click
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Enter test name/email and click "Add"
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[span='Add']")).click();
			Thread.sleep(SLEEP_DURATION);

			// Verify there is new student
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			we = driver.findElement(By.xpath("//div[@data-field='name' and @data-value='" + student.getName() + "']"));
			assertNotNull(we, "Confirm, new student added.");

		} catch (Exception ex) {
			throw ex;
		} finally {
			//Clean db
			Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
			if(s != null) {
				studentRepository.delete(s);
			}
			driver.quit();
		}
	}
}