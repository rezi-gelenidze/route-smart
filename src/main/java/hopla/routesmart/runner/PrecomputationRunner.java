package hopla.routesmart.runner;

import hopla.routesmart.service.PathfindingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PrecomputationRunner implements CommandLineRunner {

    @Autowired
    private PathfindingService pathfindingService;

    @Override
    public void run(String... args) throws Exception {
        String command = args.length > 0 ? args[0] : "";

        if (command.equals("precompute")) {
            System.out.println("Starting the precomputation process...");
            pathfindingService.precomputePaths();
            System.out.println("Precomputation process completed.");
        }
    }
}
