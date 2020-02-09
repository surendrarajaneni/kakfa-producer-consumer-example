package com.surendra.kafka.constants;

public interface IKafkaConstants {
	//public static String KAFKA_BROKERS = "172.31.9.49:9091,172.31.10.185:9092,172.31.7.125:9093";
	//public static String KAFKA_BROKERS = "10.106.179.19:9092,10.106.179.21:9092,10.106.179.22:9092";
	public static String KAFKA_BROKERS = "10.83.34.8:9092,10.83.34.9:9092,10.83.34.10:9092";
	
	public static Integer MESSAGE_COUNT=1000;
	
	public static String CLIENT_ID="client1";
	
	public static String TOPIC_NAME="telemetry";
	
	public static String GROUP_ID_CONFIG="consumerGroup10";
	
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
	
	public static String OFFSET_RESET_LATEST="latest";
	
	public static String OFFSET_RESET_EARLIER="earliest";
	
	public static Integer MAX_POLL_RECORDS=1;
}
