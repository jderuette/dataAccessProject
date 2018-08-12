package fr.houseofcode.dap;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Hello world!
 */
public final class AppLauncher {
    /**
     * Utility CLass.
     */
    private AppLauncher() {
        throw new UnsupportedOperationException("Utility Class");
    }
    /**
     * Main entry point.
     * @param args user parameters
     * @throws IOException              Google error
     * @throws GeneralSecurityException Google error
     */
    public static void main(final String[] args) throws IOException, GeneralSecurityException {
        final QuickStartSample app = new QuickStartSample();
        app.display("me");
    }
}
