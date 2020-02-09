package com.surendra.kafka;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.surendra.kafka.constants.IKafkaConstants;
import com.surendra.kafka.consumer.ConsumerCreator;
import com.surendra.kafka.producer.ProducerCreator;

public class App {
	public static void main(String[] args) {
		//runProducer();
		 runConsumer();
	}

	static void runConsumer() {
		Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

		int noMessageToFetch = 0;
		HashMap<String, String> values = new HashMap<>();
		//consumer.seekToEnd(consumer.assignment());
		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
			if (consumerRecords.count() == 0) {
				noMessageToFetch++;
				if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
					break;
				else
					continue;
			}
			
			consumerRecords.forEach(record -> {
				System.out.println("Record value " + record.value());
				System.out.println("Record offset " + record.offset());
				JSONObject jsonObject = readAsJson(record.value());
				System.out.println(" -------------- >>>>>>>> "+values.keySet().size() );
				String sensorPath = (String) jsonObject.get("name");
				values.put(sensorPath, record.value());
				
				if(values.keySet().size() > 5) {
					System.out.println(values);
				}
				if(record.value().contains("Cisco-IOS-XR-pfi-im-cmd-oper:interfaces")) {
				//System.out.println("Record Key " + record.key());
				//System.out.println("Record value " + record.value());
				//System.out.println("Record partition " + record.partition());
				//System.out.println("Record offset " + record.offset());
				
				}
			});
			
			
			consumer.commitAsync();
		}
		consumer.close();
	}
	
	

	static void runProducer() {
		Producer<Long, String> producer = ProducerCreator.createProducer();
		System.out.println(loadJson());
		JSONObject jsonObject = loadJson();

		for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {

			/*
			 * final ProducerRecord<Long, String> record = new ProducerRecord<Long,
			 * String>(IKafkaConstants.TOPIC_NAME, "This is record " + index);
			 */
			
			jsonObject.put("producerTime", System.currentTimeMillis());
			
			final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
					jsonObject.toJSONString());
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		}

	}

	static JSONObject loadJson() {
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		try {
			ClassLoader classLoader = App.class.getClassLoader();

			URL resource = classLoader.getResource("message.json");

			Object obj = parser.parse(new FileReader(resource.getFile()));
			
			jsonObject = (JSONObject) obj;

			String Source = (String) jsonObject.get("Source");

			System.out.println("Source: " + Source);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	static JSONObject readAsJson(String message) {
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(message);
			jsonObject = (JSONObject) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
}




