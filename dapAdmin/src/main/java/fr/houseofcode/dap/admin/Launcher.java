package fr.houseofcode.dap.admin;
/**
 * Data Access Project Launcher.
 */

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.houseofcode.dap.admin.service.DapWSClient;
import fr.houseofcode.dap.admin.view.AccountToCreate;
import fr.houseofcode.dap.admin.view.MainWindow;

/**
 * Launcher for the DataAccessProject admin tool.
 * @author djer
 */
public final class Launcher {

    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** The main Windows. */
    private static MainWindow mainWindow;

    /** User Configuration. */
    private static Config configuration;

    /**
     * Utility CLass.
     */
    private Launcher() {
        LOG.error("Cannot instntiate Launcher");
        throw new UnsupportedOperationException("Launcher is an EntryPoint (Utility Class) not a business Class");
    }

    /**
     * Program entry point.
     * @param args command line parameters.
     */
    public static void main(final String[] args) {
        configuration = Config.get().setUser("djer13");
        DapWSClient.get().setWsMainUrl("http://localhost:8080");
        mainWindow = new MainWindow("House of Code - Data Access Project");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateEvents();
                updateEmails();
            }
        });

        mainWindow.addRefreshCallback(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                updateEvents();
            }
        });

        mainWindow.addRefreshCallback(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                updateEmails();
            }
        });

        mainWindow.addCreateAccountListner(new AccountToCreate() {
            @Override
            public void accountToAdd(final String accountName) {
                createAccount(accountName);
            }
        });

        mainWindow.display();
    }

    /**
     * Update (refresh) events informations.
     */
    private static void updateEvents() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder().append("Updating Events Infos").toString());
        }
        mainWindow.resetNextEvent();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String nextEvent = DapWSClient.get().getNextEvent(configuration);
                mainWindow.setNextEvent(nextEvent);
            }
        });
    }

    /**
     * Update (refresh) e-mails informations.
     */
    private static void updateEmails() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder().append("Updating emails Infos").toString());
        }
        mainWindow.resetEmailsLabels();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String labels = DapWSClient.get().getEmailLabels(configuration);
                // Ugly
                final String formatedLabels = labels.replaceAll("- ", "\n");
                mainWindow.setEmailsLabels(formatedLabels);
            }
        });

        mainWindow.resetNbUnreadEmails();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String nbUnreadEmails = DapWSClient.get().getNbUnreadEmails(configuration);
                mainWindow.setNbUnreadEmails(nbUnreadEmails);
            }
        });
    }

    /**
     * Create a new Account and ask the user to connect his Google Account.
     * @param accountName the name of the new Account
     */
    private static void createAccount(final String accountName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder().append("Creating account with name :").append(accountName).toString());
        }
        final String url = DapWSClient.get().buildCreateAccounbtUrl(accountName);
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            LOG.error("URI cannot be created with URL : " + url, e);
        }
        try {
            if (null != uri) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (IOException e) {
            LOG.error("URI cannot be open : " + url, e);
        }

    }
}
