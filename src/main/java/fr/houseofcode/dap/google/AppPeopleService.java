package fr.houseofcode.dap.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.people.v1.PeopleService;

import fr.houseofcode.dap.Config;

/**
 * @author djer
 *
 */
public class AppPeopleService extends GoogleService {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/plus.login ",
            "https://www.googleapis.com/auth/userinfo.email");

    /**
     * Create a new PeopleService.
     * @param config the global application specific configuration
     */
    public AppPeopleService(final Config config) {
        super(config);
    }

    /**
     * Build a new Google People Service.
     * @return the Google people service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    public PeopleService getPeopoleService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final PeopleService peopleService = new PeopleService.Builder(httpTransport, getGoogleJsonFactory(),
                getCredentials(httpTransport)).setApplicationName(getConfiguration().getApplicationName()).build();

        return peopleService;
    }

    /* (non-Javadoc)
     * @see fr.houseofcode.dap.google.GoogleService#getLog()
     */
    @Override
    protected Logger getLog() {
        return LOG;
    }

    /* (non-Javadoc)
     * @see fr.houseofcode.dap.google.GoogleService#getScopes()
     */
    @Override
    protected List<String> getScopes() {
        return SCOPES;
    }

}
