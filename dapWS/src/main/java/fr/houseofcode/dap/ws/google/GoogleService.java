package fr.houseofcode.dap.ws.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import fr.houseofcode.dap.ws.Config;
import fr.houseofcode.dap.ws.GoogleFacade;

/**
 * @author djer
 *
 */
public abstract class GoogleService {
    /** JsonFactory to marshal/unMarshall Google messages. */
    private final JsonFactory googleJsonFactory = JacksonFactory.getDefaultInstance();

    /** Application Configuration. */
    private final Config configuration;

    /** Google Authorization Flow. */
    private GoogleAuthorizationCodeFlow flow;

    /**
     * Prepare a Google Service.
     * @param config configuration to pickup App specific configuration (credential
     *               folder, application name, JSON App credential file, ...)
     */
    public GoogleService(final Config config) {
        this.configuration = config;
    }

    /**
     * Google specific Logger.
     * @return a Log4J Logger
     */
    protected abstract Logger getLog();

    /**
     * Google specifics services scopes.
     * @return the list of required scopes
     */
    protected abstract List<String> getScopes();

    /**
     * Creates an authorized Credential object.
     * @param userId the App User ID
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */
    public Credential getCredentials(final String userId) throws IOException {
        return getFlow().loadCredential(userId);

        // installed App code
        // return new AuthorizationCodeInstalledApp(flow, new
        // LocalServerReceiver()).authorize("user");
    }

    /**
     * Retrieve all users Tokens.
     * @return A Map, userid as key, and token data as Value see : @see DataStore
     */
    public DataStore<StoredCredential> getAllCredentials() {
        DataStore<StoredCredential> response = null;

        try {
            response = getFlow().getCredentialDataStore();
        } catch (IOException ioe) {
            getLog().error("Error while trying to load Google Flow", ioe);
            // fake DataStore to implements Null Safe Pattern
            try {
                response = MemoryDataStoreFactory.getDefaultInstance().getDataStore("fake");
            } catch (IOException ioe2) {
                // No Null Safe :-(
                getLog().error("Cannot create a fake memoryDataStore", ioe2);
            }
        }

        return response;
//        Iterator<String> itUsersKey = getFlow().getCredentialDataStore().keySet().iterator();
//        while(itUsersKey.hasNext()) {
//            String userKey = itUsersKey.next();
//            StoredCredential userData = getFlow().getCredentialDataStore().get(userKey);
//        }

    }

    /**
     * Initialize Google flow (if not already initialized) and return it.
     * @return a Google Authorization Flow
     * @throws IOException if Google Error occurs
     */
    protected GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (null == flow) {
                flow = initializeFlow();
        }
        return flow;
    }

    /**
     * Initialize a Google Flow.
     * @return the Google Flow
     * @throws IOException if Google Error occurs
     */
    public GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
        // Load client secrets.
        Reader appClientSecret = null;
        final File appClientSecretFile = new File(configuration.getClientSecretFile());
        if (appClientSecretFile.exists()) {
            appClientSecret = new InputStreamReader(new FileInputStream(appClientSecretFile), Charset.forName("UTF-8"));
        } else {
            // try with app local data (not recommended to store this file in public
            // repository)
            final InputStream appClientSecretStream = GoogleFacade.class
                    .getResourceAsStream(configuration.getClientSecretFile());
            if (null != appClientSecretStream) {
                appClientSecret = new InputStreamReader(appClientSecretStream, Charset.forName("UTF-8"));
            }
        }

        if (null == appClientSecret) {
            final String message = "No AppCredentialFile to connect to Google App. This file should be in : "
                    + configuration.getClientSecretFile();
            getLog().error(message);
            throw new FileSystemNotFoundException(message);
        }

        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(getGoogleJsonFactory(), appClientSecret);

        // Build flow and trigger user authorization request.
        final GoogleAuthorizationCodeFlow newFlow = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
                getGoogleJsonFactory(), clientSecrets, getScopes())
                        .setDataStoreFactory(new FileDataStoreFactory(new File(configuration.getCredentialFolder())))
                        .setAccessType("offline").build();

        return newFlow;
    }

    /**
     * @return the googleJsonFactory
     */
    protected JsonFactory getGoogleJsonFactory() {
        return googleJsonFactory;
    }

    /**
     * @return the configuration
     */
    public final Config getConfiguration() {
        return configuration;
    }

}
