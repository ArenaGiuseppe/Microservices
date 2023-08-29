package com.xantrix.webapp.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "fidelity")
@Data
public class FidelityProperties 
{
    private int valminimo;
    private int valbollino;
    private int bonusmobile;
}
