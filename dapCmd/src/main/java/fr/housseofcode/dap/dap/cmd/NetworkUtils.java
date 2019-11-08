/**
 * 
 */
package fr.housseofcode.dap.dap.cmd;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author djer1
 *
 */
public class NetworkUtils {

    public HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
