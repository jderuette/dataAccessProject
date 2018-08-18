package fr.houseofcode.dap.admin;

/**
 * @author djer
 *
 */
public class Config {

    /** User to query in DaP WS. */
    private String user;

    /**
     * User specific configuration.
     * @param userId the userId to use to query dap WS
     */
    public Config(final String userId) {
        this.user = userId;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param newUser the user to set
     */
    public void setUser(final String newUser) {
        this.user = newUser;
    }
}
