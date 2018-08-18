package fr.houseofcode.dap.admin;

/**
 * @author djer
 *
 */
public final class Config {
    /** Singleton instance Holder. */
    private static Config instance;

    /** User to query in DaP WS. */
    private String user;

    /** Dap WebService URl. */
    private String dapUrl;

    /**
     * Singleton.
     */
    private Config() {
        super();
        dapUrl = "http://localhost:8080";
    }

    /**
     * get the Singleton instance.
     * @return the instance
     */
    public static Config get() {
        if (null == instance) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param newUser the user to set
     * @return this object (chain method call)
     */
    public Config setUser(final String newUser) {
        this.user = newUser;
        return this;
    }

    /**
     * @return the dapUrl
     */
    public String getDapUrl() {
        return dapUrl;
    }

    /**
     * @param newDapUrl the DaP Url to set
     * @return this object (chain method call)
     */
    public Config setDapUrl(final String newDapUrl) {
        this.dapUrl = newDapUrl;
        return this;
    }
}
