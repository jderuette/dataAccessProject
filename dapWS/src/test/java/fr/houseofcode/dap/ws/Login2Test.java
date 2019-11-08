package fr.houseofcode.dap.ws;

import org.junit.Test;


/**
 * Log a user to Djer13-Moodle.
 * @author djer1
 *
 */
public class Login2Test extends SeleniumBaseClasse {

    /**
     * Check student Login.
     */
    @Test
    public void login2() {
        SeleniumDapHelper.login(super.getDriver());
    }

}
