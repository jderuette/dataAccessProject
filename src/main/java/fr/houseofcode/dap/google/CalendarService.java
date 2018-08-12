package fr.houseofcode.dap.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;

import fr.houseofcode.dap.Config;

/**
 * @author djer
 *
 */
public class CalendarService extends GoogleService {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    /**
     * Create a new CalendarService.
     * @param config the global application specific configuration
     */
    public CalendarService(final Config config) {
        super(config);
    }

    /**
     * Build a new Google Calendar Service.
     * @return the Google Calendar service
     * @throws GeneralSecurityException general Google security errors
     * @throws IOException              a general error (network, fileSystem, ...)
     */
    public Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final Calendar service = new Calendar.Builder(httpTransport, getGoogleJsonFactory(),
                getCredentials(httpTransport)).setApplicationName(getConfiguration().getApplicationName()).build();

        return service;
    }

    /**
     * Get the Google Events.
     * @param from      search events after this dataTime
     * @param maxResult maximum number of result to retrieve
     * @return a Google Events (containing found events)
     */
    public Events getEvents(final DateTime from, final Integer maxResult) {
        Events events = new Events();

        Calendar service;
        try {
            service = getCalendarService();
            events = service.events().list("primary").setMaxResults(maxResult).setTimeMin(from)
                    .setOrderBy("startTime").setSingleEvents(true).execute();
        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Error while trying to get Calendar remote service", e.getMessage());
        }

        return events;
    }

    /**
     * Retrieve the next Event in the user calendars.
     * @param user user Id or "me"
     * @return the next Google Event.
     */
    public Event getNextEvent(final String user) {

        Event nextEvent = null;
        final DateTime now = new DateTime(System.currentTimeMillis());
        final Events events = getEvents(now, 1);
        final List<Event> items = events.getItems();
        nextEvent = items.get(0);
        return nextEvent;
    }

    /**
     * Get the "status" for the Event for the current connected user.
     * @param event the event to search for status of current user.
     * @return A string representation of the user status
     */
    public String getMyStatus(final Event event) {
        String myStatus = "unknow";
        if (null != event) {
            final AppPeopleService peopleService = new AppPeopleService(getConfiguration());
            final String currentUser = peopleService.getCurrentConnectedUserEmail();
            myStatus = getstatus(event, currentUser);
        }
        return myStatus;
    }

    /**
     * Search the user (by Email) status in the event.
     * @param event     the event to scan
     * @param userEmail the user Email to search status for
     * @return the user status (or "unknow" if not found)
     */
    public static String getstatus(final Event event, final String userEmail) {
        String myStatus = "unknow";
        if (null != event.getAttendees() && event.getAttendees().size() > 0) {
            for (final EventAttendee attendee : event.getAttendees()) {
                if (attendee.getEmail().equals(userEmail)) {
                    myStatus = attendee.getResponseStatus();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(new StringBuilder().append("For Event : ").append(event.getSummary())
                                .append(" current conencted user (").append(userEmail)
                                .append(") is attendee and has status : ").append(myStatus).toString());
                    }
                    break;
                }
            }
        } else if (null != event.getOrganizer()) {
            if (event.getOrganizer().getEmail().equals(userEmail)) {
                myStatus = "Organizer";
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuilder().append("For Event : ").append(event.getSummary())
                            .append(" current conencted user (").append(userEmail).append(") is organizer")
                            .toString());
                }
            }
        }
        return myStatus;

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
