package fr.houseofcode.dapcmd;

import org.junit.Assert;
import org.junit.Test;

import fr.housseofcode.dap.dap.cmd.DapClient;

/**
 * @author house
 *
 */
public class DapClientTest {

    /**
     * Verify that the URLs to the Dap server are working well.
     */
    @Test
    public void testbuildURL() {
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "/a";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "test";
        final String res = DapClient.buildUrl(path, userId);

        Assert.assertEquals("url non construite", "http://localhost:8080/a?userId=test", res);

    }

    /**
     * Verify that the URLs to the Dap server are working well.
     */
    @Test
    public void testbuildURLInvalidPath() {
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "a";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "test";
        final String res = DapClient.buildUrl(path, userId);

        Assert.assertEquals("url non construite", "http://localhost:8080/a?userId=test", res);

    }

}
