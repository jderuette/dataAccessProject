package fr.houseofcode.dap.admin;
/**
 * Data Access Project Launcher.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.houseofcode.dap.admin.service.DapWSClient;
import fr.houseofcode.dap.admin.view.MainWindow;

/**
 * Launcher for the DataAccessProject admin tool.
 * @author djer
 */
public final class Launcher {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    private static MainWindow mainWindow;

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

        mainWindow.display();
    }

    private static void updateEvents() {
        mainWindow.resetNextEvent();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String nextEvent = DapWSClient.get().getNextEvent();
                mainWindow.setNextEvent(nextEvent);
            }
        });
    }

    private static void updateEmails() {
        mainWindow.resetEmailsLabels();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String labels = DapWSClient.get().getEmailLabels();
                // Ugly
                final String formatedLabels = labels.replaceAll("- ", "\n");
                mainWindow.setEmailsLabels(formatedLabels);
            }
        });

        mainWindow.resetNbUnreadEmails();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final String nbUnreadEmails = DapWSClient.get().getNbUnreadEmails();
                mainWindow.setNbUnreadEmails(nbUnreadEmails);
            }
        });
    }
}
