package fr.houseofcode.dap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.FileSystemNotFoundException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Person;

/**
 * @author djer
 */
public final class QuickStartSample {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();
    /**
     * Google application name.
     */
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    /**
     * JsonFactory to marshal/unMarshall Google messages.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Folder to Store DataAccesProject user Google credentials.
     */
    private static final String CREDENTIALS_FOLDER = System.getProperty("user.home") + "/houseOfCode"
            + System.getProperty("file.separator") + "dataAccessProject" + System.getProperty("file.separator")
            + "googleCredentials"; // Directory to store user credentials.

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY,
            CalendarScopes.CALENDAR_READONLY, "https://www.googleapis.com/auth/plus.login ",
            "https://www.googleapis.com/auth/userinfo.email");

    /**
     * Application credential file.
     */
    private static final String CLIENT_SECRET_FILE = System.getProperty("user.home") + "/houseOfCode"
            + System.getProperty("file.separator") + "dataAccessProject" + System.getProperty("file.separator")
            + "credentials.json";

    /**
     * Maximum number of message (email) per page from Google Email Service.
     */
    private static final Long MAX_EMAIL_PER_PAGES = 1000L;

    /**
     * Utility Class.
     */
    private QuickStartSample() {
        throw new UnsupportedOperationException("Utility Class");
    }
    /**
     * Creates an authorized Credential object.
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        Reader appClientSecret = null;
        final File appClientSecretFile = new File(CLIENT_SECRET_FILE);
        if (appClientSecretFile.exists()) {
            appClientSecret = new InputStreamReader(new FileInputStream(appClientSecretFile), Charset.forName("UTF-8"));
        } else {
            // try with app local data (not recommended to store this file in public
            // repository)
            final InputStream appClientSecretStream = QuickStartSample.class.getResourceAsStream(CLIENT_SECRET_FILE);
            if (null != appClientSecretStream) {
                appClientSecret = new InputStreamReader(appClientSecretStream, Charset.forName("UTF-8"));
            }
        }

        if (null == appClientSecret) {
            final String message = "No AppCredentialFile to connect to Google App. This file should be in : "
                    + CLIENT_SECRET_FILE;
            LOG.error(message);
            throw new FileSystemNotFoundException(message);
        }

        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                appClientSecret);

        // Build flow and trigger user authorization request.
        final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new File(CREDENTIALS_FOLDER)))
                        .setAccessType("offline").build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Build a new Gmail remote service.
     * @return the Gmail Service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    private static Gmail getGmailService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final Gmail service = new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME).build();

        return service;
    }

    /**
     * Build a new Google Calendar Service.
     * @return the Google Calendar service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    private static Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME).build();

        return service;
    }

    /**
     * Build a new Google People Service.
     * @return the Google people service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    private static PeopleService getPeopoleService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final PeopleService peopleService = new PeopleService.Builder(httpTransport, JSON_FACTORY,
                getCredentials(httpTransport)).setApplicationName(APPLICATION_NAME).build();

        return peopleService;
    }

    /**
     * The main entry Point (from Google Sample, should be refactored).
     * @param args application agrs
     * @throws IOException              a general error (network, fileSystem, ...)
     * @throws GeneralSecurityException general Google security errors
     */
    public static void main(final String... args) throws IOException, GeneralSecurityException {
        final String user = "me";

        userMessage(getInboxLabels(user));
        userMessage("Nb Emails : " + getNbUnreadEmail(user));

        userMessage("Prochain evennement : " + display(getNextEvent(user)));
    }

    /**
     * Retrieve the Number of Email for a user.
     * @param user The user ID or "me"
     * @return The number of unread email in his mail box
     */
    private static String getNbUnreadEmail(final String user) {
        return String.valueOf(getNbUnreadEmail(user, null));
    }

    /**
     * Retrieve the Number of Email for a user from a specific page Token.
     * @param user          The user ID or "me"
     * @param nextPageToken The "next page token" received from the previous call.
     *                      NULL for the first call.
     * @return The number of unread email in his mail box from this page. Recursive
     *         so ALL pages are traversed FROM nextPageToken.
     */
    private static Integer getNbUnreadEmail(final String user, final String nextPageToken) {
        Integer nbEmail = 0;
        ListMessagesResponse listMessagesResponse = getMessages(user, nextPageToken);

        if (null != listMessagesResponse) {
            nbEmail += countNbMessage(listMessagesResponse);

            while (null != listMessagesResponse.getNextPageToken()) {
                listMessagesResponse = getMessages(user, listMessagesResponse.getNextPageToken());
                nbEmail += countNbMessage(listMessagesResponse);
            }
        }

        return nbEmail;
    }

    /**
     * Count the number of message from a Google MessageListResponse.
     * @param listMessagesResponse list of message from the Google Service
     * @return the number of message on this list.
     */
    private static Integer countNbMessage(final ListMessagesResponse listMessagesResponse) {
        int nbEmail = 0;
        if (null != listMessagesResponse) {
            nbEmail = listMessagesResponse.getMessages().size();
        }
        return nbEmail;
    }

    /**
     * Get a list of message (email) from Google.
     * @param user          The user ID or "me"
     * @param nextPageToken The "next page token" received from the previous call.
     * @return the listMessageResponse from the Google service;
     */
    private static ListMessagesResponse getMessages(final String user, final String nextPageToken) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving emails with page token : " + nextPageToken);
        }
        ListMessagesResponse listResponse = null;
        try {
            final Gmail service = getGmailService();
            listResponse = service.users().messages().list(user).setLabelIds(Arrays.asList("INBOX", "UNREAD"))
                    .setIncludeSpamTrash(Boolean.FALSE).setPageToken(nextPageToken).setMaxResults(MAX_EMAIL_PER_PAGES)
                    .execute();
        } catch (IOException | GeneralSecurityException e) {
            LOG.error("Error while trying to get Gmail remote service", e.getMessage());
        }

        return listResponse;
    }

    /**
     * retrieve all label from a Google Account.
     * @param user The user ID or "me"
     * @return a string representation of all labels
     */
    private static String getInboxLabels(final String user) {
        final StringBuilder allLabels = new StringBuilder();

        List<Label> labels = new ArrayList<Label>();

        try {
            final Gmail service = getGmailService();
            final ListLabelsResponse listResponse = service.users().labels().list(user).execute();
            labels = listResponse.getLabels();
        } catch (IOException | GeneralSecurityException e) {
            LOG.error("Error while trying to get Gmail remote service", e.getMessage());
        }

        if (labels.isEmpty()) {
            allLabels.append("No labels found.");
        } else {
            allLabels.append("Labels:");
            for (final Label label : labels) {
                allLabels.append(String.format("- %s%n", label.getName()));
            }
        }

        return allLabels.toString();
    }

    /**
     * Display an event as simple string for user.
     * @param event The event to display
     * @return A simple string representation of the event
     */
    private static String display(final Event event) {
        String eventText = "No Event";
        if (null != event) {
            eventText = new StringBuilder().append(event.getSummary()).append("[" + event.getStart()).append("] ")
                    .append(getMyStatus(event)).toString();
        }
        return eventText;
    }

    /**
     * Get the "status" for the Event for the current connected user.
     * @param event the event to search for status of current user.
     * @return A string representation of the user status
     */
    private static String getMyStatus(final Event event) {
        String myStatus = "unknow";
        if (null != event) {
            final String currentUser = getCurrentConnectedUserEmail();
            if (null != event.getAttendees() && event.getAttendees().size() > 0) {
                for (final EventAttendee attendee : event.getAttendees()) {
                    if (attendee.getEmail().equals(currentUser)) {
                        myStatus = attendee.getResponseStatus();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(new StringBuilder().append("For Event : ").append(event.getSummary())
                                    .append(" current conencted user (")
                                    .append(currentUser).append(") is attendee and has status : ").append(myStatus)
                                    .toString());
                        }
                        break;
                    }
                }
            } else if (null != event.getOrganizer()) {
                if (event.getOrganizer().getEmail().equals(currentUser)) {
                    myStatus = "Organizer";
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(new StringBuilder().append("For Event : ").append(event.getSummary())
                                .append(" current conencted user (").append(currentUser).append(") is organizer")
                                .toString());
                    }
                }
            }
        }
        return myStatus;
    }

    /**
     * Try to get the email address of the current connected User.
     * @return Email address of the current connected user
     */
    private static String getCurrentConnectedUserEmail() {
        String userEmail = null;
        try {
            final PeopleService servcie = getPeopoleService();
            final Person profile = servcie.people().get("people/me").setPersonFields("emailAddresses").execute();

            final List<EmailAddress> emailsAdresses = profile.getEmailAddresses();
            if (null != emailsAdresses && emailsAdresses.size() > 0) {
                for (final EmailAddress email : emailsAdresses) {
                    if (null != email.getMetadata() && email.getMetadata().getPrimary()
                            || null != email.getType() && email.getType().equals("account")) {
                        userEmail = email.getValue();
                        break;
                    }
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Error while trying to get Peopole remote service", e.getMessage());
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("current connected account user email adress : " + userEmail);
        }

        return userEmail;
    }

    /**
     * Retrieve the next Event in the user calendars.
     * @param user user Id or "me"
     * @return the next Google Event.
     */
    private static Event getNextEvent(final String user) {
        Event nextEvent = null;
        final DateTime now = new DateTime(System.currentTimeMillis());

        Calendar service;
        try {
            service = getCalendarService();
            final Events events = service.events().list("primary").setMaxResults(1).setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true).execute();
            final List<Event> items = events.getItems();
            nextEvent = items.get(0);
        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Error while trying to get Calendar remote service", e.getMessage());
        }
        return nextEvent;
    }

    /**
     * Display a message to the user.
     * @param message Message to display to the user.
     */
    private static void userMessage(final String message) {
        System.out.println(message);
    }

}
