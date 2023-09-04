package com.digitaltwin.springboot_kafkatwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringbootKafkatwinApplication {

	@Autowired
	private final KafkaProducer producer ;
	
	twinSetup twins;	

	public SpringbootKafkatwinApplication() {
		this.producer = null;
		try {
			System.out.println("Creating Twins ...");
			twins = new twinSetup(2);
			System.out.println("Twins online ...");
		} catch(Exception e){
			e.printStackTrace();
		}	
	}

	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootKafkatwinApplication.class, args);
	}

	@Value("${springboot_kafkatwin.topic1}")
	private String topic1;

	@Value("${springboot_kafkatwin.topic2}")
	private String topic2;
	
	

	@Scheduled(fixedDelay=5000L)
	public void publishTelemetry() {
		String topics[]= {topic1,topic2};
		int x=0;
		for(x=1;x<=twins.get_device_count();x++) {
			JSONObject jsonObject = twins.get_device_telemetry(x);
			if(jsonObject == null) return;
			String message = jsonObject.toString();		
			System.out.println("Publishing telemetry for motor : "+x);
//			producer.publishTelemetry(topics[x-1], (Object) jsonObject); <== Serializer does not work
			producer.publishTelemetry(topics[x-1], message);
		}
	}	
	
	
}
