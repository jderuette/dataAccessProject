package fr.houseofcode.dap.ws.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Person;

import fr.houseofcode.dap.ws.Config;

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
    public static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/plus.login ",
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
    public PeopleService getPeopoleService(final String user) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final PeopleService peopleService = new PeopleService.Builder(httpTransport, getGoogleJsonFactory(),
                getCredentials(user)).setApplicationName(getConfiguration().getApplicationName()).build();

        return peopleService;
    }

    /**
     * Retrieve the current connected User Person information.
     * @param user The user ID used to store the credentials
     * @return a Google Person
     */
    private Person getMe(final String user) {
        Person profile = new Person();
        try {
            final PeopleService servcie = getPeopoleService(user);
            profile = servcie.people().get("people/me").setPersonFields("emailAddresses").execute();
        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Error while trying to get Peopole remote service", e.getMessage());
        }

        return profile;
    }

    /**
     * Try to get the email address of the current connected User.
     * @param user The user ID used to store the credentials
     * @return Email address of the current connected user
     */
    public String getCurrentConnectedUserEmail(final String user) {
        String userEmail = null;
        final Person mySelf = getMe(user);

            final List<EmailAddress> emailsAdresses = mySelf.getEmailAddresses();
            if (null != emailsAdresses && emailsAdresses.size() > 0) {
                for (final EmailAddress email : emailsAdresses) {
                    if (null != email.getMetadata() && email.getMetadata().getPrimary()
                            || null != email.getType() && email.getType().equals("account")) {
                        userEmail = email.getValue();
                        break;
                    }
                }
            }
        if (LOG.isDebugEnabled()) {
            LOG.debug("current connected account user email adress : " + userEmail);
        }

        return userEmail;
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
