package com.eazybytes.eazyschool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "eazyschool")
@PropertySource("classpath:pierrot.properties")
@Validated
public class EazySchoolProps {

    @Min(value = 5,message = "must be between 5 and 8")
    @Max(value = 8,message = "must be between 5 and 8")
    private int defaultPageSize;
    private Map<String, String> contact;
    private List<String> cities;

}
