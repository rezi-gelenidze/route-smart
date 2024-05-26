package hopla.routesmart;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RouteSmartApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(RouteSmartApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {}
}
