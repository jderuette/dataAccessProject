package fr.houseofcode.dap.ws.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author djer
 *
 */
@Service
public class UserService {

    /** user Data Access Object. */
    @Autowired
    private UserDao userDao;

    /**
     * Check if a user is an administrator.
     * @param userId the user "public" ID
     * @return True if user is administrator, false if not administrator or not
     *         exists
     */
    public Boolean isAdmin(final String userId) {
        Boolean isAdmin = Boolean.FALSE;
        final AppUserDTO user = userDao.getUser(userId);
        if (null != user) {
            isAdmin = user.getRole().equals(AppRole.ADMIN);
        }

        return isAdmin;
    }

    /**
     * Retrieve all app users.
     * @return A list of users
     */
    public List<AppUserDTO> getAllUsers() {
        return userDao.getAllUsers();
    }
}
