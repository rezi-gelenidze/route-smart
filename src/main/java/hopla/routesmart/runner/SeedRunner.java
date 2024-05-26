package hopla.routesmart.runner;

import hopla.routesmart.service.SeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;


@Component
public class SeedRunner implements CommandLineRunner {

    @Autowired
    private SeedService seedService;

    @Autowired
    private ApplicationContext appContext;

    @Override
    public void run(String... args) throws Exception {
        String command = args.length > 0 ? args[0] : "";
        if (command.equals("seed")) {
            System.out.println("Starting the seeding process...");
            seedService.seedNodes("classpath:seed/nodes.geojson");
            seedService.seedEdges("classpath:seed/edges.geojson");
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
