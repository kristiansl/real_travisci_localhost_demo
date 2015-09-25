package com.yourcompany;

import com.saucelabs.common.SauceOnDemandAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
  
/** 
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple browsers in parallel.
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher} which will invoke the Sauce REST API to mark
 * the test as passed or failed.
 *
 * @author Neil Manvar
 */
@RunWith(ConcurrentParameterized.class)
public class SampleSauceTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("kristianmeiersl", "69c9ea29-59c8-4b3a-9909-18b1b05343f6");

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    /**
     * Represents the browser to be used as part of the test run.
     */
    private String browserName;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    private String platformName;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    private String platformVersion;
    /**
     * Represents the deviceName of mobile device
     */
    private String deviceName;
    /**
     * Represents the device-orientation of mobile device
     */

    private String name;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    private String sessionId;

    /**
     * The {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private WebDriver driver;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

    public SampleSauceTest(String deviceName, String platformName, String platformVersion, String browserName, String name) {
        super();
        this.deviceName = deviceName;
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.browserName = browserName;
        this.name = name;
    }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"Samsung Galaxy S5 Device", "Android", "4.4", "Chrome", "Samsung Galaxy S5 Real name"});
        browsers.add(new String[]{"Samsung Galaxy S4 Device", "Android", "4.4", "Chrome", "Samsung Galaxy S4 Real name"});
        browsers.add(new String[]{"iPhone 6 Device", "iOS", "8.0", "Safari", "iPhone 6 Real name"});

 
        return browsers;
    } 
   
     
    /** 
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browserName != null) capabilities.setCapability("browserName", browserName);
        if (platformVersion != null) capabilities.setCapability("platformVersion", platformVersion);
        if (deviceName != null) capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("name", name);

        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                        "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
    } 
    
    /**
     * Runs a simple test verifying the title of the home page.
     * @throws Exception
     */
    
    @Test
    public void verifyTitleTest() throws Exception {
        driver.get("http://www.saucelabs.com");
        assertEquals("Sauce Labs: Selenium Testing, Mobile Testing, JS Unit Testing and More", driver.getTitle());
    }   
 
    /**
     * Runs a simple Authentication test 
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {
        driver.get("http://saucelabs.com/");
        WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hamburger")));
        driver.findElement(By.cssSelector(".hamburger")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//a[@title = 'Log in']")).click();
        Thread.sleep(5000);
        driver.findElement(By.id("username")).sendKeys("abpe17@gmail.com");
        driver.findElement(By.id("password")).sendKeys("shruti");
        driver.findElement(By.id("submit")).click();
        Thread.sleep(5000);
        
 
    } 

    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }
}