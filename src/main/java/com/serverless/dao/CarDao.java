package com.serverless.dao;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.serverless.model.Car;

public class CarDao {

	private static DynamoDBMapper mapper;
	private static volatile CarDao instance;

	private CarDao() {
		DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
				.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(Car.TABLE_NAME))
		        .build();
		mapper = DynamoDBManager.instance().createDbMapper(mapperConfig);
	}

	public static CarDao instance() {

		if (instance == null) {
			synchronized (CarDao.class) {
				if (instance == null)
					instance = new CarDao();
			}
		}
		return instance;
	}

	public List<Car> findAllCars() {
		return mapper.scan(Car.class, new DynamoDBScanExpression());
	}
	
	public Car save(Car car) {    
	    mapper.save(car);
	    return car;
	}
}
