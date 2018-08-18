/**
 * 
 */
package fr.houseofcode.dap.admin.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.houseofcode.dap.admin.Config;

/**
 * @author djer
 */
public class ConfigWindow extends JFrame {

    /** serialVersionUID. */
    private static final long serialVersionUID = -6453623999108757746L;

    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** Default Main Window Height. */
    private static final int WINDOW_HEIGHT = 400;
    /** Default Main Window Width. */
    private static final int WINDOW_WIDTH = 200;
    /** Panel displayed by default when JFram is loaded. */
    private JPanel mainPanel = new JPanel();

    /** The userId to query. */
    private JTextField userIdField;
    /** The URL of DaP WebService. */
    private JTextField wsUrlField;
    /** List of field container. */
    private JPanel fieldListPane;
    /** The window Save button. */
    private JButton saveButton;
    /** Panle to group action buttons. */
    private JPanel actionPanel;

    /**
     * Create the configuration Windows.
     */
    public ConfigWindow() {
        super();
        init();
    }

    /**
     * Initialize the Windows.
     */
    private void init() {
        this.setTitle("Dap Confiuration");
        this.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        this.setLayout(new BorderLayout());

        userIdField = new JTextField("user");

        wsUrlField = new JTextField();
        wsUrlField.setText("http://localhost:8080");

        fieldListPane = new JPanel();
        fieldListPane.add(userIdField);
        fieldListPane.add(wsUrlField);

        saveButton = new JButton("Sauvegarder");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Saving new Configuration");
                }
                Config.get().setUser(userIdField.getText());
                Config.get().setDapUrl(wsUrlField.getText());
            }
        });

        actionPanel = new JPanel();
        actionPanel.add(saveButton);


        getContentPane().add(fieldListPane, BorderLayout.CENTER);
        getContentPane().add(actionPanel, BorderLayout.SOUTH);

    }

}
