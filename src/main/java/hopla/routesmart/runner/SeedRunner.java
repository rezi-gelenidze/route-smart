package hopla.routesmart.runner;

import hopla.routesmart.service.SeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedRunner implements CommandLineRunner {

    @Autowired
    private SeedService seedService;

    @Override
    public void run(String... args) throws Exception {
        String command = args.length > 0 ? args[0] : "";
        if (command.equals("seed")) {
            System.out.println("Starting the seeding process...");
            seedService.seedNodes("classpath:seed/nodes.geojson");
            seedService.seedEdges("classpath:seed/edges.geojson");
            System.out.println("Seeding process completed.");
        }
        else if (command.equals("seed-dummy")) {
            System.out.println("Starting the dummy seeding process...");
            seedService.seedDummyTripsAndParcels();
            System.out.println("Dummy seeding process completed.");
        }
    }
}
