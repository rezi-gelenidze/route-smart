package hopla.routesmart.runner;

import hopla.routesmart.service.SeedService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;


@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {
    private final SeedService seedService;
    private final ApplicationContext appContext;

    @Value("${seed.nodes.path}")
    String nodesPath;

    @Value("${seed.edges.path}")
    String edgesPath;

    @Override
    public void run(String... args) throws Exception {
        String command = args.length > 0 ? args[0] : "";
        if (command.equals("seed-graph")) {
            System.out.println("Starting the seeding process...");
            seedService.seedNodes(nodesPath);
            seedService.seedEdges(edgesPath);
            System.out.println("Seeding process completed.");

            SpringApplication.exit(appContext, () -> 0);
        } else if (command.equals("seed-dummy")) {
            System.out.println("Starting the dummy seeding process...");
            seedService.seedDummyTripsAndParcels();
            System.out.println("Dummy seeding process completed.");

            SpringApplication.exit(appContext, () -> 0);
        }
    }
}
