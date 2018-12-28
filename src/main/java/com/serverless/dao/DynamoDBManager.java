package com.serverless.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class DynamoDBManager {

	private static volatile DynamoDBManager instance;
	private static AmazonDynamoDB client;

	private DynamoDBManager() {
		client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
	}

	public static DynamoDBManager instance() {

		if (instance == null) {
			synchronized (DynamoDBManager.class) {
				if (instance == null)
					instance = new DynamoDBManager();
			}
		}
		return instance;
	}

	public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
		return new DynamoDBMapper(client, mapperConfig);
	}

}
