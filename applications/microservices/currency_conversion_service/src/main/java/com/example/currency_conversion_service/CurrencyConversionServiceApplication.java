package com.example.currency_conversion_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyConversionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}
}

// URL (Currency Exchange Service): http://localhost:8000/currency-exchange/from/USD/to/INR
// URL (Currency Conversion Service): http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
// Response structure:
//{
//	"id": 10001,
//	"from": "USD",
//	"to": "INR",
//	"conversionMultiple": 65.00,
//	"quantity": 10,
//	"totalCalculatedAmount": 650.00,
//	"environment": "8000 instance-id"
//}