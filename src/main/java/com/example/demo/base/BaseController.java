package com.example.demo.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping(path="/")
public class BaseController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String index() {
        return this.messageSource.getMessage("good.morning", null, "Default message", LocaleContextHolder.getLocale());
    }
}
