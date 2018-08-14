package fr.houseofcode.dap.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;

import fr.houseofcode.dap.GoogleFacade;

/**
 * @author djer
 */
@RestController
@RequestMapping("/events")
public class Events {
    /** Google facade. */
    @Autowired
    private GoogleFacade googleFacade;

    /**
     * DataAccesProjects Event component.
     */
    public Events() {
        super();
    }

    /**
     * Display the next Event.
     * @param requestedUser user Id or "me"
     * @return informations about he next Event
     */
    @RequestMapping(value = "/next/{requestedUser}", consumes = "!"+MediaType.APPLICATION_JSON_UTF8_VALUE, produces = "text/plain")
    public String nextEvent(@PathVariable final String requestedUser) {
        return googleFacade.display(googleFacade.getNextEvent(requestedUser));
    }

    /**
     * return a JSON representation of the next Event.
     * @param requestedUser user Id or "me"
     * @return informations about he next Event
     */
    @RequestMapping(value = "/next/{requestedUser}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Event nextEventJson(@PathVariable final String requestedUser) {
        return googleFacade.getNextEvent(requestedUser);
    }

}
