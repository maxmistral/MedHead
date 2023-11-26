package eu.echodream.medhead;

import eu.echodream.medhead.Utils.CoordinatesCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedHeadApplication {

	public static void main(String[] args) {
		CoordinatesCache.initCache();
		SpringApplication.run(MedHeadApplication.class, args);
	}

}
