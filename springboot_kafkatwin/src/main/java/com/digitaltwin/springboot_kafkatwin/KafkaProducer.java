package com.digitaltwin.springboot_kafkatwin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller
public class KafkaProducer {

	private KafkaTemplate<String, String> template ;
	
	@Autowired
	public KafkaProducer(KafkaTemplate<String, String> template) {
		this.template = template;
	}

	
	
	public void publishTelemetry(String topic, String message) {
		System.out.println(topic+"::"+message);
		this.template.send(topic,message);
	}
	
}
