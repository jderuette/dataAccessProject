package fr.houseofcode.dap.ws.web;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author house
 *
 */
public class SeleniumBaseClass {
    /**
     * temps de chargement par défaut.
     */
    private static final int DEFAULT_PAGE_LOAD_TIME = 5;
    /**
     * Webdriver.
     */
    private WebDriver driver;

    /**
     * Initialize Firefox.
     */
    @Before
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\house\\geckodriver\\geckodriver.exe");
        setDriver(new FirefoxDriver());
        driver.manage().timeouts().implicitlyWait(DEFAULT_PAGE_LOAD_TIME, TimeUnit.SECONDS);
    }

    /**
     * Close Firefox.
     */
    @After
    public void tearDown() {
        getDriver().quit();
    }

    /**
     * get le webdriver configuré.
     * @return le driver.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * set le webdriver configuré.
     * @param newdriver le driver.
     */
    public void setDriver(final WebDriver newdriver) {
        this.driver = newdriver;
    }
}
