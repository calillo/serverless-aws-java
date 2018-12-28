package com.serverless.handler;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dao.CarDao;
import com.serverless.model.Car;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;


public class CreateCarHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = LogManager.getLogger(CreateCarHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			// get the 'body' from input
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

			// create the Product object for post
			Car car = new Car();

			// car.setId(body.get("id").asText());
			car.setBrand(body.get("brand").asText());
			car.setModel(body.get("model").asText());
			car.setVersion(body.get("version").asInt());
			car.setPrice(new BigDecimal(body.get("price").asText()));
			Date now = Calendar.getInstance().getTime();
			car.setInsertDate(now);
			car.setUpdateDate(now);
			CarDao.instance().save(car);

			// send the response back
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(car)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		} catch (Exception ex) {
			logger.error("Error in saving product: " + ex);

			// send the error response back
			Response responseBody = new Response("Error in saving product: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
