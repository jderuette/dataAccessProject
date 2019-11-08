package fr.houseofcode.dap.ws.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * @author house
 *
 */
public final class SeleniumDapHelper {

    /**
     * constant weight for window dimensions.
     */
    private static final int WINDOW_WEIGHT_DEFAULT = 968;
    /**
     * contant height for window dimensions.
     */
    private static final int WINDOW_HEIGHT_DEFAULT = 760;

    /**
     * Hide default constructor.
     */
    private SeleniumDapHelper() {

    }

    /**
     * Connection method.
     * @param driver webdriver configuré.
     */
    public static void login(final WebDriver driver) {
        driver.get("http://djer13-moodle.clicketcloud.com/my/");
        driver.findElement(By.cssSelector(".col-md-5:nth-child(1)")).click();
        driver.findElement(By.id("username")).sendKeys(" etudtest");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("PassEtud55:");
        driver.findElement(By.id("loginbtn")).click();
        driver.manage().window().setSize(new Dimension(WINDOW_WEIGHT_DEFAULT, WINDOW_HEIGHT_DEFAULT));
    }

}
