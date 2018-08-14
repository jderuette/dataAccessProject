package fr.houseofcode.dap.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author djer
 *
 */
@RestController
public class Index {

    /**
     * Default constructor.
     */
    public Index() {
        super();
    }

    /**
     * A basic Controller to test.
     * @return an hello String
     */
    @RequestMapping("/")
    public String index() {
        return "Bonjour et bienvenu dans votre application de gestion et centralisation de vos données";
    }

}
