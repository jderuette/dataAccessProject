package fr.houseofcode.dap.ws;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListMessagesResponse;

import fr.houseofcode.dap.ws.google.CalendarService;
import fr.houseofcode.dap.ws.google.GmailService;

/**
 * @author djer
 */
@Service
public class GoogleFacade {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /** Application Configuration. */
    @Autowired
    private Config configuration;

    /**
     * Load with a default configuration.
     */
    public GoogleFacade() {
        this.configuration = new Config();
    }

    /**
     * Load with a App specific configuration.
     * @param config Application Configuration
     */
    public GoogleFacade(final Config config) {
        this.configuration = config;
    }

    /**
     * Display all synthetic information.
     * @param user The user ID or "me"
     * @throws IOException              a general error (network, fileSystem, ...)
     * @throws GeneralSecurityException general Google security errors
     */
    public void display(final String user) throws IOException, GeneralSecurityException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Displaying information for user ID : " + user);
        }
        userMessage(buildInboxLabels(user));
        userMessage("Nb Emails : " + getNbUnreadEmail(user));

        userMessage("Prochain evennement : " + display(getNextEvent(user)));
    }

    /**
     * Retrieve the Number of Email for a user.
     * @param user The user ID or "me"
     * @return The number of unread email in his mail box
     */
    public String getNbUnreadEmail(final String user) {
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
        final GmailService service = new GmailService(configuration);
        ListMessagesResponse listMessagesResponse = service.getMessages(user, nextPageToken,
                Arrays.asList("INBOX", "UNREAD"));

        if (null != listMessagesResponse) {
            nbEmail += GmailService.countNbMessage(listMessagesResponse);

            while (null != listMessagesResponse.getNextPageToken()) {
                listMessagesResponse = service.getMessages(user, listMessagesResponse.getNextPageToken(),
                        Arrays.asList("INBOX", "UNREAD"));
                nbEmail += GmailService.countNbMessage(listMessagesResponse);
            }
        }

        return nbEmail;
    }

    /**
     * retrieve all label from a Google Account.
     * @param user The user ID or "me"
     * @return a string representation of all labels
     */
    public String buildInboxLabels(final String user) {
        final StringBuilder allLabels = new StringBuilder();
        final GmailService service = new GmailService(configuration);

        final List<Label> labels = service.getInboxLabels(user);

        if (null == labels) {
            allLabels.append("No labels found.");
        } else if (labels.isEmpty()) {
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
    public String display(final Event event) {
        String eventText = "No Event";
        if (null != event) {
            final CalendarService service = new CalendarService(configuration);
            final String myStatus = service.getMyStatus(event);
            eventText = new StringBuilder().append(event.getSummary()).append("[" + event.getStart()).append("] ")
                    .append(myStatus).toString();
        }
        return eventText;
    }

    /**
     * Retrieve the next Event in the user calendars.
     * @param user user Id or "me"
     * @return the next Google Event.
     */
    public Event getNextEvent(final String user) {
        final CalendarService service = new CalendarService(configuration);
        return service.getNextEvent(user);
    }

    /**
     * Display a message to the user.
     * @param message Message to display to the user.
     */
    private static void userMessage(final String message) {
        System.out.println(message);
    }

    /**
     * @param config the configuration to set
     */
    public final void setConfiguration(final Config config) {
        this.configuration = config;
    }
}
