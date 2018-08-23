package fr.houseofcode.dap.admin.view;

import java.util.EventListener;

/**
 * @author djer
 *
 */
public interface AccountToCreate extends EventListener {
    /**
     * Invoked when a user ask to add an Account.
     * @param accountName name of the account
     */
    void accountToAdd(String accountName);

}
