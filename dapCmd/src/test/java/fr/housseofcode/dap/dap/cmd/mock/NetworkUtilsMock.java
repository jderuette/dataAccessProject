package fr.housseofcode.dap.dap.cmd.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;
import java.nio.charset.Charset;

import fr.housseofcode.dap.dap.cmd.NetworkUtils;

/**
 * @author djer1
 *
 */
public class NetworkUtilsMock extends NetworkUtils {

    public HttpURLConnection createConnection(URL url) throws IOException {
        return new HttpURLConnection(url) {

            public InputStream getInputStream() throws IOException {

                InputStream datas;
                if (url.getPath().contains("/email")) {
                    datas = NetworkUtilsMock.class.getResourceAsStream("/NbUnreadEmails1.data");
                } else {
                    datas = NetworkUtilsMock.class.getResourceAsStream("/NextEvent1.data");
                }
                return datas;
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public void connect() throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean usingProxy() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void disconnect() {
                // TODO Auto-generated method stub

            }
        };
    }
}
