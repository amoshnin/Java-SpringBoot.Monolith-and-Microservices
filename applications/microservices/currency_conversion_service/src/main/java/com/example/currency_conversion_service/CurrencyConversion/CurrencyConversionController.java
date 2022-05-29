package com.example.currency_conversion_service.CurrencyConversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequestMapping(path="currency-conversion")
public class CurrencyConversionController {
    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping(path="from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate()
                .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);// Sending a GET request and GETTING an object back
        CurrencyConversion currencyConversion = responseEntity.getBody();
        // Note: CurrencyConversion structure matches the response we obtain from CurrencyExchange Microservice (url above). Therefore, these values automatically get mapped.
            // -- CurrencyConversion structure --
            // public class CurrencyConversion {
            //    private Long id;
            //    private String from;
            //    private String to;
            //    private BigDecimal quantity;
            //    private BigDecimal conversionMultiple;
            //    private BigDecimal totalCalculatedAmount;
            //    private String environment;
            //}
            // -- Response we obtain from CurrencyExchange Service (url above) --
            //{
                //	"id":10001,
                //	"from":"USD",
                //	"to":"INR",
                //	"conversionMultiple":65.00,
                //	"environment":"8000 instance-id"
            //}
        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment());
    }

    @GetMapping(path="feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionValueFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = this.proxy.retrieveExchangeValue(from ,to);
        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment());
    }
}
