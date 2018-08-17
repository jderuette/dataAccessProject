/**
 * 
 */
package fr.houseofcode.dap.admin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author djer
 */
public class DapWSClient {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    private String wsMainUrl;
    private static DapWSClient instance;

    /**
     * 
     */
    public DapWSClient() {
        this.wsMainUrl = "http://localhost:8080";
    }

    public synchronized static DapWSClient get() {
        if (null == instance) {
            instance = new DapWSClient();
        }
        return instance;
    }

    /**
     * Get the next Event for "me".
     * @return a string representation of the next Event
     */
    public String getNextEvent() {
        return getNextEvent("me");
    }

    /**
     * Get the next Event for "me".
     * @return a string representation of the next Event
     */
    public String getNextEvent(final String user) {
        return get(wsMainUrl + "/events/next/" + user);
    }

    public String getEmailLabels() {
        return getEmailLabels("me");
    }

    private String getEmailLabels(final String user) {
        return get(wsMainUrl + "/emails/labels/" + user);
    }

    public String getNbUnreadEmails() {
        return getNbUnreadEmails("me");
    }

    private String getNbUnreadEmails(final String user) {
        return get(wsMainUrl + "/emails/unread/count/" + user);
    }

    private String get(final String wsPath) {
        String response = null;
        final Callable<String> externaleCall = new Callable<String>() {

            @Override
            public String call() {
                final StringBuilder response = new StringBuilder();
                HttpURLConnection conn = null;
                try {
                    final URL url = new URL(wsPath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        LOG.error("Bad HTTP Response on " + wsPath + " : " + conn.getResponseCode());
                        // throw new RuntimeException("Failed : HTTP error code : " +
                        // conn.getResponseCode());
                    }

                    final BufferedReader responseBufferedReader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

                    String output;
                    while ((output = responseBufferedReader.readLine()) != null) {
                        response.append(output);
                    }
                    responseBufferedReader.close();
                    conn.disconnect();
                } catch (IOException e) {
                    LOG.error("Error while calling WS on " + wsPath, e);
                    if (null != conn) {
                        try {
                            conn.getInputStream().close();
                        } catch (IOException e1) {
                            LOG.error("WS connection cannot be (force) closed on " + wsPath, e1);
                        }
                        conn.disconnect();
                    }
                    response.append("Error");
                }
                return response.toString();
            }
        };

        final FutureTask<String> futureTask = new FutureTask<>(externaleCall);
        final Thread newTread = new Thread(futureTask);
        newTread.start();

        try {
            response = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while calling WS : " + wsPath, e);
        }

        return response;
    }

}
