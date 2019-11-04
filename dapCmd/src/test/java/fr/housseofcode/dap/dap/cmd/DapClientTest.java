package fr.housseofcode.dap.dap.cmd;

import org.junit.Assert;
import org.junit.Test;

import fr.housseofcode.dap.dap.cmd.mock.NetworkUtilsMock;

/**
 * @author djer1
 *
 */
public class DapClientTest {

    /**
     * Vérifie que les URLs vers le servers DaP Fonctionne bien.
     */
    @Test
    public void testBuildUrl() {
        //Init
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "/a";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "test";
        //Appel
        final String res = DapClient.buildUrl(path, userId);

        //Check
        Assert.assertEquals("Url mal construite", "http://localhost:8080/a?userId=test", res);
    }

    @Test
    public void testBuildUrl_invalidPath() {
        //Init
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "a";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "test";
        //Appel
        final String res = DapClient.buildUrl(path, userId);

        //Check
        Assert.assertEquals("Url mal construite", "http://localhost:8080/a?userId=test", res);
    }
    
    
    @Test
    public void testGetDapData_NbEmail() {
        //Init
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "/emails/unread/count";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "djer";
        //Appel
        DapClient.nu = new NetworkUtilsMock();
        final String res = DapClient.getDapData(path, userId);

        //Check
        Assert.assertEquals("Valeur invalide", "48", res);
    }
    
    @Test
    public void testGetDapData_nextEvent() {
        //Init
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "/event/next";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "djer";
        //Appel
        DapClient.nu = new NetworkUtilsMock();
        final String res = DapClient.getDapData(path, userId);

        //Check
        Assert.assertEquals("Valeur invalide", "Un truc dans pas longtemps", res);
    }

}
