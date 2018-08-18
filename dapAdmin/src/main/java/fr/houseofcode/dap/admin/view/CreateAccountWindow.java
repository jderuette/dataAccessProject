package fr.houseofcode.dap.admin.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.houseofcode.dap.admin.Constantes;

/**
 * @author djer
 */
public class CreateAccountWindow extends JFrame {

    /** serialVersionUID. */
    private static final long serialVersionUID = -2107190421348550856L;

    /** Logger. */
    private static final Logger LOG = LogManager.getLogger();

    /** Default Main Window Height. */
    private static final int WINDOW_HEIGHT = 400;
    /** Default Main Window Width. */
    private static final int WINDOW_WIDTH = 100;
    /** Panel displayed by default when JFram is loaded. */
    private JPanel mainPanel = new JPanel();

    /** Panel to Hold user Account. */
    private JPanel accountPanel;
    /** Label for user Account description. */
    private JLabel userAccountLabel;
    /** to allow user to define the "name" of is new account. */
    private JTextField userAccountId;
    /** to launch account creation process. */
    private JButton createButton;
    /** Event Listener to "accountCreated" Callback. */
    private EventListenerList eventListenerList = new EventListenerList();

    /**
     * Create window with default options.
     */
    public CreateAccountWindow() {
        super();
        init();
    }

    /**
     * Initialize the GUI.
     */
    private void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder().append("Init : CreateAccountWindow").toString());
        }
        this.setTitle("Dap Création de compte");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        this.setLayout(new BorderLayout());

        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.LINE_AXIS));

        userAccountLabel = new JLabel("Id du compte");
        accountPanel.add(userAccountLabel);
        userAccountId = new JTextField();
        userAccountId.setPreferredSize(Constantes.DEFAULT_TEXT_FIELD_DIMENSION);
        accountPanel.add(userAccountId);

        createButton = new JButton("Créer !");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (null == userAccountId.getText() || "".equals(userAccountId.getText())) {
                    userAccountId.setCaretColor(Constantes.ERROR_BACKGROUND_COLOR);
                } else {
                    final AccountToCreate[] listners = eventListenerList.getListeners(AccountToCreate.class);
                    if (listners.length > 0) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Calling " + listners.length + " accountToCreate Listner");
                        }
                        for (final AccountToCreate listner : listners) {
                            listner.accountToAdd(userAccountId.getText());
                        }
                    }
                }
            }
        });
        createButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent event) {
                userAccountId.setCaretColor(null);
            }
        });

        getContentPane().add(accountPanel, BorderLayout.CENTER);
        getContentPane().add(createButton, BorderLayout.SOUTH);
    }

    /**
     * @param lsitener the listener to call
     */
    public void addAccountToAddListener(final AccountToCreate lsitener) {
        eventListenerList.add(AccountToCreate.class, lsitener);
    }

}
