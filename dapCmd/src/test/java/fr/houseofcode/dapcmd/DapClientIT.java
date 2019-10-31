package fr.houseofcode.dapcmd;

import org.junit.Assert;
import org.junit.Test;

import fr.housseofcode.dap.dap.cmd.DapClient;

/**
 * @author house
 *
 */
public class DapClientIT {

    /**
     * Verify that the number of emails unread.
     */
    @Test
    public void testgetDapData() {
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String path = "/emails/unread/count";
        @SuppressWarnings("PMD.AvoidFinalLocalVariable")
        final String userId = "lydie";
        final String res = DapClient.getDapData(path, userId);

        Assert.assertNotEquals("la réponse ne devrait pas être vide", res);
        Assert.assertEquals("valeur invalide", "1", res);

    }
}
