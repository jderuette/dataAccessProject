package fr.houseofcode.dap.ws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.houseofcode.dap.ws.user.AppRole;
import fr.houseofcode.dap.ws.user.UserDao;

/**
 * @author djer
 */
@Service
public class DataInitializer {

    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** User management data. */
    @Autowired
    private UserDao userDao;

    /**
     * Initialize Data (default users, ...).
     */
    public void init() {
        LOG.info("Creating default admin user");
        userDao.createUser("admin", "admin", AppRole.ADMIN);
    }

}
