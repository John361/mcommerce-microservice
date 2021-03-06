package com.mproduits.configuration;

import brave.sampler.Sampler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfiguration {

    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
