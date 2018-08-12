package fr.houseofcode.dap.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

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
