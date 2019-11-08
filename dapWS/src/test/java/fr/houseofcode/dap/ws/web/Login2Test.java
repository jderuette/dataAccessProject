package fr.houseofcode.dap.ws.web;

import org.junit.Test;

/**
 * @author house
 * Classe de test pour la connexion au moddle de Jérémie par un étudiant.
 *
 */
public class Login2Test extends SeleniumBaseClass {

    /**
     * Check student login.
     */
    @Test
    public void login2() {
        SeleniumDapHelper.login(getDriver());
    }
}
