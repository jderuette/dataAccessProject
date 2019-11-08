/**
 * 
 */
package fr.houseofcode.dap.ws;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author djer1
 *
 */
public class SeleniumBaseClasse {

    private static final int DEFAULT_PAGE_LOAD_TIME = 5;
    /** A WebDrver. */
    private WebDriver driver;

    /**
     * @return the driver
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Initialize FireFox.
     */
    @Before
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "D:\\program\\geckoDriver_026\\geckodriver.exe");
        setDriver(new FirefoxDriver());
        driver.manage().timeouts().implicitlyWait(DEFAULT_PAGE_LOAD_TIME, TimeUnit.SECONDS);
    }

    /**
     * @param newDriver the driver to set
     */
    protected void setDriver(final WebDriver newDriver) {
        this.driver = newDriver;
    }

    /**
     * Close FireFox.
     */
    @After
    public void tearDown() {
        getDriver().quit();
    }

}
