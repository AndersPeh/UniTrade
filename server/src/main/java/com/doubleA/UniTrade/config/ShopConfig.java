package com.doubleA.UniTrade.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// To indicate that this class contains beans for dependency injection.
@Configuration
public class ShopConfig {

    // Bean for allowing this to be used in dependency injection.
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
