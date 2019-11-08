package fr.houseofcode.dap.ws;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * @author djer1
 *
 */
public final class SeleniumDapHelper {

    /**
     * Default browser window weight.
     */
    private static final int WINDOW_HEIGHT_DEFAULT = 687;
    /**
     * Default browser window height.
     */
    private static final int WINDOW_WHEIGT_DEFAULT = 1280;

    /**
     * Hide default constructor.
     */
    private SeleniumDapHelper() {

    }

    /**
     * Log user on Djer13-Moodle.
     * @param driver A configured WebDriver
     */
    public static void login(final WebDriver driver) {
        driver.get("http://djer13-moodle.clicketcloud.com/my/");
        driver.manage().window().setSize(new Dimension(WINDOW_WHEIGT_DEFAULT, WINDOW_HEIGHT_DEFAULT));
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("etudtest");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("PassEtud55:");
        driver.findElement(By.xpath("//section/div/div[2]")).click();
        driver.findElement(By.id("loginbtn")).click();
    }

}
