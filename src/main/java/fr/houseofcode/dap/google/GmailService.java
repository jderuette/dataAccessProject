package fr.houseofcode.dap.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import fr.houseofcode.dap.Config;

/**
 * @author djer
 */
public class GmailService extends GoogleService {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY);

    /**
     * Create an new GMailService.
     * @param config the global application specific configuration
     */
    public GmailService(final Config config) {
        super(config);
    }

    /**
     * Build a new Gmail remote service.
     * @return the Gmail Service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    public Gmail getGmailService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final Gmail service = new Gmail.Builder(httpTransport, getGoogleJsonFactory(), getCredentials(httpTransport))
                .setApplicationName(getConfiguration().getApplicationName()).build();

        return service;
    }

    @Override
    protected Logger getLog() {
        return LOG;
    }

    @Override
    protected List<String> getScopes() {
        return SCOPES;
    }

}
