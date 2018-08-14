package fr.housseofcode.dap.dap.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * display informations from Data Access Project
 */
public class AppLauncher {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    public static void main(final String[] args) {
        String wsUrl = "http://localhost:8080/";

        System.out.println("Labels : " + get(wsUrl + "/emails/labels/me"));
        System.out.println("Nb emails non lus : " + get(wsUrl + "/emails/unread/count/me"));
        System.out.println("Prochains évènement : " + get(wsUrl + "/events/next/me"));
    }

    private static String get(final String wsFullUrl) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(wsFullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                LOG.error("Bad HTTP Response : " + conn.getResponseCode());
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            LOG.error("Bad URL for WS ", e);
        } catch (IOException e) {
            LOG.error("IOException from WS ", e);
        }

        return response.toString();
    }
}
