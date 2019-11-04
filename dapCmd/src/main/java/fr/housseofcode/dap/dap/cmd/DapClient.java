package fr.housseofcode.dap.dap.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author djer1
 *
 */
public class DapClient {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /** Status code for a successful request. */
    private static final int STATUS_CODE_OK = 200;

    /** Default Dap WS URl. */
    private static final String DEFAULT_WS_URL = "http://localhost:8080";
    /** allow network connections */
    static NetworkUtils nu = new NetworkUtils();

    /**
     * Retrieve Data from the DaP server.
     * @param path Path to query
     * @param userId the userId
     * @return the server response
     */
    public static String getDapData(final String path, final String userId) {
        return get(buildUrl(path, userId));
    }

    /**
     * Build a URL to DaP WS.
     * @param path   service specific path
     * @param userId the userId
     * @return a full URL
     */
    public static String buildUrl(final String path, final String userId) {
        final StringBuilder builder = new StringBuilder();

        builder.append(DEFAULT_WS_URL);

        if (path.startsWith("/")) {
            builder.append(path);
        } else {
            builder.append("/").append(path);
        }

        builder.append("?userId=").append(userId);
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

            //final URL url = new URL("http://localhost:8080/email/unread/count");

            final HttpURLConnection conn = nu.createConnection(url);
            //final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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
        //return "48";
    }

}
