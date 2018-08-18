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

import fr.houseofcode.dap.admin.Config;

/**
 * @author djer
 */
public class DapWSClient {
    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** Status code for a successful request. */
    public static final int STATUS_CODE_OK = 200;

    /** Root URL for DaP WS. */
    private String wsMainUrl;
    /** singleton instance. */
    private static DapWSClient instance;

    /** Default Dap Client. */
    public DapWSClient() {
        this.wsMainUrl = "http://localhost:8080";
    }

    /**
     * Singleton.
     * @return the only class instance
     */
    public static synchronized DapWSClient get() {
        if (null == instance) {
            instance = new DapWSClient();
        }
        return instance;
    }

    /**
     * Build a URL to DaP WS.
     * @param path   service specific path
     * @param userId the user ID account
     * @return a full URL
     */
    private String buildUrl(final String path, final String userId) {
        final StringBuilder builder = new StringBuilder();
        builder.append(wsMainUrl).append(path).append("?userId=").append(userId);
        return builder.toString();
    }

    /**
     * Get the next Event for "me".
     * @param userConfig The user Id used to store Account connection
     * @return a string representation of the next Event
     */
    public String getNextEvent(final Config userConfig) {
        return getText(buildUrl("/events/next", userConfig.getUser()));
    }

    /**
     * Retrieve main Box user message labels.
     * @param userConfig The user Id used to store Account connection
     * @return a String representation of the e-mails Labels
     */
    public String getEmailLabels(final Config userConfig) {
        return getText(buildUrl("/emails/labels", userConfig.getUser()));
    }

    /**
     * Retrieve the number of unread e-mail in User main Box.
     * @param userConfig The user Id used to store Account connection
     * @return the number of unread e-mails
     */
    public String getNbUnreadEmails(final Config userConfig) {
        return getText(buildUrl("/emails/unread/count", userConfig.getUser()));
    }

    /**
     * Build a URL to create a new Account.
     * @param accountName the account name
     * @return the string URL to open in a browser to create a new account
     */
    public String buildCreateAccounbtUrl(final String accountName) {
        return buildUrl("/account/add/" + accountName, accountName);
    }

    /**
     * Retrieve Data from DaP WS.
     * @param wsPath path to query
     * @return the data from DaP in JSON format
     */
    private String get(final String wsPath) {
        return get(wsPath, "application/json");
    }

    /**
     * Retrieve Data from DaP WS.
     * @param wsPath path to query
     * @return the data from DaP in text format
     */
    private String getText(final String wsPath) {
        return get(wsPath, "text/plain");
    }

    /**
     * Retrieve Data from DaP WS.
     * @param wsPath       path to query
     * @param acceptedData accepted data format for the response
     * @return the data from DaP
     */
    private String get(final String wsPath, final String acceptedData) {
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
                    conn.setRequestProperty("Accept", acceptedData);

                    if (conn.getResponseCode() != STATUS_CODE_OK) {
                        LOG.error("Bad HTTP Response on " + wsPath + " : " + conn.getResponseCode());
                        response.append("Error");
                        // throw new RuntimeException("Failed : HTTP error code : " +
                        // conn.getResponseCode());
                    } else {
                        final BufferedReader responseBufferedReader = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

                        String output;
                        while ((output = responseBufferedReader.readLine()) != null) {
                            response.append(output);
                        }
                        responseBufferedReader.close();
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    LOG.error("Error while calling WS on " + wsPath, e);
                    conn.disconnect();
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

    /**
     * @return the wsMainUrl
     */
    public String getWsMainUrl() {
        return wsMainUrl;
    }

    /**
     * @param newWsMainUrl the wsMainUrl to set
     * @return this object (chain method call)
     */
    public DapWSClient setWsMainUrl(final String newWsMainUrl) {
        this.wsMainUrl = newWsMainUrl;
        return this;
    }
}
