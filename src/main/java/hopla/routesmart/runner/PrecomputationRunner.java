package hopla.routesmart.runner;

import hopla.routesmart.service.PathfindingService;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;


@Component
@RequiredArgsConstructor
public class PrecomputationRunner implements CommandLineRunner {
    private final PathfindingService pathfindingService;
    private final ApplicationContext appContext;

    @Override
    public void run(String... args) {
        String command = args.length > 0 ? args[0] : "";

        if (command.equals("precompute")) {
            System.out.println("Starting the precomputation process...");
            pathfindingService.precomputePaths();
            System.out.println("Precomputation process completed.");

            // Exit the application gracefully
            SpringApplication.exit(appContext, () -> 0);
        }
    }
}
