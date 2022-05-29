package com.example.currency_conversion_service.CurrencyConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="currency-exchange-service")
public interface CurrencyExchangeProxy {
    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
    // Note: CurrencyConversion structure matches the response we obtain from CurrencyExchange Microservice (url above). Therefore, these values automatically get mapped.
        //     -- CurrencyConversion structure --
        //     public class CurrencyConversion {
        //        private Long id;
        //        private String from;
        //        private String to;
        //        private BigDecimal quantity;
        //        private BigDecimal conversionMultiple;
        //        private BigDecimal totalCalculatedAmount;
        //        private String environment;
        //    }
        //     -- Response we obtain from CurrencyExchange Service (url above) --
        //    {
        //    	"id":10001,
        //    	"from":"USD",
        //    	"to":"INR",
        //    	"conversionMultiple":65.00,
        //    	"environment":"8000 instance-id"
        //    }
}
