package fr.houseofcode.dap;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Person;

import fr.houseofcode.dap.google.AppPeopleService;
import fr.houseofcode.dap.google.CalendarService;
import fr.houseofcode.dap.google.GmailService;

/**
 * @author djer
 */
public class QuickStartSample {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Maximum number of message (email) per page from Google Email Service.
     */
    private static final Long MAX_EMAIL_PER_PAGES = 1000L;

    /** Application Configuration. */
    private Config configuration = new Config(
            System.getProperty("user.home") + System.getProperty("file.separator") + "houseOfCode"
                    + System.getProperty("file.separator") + "dataAccessProject");

    /**
     * Load with a default configuration.
     */
    public QuickStartSample() {

    }

    /**
     * Load with a App specific configuration.
     * @param config Application Configuration
     */
    public QuickStartSample(final Config config) {
        this.configuration = config;
    }

    /**
     * The main entry Point.
     * @param user Id of suer (stored credential ID)
     * @throws IOException              a general error (network, fileSystem, ...)
     * @throws GeneralSecurityException general Google security errors
     */
    public void display(final String user) throws IOException, GeneralSecurityException {
        userMessage(getInboxLabels(user));
        userMessage("Nb Emails : " + getNbUnreadEmail(user));

        userMessage("Prochain evennement : " + display(getNextEvent(user)));
    }

    /**
     * Retrieve the Number of Email for a user.
     * @param user The user ID or "me"
     * @return The number of unread email in his mail box
     */
    private String getNbUnreadEmail(final String user) {
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
    private Integer getNbUnreadEmail(final String user, final String nextPageToken) {
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
    private Integer countNbMessage(final ListMessagesResponse listMessagesResponse) {
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
    private ListMessagesResponse getMessages(final String user, final String nextPageToken) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving emails with page token : " + nextPageToken);
        }
        ListMessagesResponse listResponse = null;
        try {
            final Gmail service = new GmailService(configuration).getGmailService();
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
    private String getInboxLabels(final String user) {
        final StringBuilder allLabels = new StringBuilder();

        List<Label> labels = new ArrayList<Label>();

        try {
            final Gmail service = new GmailService(configuration).getGmailService();
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
    private String display(final Event event) {
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
    private String getMyStatus(final Event event) {
        String myStatus = "unknow";
        if (null != event) {
            final String currentUser = getCurrentConnectedUserEmail();
            if (null != event.getAttendees() && event.getAttendees().size() > 0) {
                for (final EventAttendee attendee : event.getAttendees()) {
                    if (attendee.getEmail().equals(currentUser)) {
                        myStatus = attendee.getResponseStatus();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(new StringBuilder().append("For Event : ").append(event.getSummary())
                                    .append(" current conencted user (").append(currentUser)
                                    .append(") is attendee and has status : ").append(myStatus).toString());
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
    private String getCurrentConnectedUserEmail() {
        String userEmail = null;
        try {
            final PeopleService servcie = new AppPeopleService(configuration).getPeopoleService();
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
    private Event getNextEvent(final String user) {
        Event nextEvent = null;
        final DateTime now = new DateTime(System.currentTimeMillis());

        Calendar service;
        try {
            service = new CalendarService(configuration).getCalendarService();
            final Events events = service.events().list("primary").setMaxResults(1).setTimeMin(now)
                    .setOrderBy("startTime").setSingleEvents(true).execute();
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
