/**
 * For House Of Code 2018.
 */
package fr.houseofcode.dap.admin.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main GUI entry point.
 * @author djer
 */
public class MainWindow extends JFrame {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 3606250506110105509L;

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Default Main Window Height.
     */
    private static final int WINDOW_HEIGHT = 800;
    /**
     * Default Main Window Width.
     */
    private static final int WINDOW_WIDTH = 600;
    /**
     * Panel displayed by default when JFram is loaded.
     */
    private JPanel mainPanel = new JPanel();

    /***
     * Default title. Can be change with constructor.
     */
    private String defaultTitle = "House of Code - DAP Admin";

    /** Display next event informations. */
    private JLabel nextEventLabel;
    /** Display number of unread e-mails. */
    private JLabel nbUnreadEmailsLabel;
    /** Allow scroll for e-mails labels. */
    private JScrollPane emailLabels;
    /** Display e-mails Labels. */
    private JTextArea emailLabelsArea;

    /** Store button for user actions. */
    private JPanel actionsPan;
    /** to refresh Data. */
    private JButton refreshButton;

    /**
     * Create the MainWindow. Will automatically call the "init" method.
     */
    public MainWindow() {
        super();
        init();
    }

    /**
     * Create the MainWindow. Will automatically call the "init" method.
     * @param title the title of this window.
     */
    public MainWindow(final String title) {
        super();
        this.setDefaultTitle(title);
        init();
    }

    /**
     * Initialize the GUI.
     */
    private void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder().append("Init : MainWindow, with title : ").append(getDefaultTitle())
                    .toString());
        }
        this.setTitle(getDefaultTitle());
        this.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        this.setLayout(new BorderLayout());
        nextEventLabel = new JLabel("unknow");

        nbUnreadEmailsLabel = new JLabel("");
        emailLabels = new JScrollPane();
        emailLabelsArea = new JTextArea("");
        emailLabelsArea.setLineWrap(true);
        emailLabels.add(emailLabelsArea);

        actionsPan = new JPanel();
        refreshButton = new JButton("Rafraichir");
        actionsPan.add(refreshButton);

        getContentPane().add(emailLabels, BorderLayout.CENTER);
        getContentPane().add(nbUnreadEmailsLabel, BorderLayout.NORTH);
        getContentPane().add(nextEventLabel, BorderLayout.SOUTH);
        getContentPane().add(actionsPan, BorderLayout.WEST);
    }

    /**
     * Show this Window.
     */
    public void display() {
        this.setVisible(true);
    }

    /**
     * Hide this window.
     */
    public void mask() {
        this.setVisible(false);
    }

    /**
     * Set default value for Next Event.
     */
    public void resetNextEvent() {
        resetText(nextEventLabel);
    }

    /**
     * Set value for Next Event.
     * @param nextEventInfo informations of the next Event
     */
    public void setNextEvent(final String nextEventInfo) {
        nextEventLabel.setText(nextEventInfo);
    }

    /**
     * Set default value fore-mails labels.
     */
    public void resetEmailsLabels() {
        resetText(emailLabelsArea);
    }

    /**
     * Set value for e-mail labels.
     * @param labels the new labels list
     */
    public void setEmailsLabels(final String labels) {
        emailLabelsArea.setText(labels);
    }

    /**
     * Set default value for number of unread e-mails.
     */
    public void resetNbUnreadEmails() {
        resetText(nbUnreadEmailsLabel);
    }

    /**
     * Set value for number of unread e-mails.
     * @param nbUnreadEmails the new Number of unread e-mails
     */
    public void setNbUnreadEmails(final String nbUnreadEmails) {
        nbUnreadEmailsLabel.setText(nbUnreadEmails);
    }

    /**
     * Allow to handle user action on refresh.
     * @param actionListener the action listener to call
     */
    public void addRefreshCallback(final ActionListener actionListener) {
        refreshButton.addActionListener(actionListener);
    }

    /**
     * Reset display text for a JLabel.
     * @param label the Jlabel to reset
     */
    private void resetText(final JLabel label) {
        label.setText("...");
    }

    /**
     * Reset display text for a JTextArea.
     * @param textArea the textArea to reset
     */
    private void resetText(final JTextArea textArea) {
        textArea.setText("...");
    }

    /**
     * @return the defaultTitle
     */
    public String getDefaultTitle() {
        return defaultTitle;
    }

    /**
     * @param title the defaultTitle to set
     */
    public void setDefaultTitle(final String title) {
        this.defaultTitle = title;
    }
}
