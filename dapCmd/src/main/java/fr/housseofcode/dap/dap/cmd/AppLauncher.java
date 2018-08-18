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

    /**
     * Main entry Point.
     */
    private AppLauncher() {
        throw new UnsupportedOperationException("Don not instatiate a Main Class ! ");
    }

    /**
     * Entry point.
     * @param args command line parameters
     */
    public static void main(final String[] args) {

        System.out.println("Labels : " + get(DEFAULT_WS_URL + "/emails/labels/me"));
        System.out.println("Nb emails non lus : " + get(DEFAULT_WS_URL + "/emails/unread/count/me"));
        System.out.println("Prochains évènement : " + get(DEFAULT_WS_URL + "/events/next/me"));
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
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != STATUS_CODE_OK) {
                LOG.error("Bad HTTP Response : " + conn.getResponseCode());
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            final BufferedReader responseBufferedReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = responseBufferedReader.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

        } catch (IOException e) {
            LOG.error("Error while calling WS", e);
        }

        return response.toString();
    }
}
