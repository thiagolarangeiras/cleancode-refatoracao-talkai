package com.satc.integrador.ai.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

@Configuration
public class DozerConfiguracao {
    @Bean
    public Mapper dozerBeanMapper() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        //mapper.setMappingFiles(Arrays.asList("dozerBeanMapping.xml")); // Optional
        return mapper;
    }
}
