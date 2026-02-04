package com.ordep.syncotable.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${prod.name}")
    private String prodName;

    @ModelAttribute("prodName")
    public String getProdName() {
        return prodName;
    }
}
