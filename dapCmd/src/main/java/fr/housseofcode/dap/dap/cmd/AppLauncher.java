package fr.housseofcode.dap.dap.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    /** Status code for a successful request. */
    public static final int STATUS_CODE_OK = 200;

    /** Default Dap WS URl. */
    public static final String DEFAULT_WS_URL = "http://localhost:8080";
    /** the userId to display informations about. */
    private static String userId;

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
        } else {
            System.err.println("An argument required, dap UserId");
            System.exit(0);
        }

        System.out.println("Labels : " + get(buildUrl("/emails/labels", userId)));
        System.out.println("Nb emails non lus : " + get(buildUrl("/emails/unread/count", userId)));
        System.out.println("Prochains évènement : " + get(buildUrl("/events/next", userId)));
    }

    /**
     * Build a URL to DaP WS.
     * @param path   service specific path
     * @param userId the user ID account
     * @return a full URL
     */
    private static String buildUrl(final String path, final String userId) {
        final StringBuilder builder = new StringBuilder();
        builder.append(DEFAULT_WS_URL).append(path).append("?userId=").append(userId);
        return builder.toString();
    }

    /**
     * Retrieve data from DaP server.
     * @param wsPath path to query
     * @return The response body from the server
     */
    private static String get(final String wsPath) {
        final StringBuilder response = new StringBuilder();
        try {
            final URL url = new URL(wsPath);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/plain");

            if (conn.getResponseCode() == STATUS_CODE_OK) {
                final BufferedReader responseBufferedReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                String output;
                while ((output = responseBufferedReader.readLine()) != null) {
                    response.append(output);
                }
                conn.disconnect();
            } else {
                LOG.error("Bad HTTP Response for : " + wsPath + " : " + conn.getResponseCode());
            }

        } catch (IOException e) {
            LOG.error("Error while calling WS", e);
        }

        return response.toString();
    }
}
