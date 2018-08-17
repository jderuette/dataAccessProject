/**
 * Class to store application, specific configuration;
 */
package fr.houseofcode.dap.ws;

import org.springframework.context.annotation.Configuration;

/**
 * @author djer
 */
@Configuration
public class Config {
    /** Default application name. */
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";

    /** Default folder to Store DataAccesProject user Google credentials. */
    private static final String CREDENTIALS_FOLDER = System.getProperty("user.home")
            + System.getProperty("file.separator") + "app_data" + System.getProperty("file.separator")
            + "googleCredentials";

    /** Default application credential file. */
    private static final String CLIENT_SECRET_FILE = System.getProperty("user.home")
            + System.getProperty("file.separator") + "app_data" + System.getProperty("file.separator")
            // + "credentials.json";
            + "client_id_server.json";

    private static final String OAUTH2_CALLBACK_URL = "/oAuth2Callback";

    /** Application name. */
    private String applicationName;
    /** Folder to Store user (Google) credentials. */
    private String credentialFolder;
    /** Application credential file. */
    private String clientSecretFile;
    /** OAuth2 Callback URL */
    private String oAuth2CallbackUrl;

    /**
     * Load the default configuration.
     */
    public Config() {
        super();
        applicationName = APPLICATION_NAME;
        credentialFolder = CREDENTIALS_FOLDER;
        clientSecretFile = CLIENT_SECRET_FILE;
        oAuth2CallbackUrl = OAUTH2_CALLBACK_URL;
    }

    /**
     * Load the configuration.
     * @param directoryDataStore folder to store configurations
     */
    public Config(final String directoryDataStore) {
        this();
        init(directoryDataStore);
    }

    /**
     * Load the configuration.
     * @param directoryDataStore folder to store configurations
     * @param appName            the specific application name
     */
    public Config(final String directoryDataStore, final String appName) {
        this();
        init(directoryDataStore, appName);
    }

    /**
     * Initialize the configuration with specific data Folder.
     * @param directoryDataStore root data Store
     */
    public void init(final String directoryDataStore) {
        this.credentialFolder = directoryDataStore + System.getProperty("file.separator") + "googleCredentials";
        this.clientSecretFile = directoryDataStore + System.getProperty("file.separator") + "credentials.json";
    }

    /**
     * Initialize the configuration with specific data Folder and an Application
     * name.
     * @param directoryDataStore root data Store
     * @param appName            application name
     */
    public void init(final String directoryDataStore, final String appName) {
        init(directoryDataStore);
        this.applicationName = appName;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @return the credentialFolder
     */
    public String getCredentialFolder() {
        return credentialFolder;
    }

    /**
     * @return the clientSecretFile
     */
    public String getClientSecretFile() {
        return clientSecretFile;
    }

    /**
     * @return the oAuth2CallbackUrl
     */
    public final String getoAuth2CallbackUrl() {
        return oAuth2CallbackUrl;
    }

}
