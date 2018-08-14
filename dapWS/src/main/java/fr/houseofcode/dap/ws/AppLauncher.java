package fr.houseofcode.dap;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@SpringBootApplication
public class AppLauncher {

    /** Application configuration. */
    @Autowired
    private Config configuration;

    /**
     * Launcher for command line.
     * @param ctx the context
     * @return the CommandLine runner
     */
    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {

            configuration.init(System.getProperty("user.home") + System.getProperty("file.separator") + "houseOfCode"
                    + System.getProperty("file.separator") + "dataAccessProject", "Djer Data Access Project");

            final GoogleFacade app = new GoogleFacade(configuration);
            app.display("me");

        };
    }

    /**
     * Main entry point.
     * @param args user parameters
     * @throws IOException              Google error
     * @throws GeneralSecurityException Google error
     */
    public static void main(final String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(AppLauncher.class, args);
    }
}
