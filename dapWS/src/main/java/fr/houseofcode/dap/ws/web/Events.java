package fr.houseofcode.dap.ws.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;

import fr.houseofcode.dap.ws.GoogleFacade;

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
     * @param userId The user ID used to store the credentials
     * @return informations about he next Event
     */
    @RequestMapping(value = "/next/{userId}", consumes = "!"
            + MediaType.APPLICATION_JSON_UTF8_VALUE, produces = "text/plain")
    public String nextEvent(@PathVariable final String userId) {
        return googleFacade.display(userId, googleFacade.getNextEvent(userId));
    }

    /**
     * return a JSON representation of the next Event.
     * @param userId The user ID used to store the credentials
     * @return informations about he next Event
     */
    @RequestMapping(value = "/next/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Event nextEventJson(@PathVariable final String userId) {
        return googleFacade.getNextEvent(userId);
    }

}
