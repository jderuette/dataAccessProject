package fr.houseofcode.dap.ws.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author djer
 *
 */
@Service
public class UserDao {
    /** internal userDataStore. */
    private List<AppUserDTO> users;

    /**
     * Default constructor. initialize the users list.
     */
    public UserDao() {
        users = new ArrayList<>();
    }

    /**
     * Search for a user by ID.
     * @param userId the user Id to search for
     * @return A AppUser instance, or null if none found
     */
    public AppUserDTO getUser(final String userId) {
        AppUserDTO response = null;

        if (null != users && users.size() > 0) {
            for (final AppUserDTO user : users) {
                if (user.getUserId().equals(userId)) {
                    response = user;
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Create and store the user. If user already exist, no data are changed.
     * @param userId   The "public" user Id.
     * @param password The user Password
     * @param role     The user Role
     * @return a AppUserDto instance
     */
    public AppUserDTO createUser(final String userId, final String password, final AppRole role) {
        AppUserDTO appUserDto = null;
        if (null == getUser(userId)) {
            appUserDto = new AppUserDTO(userId, password, role);
            users.add(appUserDto);
        }

        return appUserDto;
    }

    /**
     * Retrieve all users.
     * @return a A list of users
     */
    public List<AppUserDTO> getAllUsers() {
        return users;
    }

}
