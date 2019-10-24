package fr.housseofcode.dap.dap.cmd;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * display informations from Data Access Project.
 */
public final class AppLauncher {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /** the userId to display informations about. */
    private static String userId;
    /** action to perform, "display" or "add". **/
    private static String action = "display";

    /**
     * Main entry Point.
     */
    private AppLauncher() {
        throw new UnsupportedOperationException("Do not instatiate a Main Class ! ");
    }

    /**
     * Entry point.
     * @param args command line parameters
     */
    public static void main(final String[] args) {

        if (args.length > 0) {
            userId = args[0];
        }
        if (args.length > 1) {
            action = args[1];
        }

        if (null == userId) {
            System.err.println("An argument required, dap UserId");
            System.exit(0);
        }

        LOG.debug("Command Line userId : " + userId + ", action : " + action);

        if ("add".equals(action)) {
            final String url = DapClient.buildUrl("/account/add/" + userId, "");
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                LOG.error("URI cannot be created with URL : " + url, e);
                System.err.println("Cannot add Account (bad URI)");
            }
            try {
                if (null != uri) {
                    Desktop.getDesktop().browse(uri);
                }
            } catch (IOException e) {
                LOG.error("URI cannot be open : " + url, e);
                System.err.println("Cannot add Account (Cannot open Web browser)");
            }
            System.out.println("Account : " + userId + " added ! ");
        } else {
            System.out.println("Labels : " + DapClient.getDapData("/emails/labels", userId));
            System.out.println("Nb emails non lus : " + DapClient.getDapData("/emails/unread/count", userId));
            System.out.println("Prochains évènement : " + DapClient.getDapData("/events/next", userId));
        }
    }

}
