/**
 * 
 */
package fr.houseofcode.dataAccessProject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 *
 */
public class QuickStartSample {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = System.getProperty("user.home")
            + "/houseOfCode" + System.getProperty("file.separator") + "dataAccessProject"
            + System.getProperty("file.separator") + "googleCredentials"; // Directory to store user credentials.

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY,
            CalendarScopes.CALENDAR_READONLY, "https://www.googleapis.com/auth/plus.login ",
            "https://www.googleapis.com/auth/userinfo.email");
            
    private static final String CLIENT_SECRET_FILE = "/data/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = QuickStartSample.class.getResourceAsStream(CLIENT_SECRET_FILE);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                        .setAccessType("offline").build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Build a new Gmail remote service
     * @return the Gmail Service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    private static Gmail getGmailService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();

        return service;
    }

    private static Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();

        return service;
    }

    private static PeopleService getPeopoleService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService peopleService = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();

        return peopleService;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        String user = "me";

        userMessage(getInboxLabels(user));
        userMessage("Nb Emails : " + getNbUnreadEmail(user));

        userMessage(display(getNextEvent(user)));
    }

    private static String getNbUnreadEmail(String user) {
        return String.valueOf(getNbUnreadEmail(user, null));
    }

    private static Integer getNbUnreadEmail(String user, String nextPageToken) {
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

    private static Integer countNbMessage(ListMessagesResponse listMessagesResponse) {
        int nbEmail = 0;
        if (null != listMessagesResponse) {
            nbEmail = listMessagesResponse.getMessages().size();
        }
        return nbEmail;
    }

    private static ListMessagesResponse getMessages(String user, String nextPageToken) {
        debug("Retrieving emails with page token : " + nextPageToken);
        ListMessagesResponse listResponse = null;
        try {
            Gmail service = getGmailService();
            listResponse = service.users().messages().list(user).setLabelIds(Arrays.asList("INBOX", "UNREAD"))
                    .setIncludeSpamTrash(Boolean.FALSE)
                    .setPageToken(nextPageToken).setMaxResults(1000l).execute();
        } catch (IOException ioe) {
            error("Error while trying to get Gmail remote service with message : " + ioe.getMessage());
        } catch (GeneralSecurityException gse) {
            error("Error while trying to get Gmail remote service with message : " + gse.getMessage());
        }

        return listResponse;
    }

    private static String getInboxLabels(String user) {
        StringBuilder allLabels = new StringBuilder();

        List<Label> labels = new ArrayList<Label>();

        try {
            Gmail service = getGmailService();
            ListLabelsResponse listResponse = service.users().labels().list(user).execute();
            labels = listResponse.getLabels();
        } catch (IOException ioe) {
            error("Error while trying to get Gmail remote service with message : " + ioe.getMessage());
        } catch (GeneralSecurityException gse) {
            error("Error while trying to get Gmail remote service with message : " + gse.getMessage());
        }
        
        if (labels.isEmpty()) {
            allLabels.append("No labels found.");
        } else {
            allLabels.append("Labels:");
            for (Label label : labels) {
                allLabels.append((String.format("- %s\n", label.getName())));
            }
        }
        
        return allLabels.toString();
    }

    private static String display(Event event) {
        String eventText = "No Event";
        if (null != event) {
            eventText = event.getSummary() + "[" + event.getStart() + "] " + getMyStatus(event);
        }
        return eventText;
    }

    private static String getMyStatus(Event event) {
        String myStatus = "unknow";
        if (null != event) {
            if (null != event.getAttendees() && event.getAttendees().size() > 0) {
                for (EventAttendee attendee : event.getAttendees()) {
                    if (attendee.getEmail().equals(getCurrentConnectedUserEmail())) {
                        myStatus = attendee.getResponseStatus();
                        break;
                    }
                }
            }
        }
        return myStatus;
    }

    private static String getCurrentConnectedUserEmail() {
        String userEmail = null;
        try {
            PeopleService servcie = getPeopoleService();
            Person profile = servcie.people().get("people/me").setPersonFields("emailAddresses").execute();

            List<EmailAddress> emailsAdresses = profile.getEmailAddresses();
            if (null != emailsAdresses && emailsAdresses.size() > 0) {
                for (EmailAddress email : emailsAdresses) {
                    if (null != email.getMetadata() && email.getMetadata().getPrimary()
                            || null != email.getType() && email.getType().equals("account")) {
                        userEmail = email.getValue();
                        break;
                    }
                }
            }
        } catch (GeneralSecurityException ioe) {
            error("Error while trying to get Peopole remote service with message : " + ioe.getMessage());
        } catch (IOException gse) {
            error("Error while trying to get Peopole remote service with message : " + gse.getMessage());
        }

        debug("current connected account user email adress : " + userEmail);

        return userEmail;
    }

    private static Event getNextEvent(String user) {
        Event nextEvent = null;
        DateTime now = new DateTime(System.currentTimeMillis());

        Calendar service;
        try {
            service = getCalendarService();
            Events events = service.events().list("primary").setMaxResults(1).setTimeMin(now).setOrderBy("startTime")
                    .setSingleEvents(true).execute();
            List<Event> items = events.getItems();
            nextEvent = items.get(0);
        } catch (GeneralSecurityException ioe) {
            error("Error while trying to get Calendar remote service with message : " + ioe.getMessage());
        } catch (IOException gse) {
            error("Error while trying to get Calendar remote service with message : " + gse.getMessage());
        }
        return nextEvent;
    }

    private static void debug(String message) {
        System.out.println("DEBUG : " + message);
    }

    private static void error(String message) {
        System.err.println("ERROR : " + message);
    }

    private static void userMessage(String message) {
        System.out.println(message);
    }

}
