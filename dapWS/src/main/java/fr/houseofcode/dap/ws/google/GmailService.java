package fr.houseofcode.dap.ws.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;

import fr.houseofcode.dap.ws.Config;

/**
 * @author djer
 */
@Service
public class GmailService extends GoogleService {
    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** Maximum number of message (email) per page from Google Email Service. */
    private static final Long MAX_EMAIL_PER_PAGES = 1000L;

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    public static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY);

    /**
     * Create an new GMailService.
     * @param config the global application specific configuration
     */
    public GmailService(final Config config) {
        super(config);
    }

    /**
     * Build a new Gmail remote service.
     * @param user user ID
     * @return the Gmail Service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    public Gmail getGmailService(final String user) throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(httpTransport, getGoogleJsonFactory(), getCredentials(user))
                .setApplicationName(getConfiguration().getApplicationName()).build();

    }

    /**
     * Get a list of message (email) from Google.
     * @param user          The user ID used to store the credentials
     * @param nextPageToken The "next page token" received from the previous call.
     * @param labels        to filter message ("INBOX" = user main box, "UNREAD" =
     *                      unread messages, ...)
     * @return the listMessageResponse from the Google service;
     */
    public ListMessagesResponse getMessages(final String user, final String nextPageToken, final List<String> labels) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving emails with page token : " + nextPageToken);
        }
        ListMessagesResponse listResponse = null;
        try {
            final Gmail service = getGmailService(user);
            listResponse = service.users().messages().list("me").setLabelIds(labels).setIncludeSpamTrash(Boolean.FALSE)
                    .setPageToken(nextPageToken).setMaxResults(MAX_EMAIL_PER_PAGES).execute();
        } catch (IOException | GeneralSecurityException e) {
            LOG.error("Error while trying to get Gmail remote service", e);
        }

        return listResponse;
    }

    /**
     * retrieve all label from a Google Account.
     * @param user The user ID used to store the credentials
     * @return a string representation of all labels
     */
    public List<Label> getInboxLabels(final String user) {
        List<Label> labels = new ArrayList<Label>();

        try {
            final Gmail service = getGmailService(user);
            final ListLabelsResponse listResponse = service.users().labels().list("me").execute();
            labels = listResponse.getLabels();
        } catch (IOException | GeneralSecurityException e) {
            LOG.error("Error while trying to get Gmail remote service", e.getMessage());
        }

        return labels;
    }

    /**
     * Count the number of message from a Google MessageListResponse.
     * @param listMessagesResponse list of message from the Google Service
     * @return the number of message on this list.
     */
    public static Integer countNbMessage(final ListMessagesResponse listMessagesResponse) {
        int nbEmail = 0;
        if (null != listMessagesResponse) {
            nbEmail = listMessagesResponse.getMessages().size();
        }
        return nbEmail;
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
