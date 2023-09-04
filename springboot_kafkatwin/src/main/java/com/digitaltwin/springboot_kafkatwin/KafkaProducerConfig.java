package com.digitaltwin.springboot_kafkatwin;

import java.util.HashMap;
import java.util.Map;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Configuration;

@Configuration
@Component
public class KafkaProducerConfig {

	@Value("${springboot_kafkatwin.topic1}")
	private String topic1;

	@Value("${springboot_kafkatwin.topic2}")
	private String topic2;


	@Autowired
	private KafkaProperties kafkaProperties;	
	
	@Bean
	public Map<String, Object> producerConfigs(){
		Map<String, Object> props= new HashMap<>(kafkaProperties.buildProducerProperties());
	//	props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapserver);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // Does not Work with JSONObject
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); 
		return props;
	}
	
	@Bean
	public ProducerFactory<String, String> producerFactory(){
		return  new DefaultKafkaProducerFactory<String, String>(producerConfigs());
		
		
	}

	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(){
		KafkaTemplate<String,String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setProducerListener(new ProducerListener<String,String>(){
			@Override
			public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
				System.out.println("Successfully published"+producerRecord.value());
			}
			@Override
			public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata,Exception e) {
				System.out.println("ERROR"+recordMetadata);
				e.printStackTrace();
			}
		});
		return kafkaTemplate;
	}

	@Bean
	NewTopic topic1() {
		/* Can include Partitions and replicas
		        e.g.: TopicBuilder.name("topic1")
                	.partitions(10)
                	.replicas(1)
                	.build();
		*/
		
		return TopicBuilder.name(topic1).build();
	}

	@Bean
	NewTopic topic2() {
		return TopicBuilder.name(topic2).build();
	}
	
	
}
