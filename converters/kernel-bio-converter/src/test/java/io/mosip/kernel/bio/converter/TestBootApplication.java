package io.mosip.kernel.bio.converter;

import io.mosip.kernel.bio.converter.config.TestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication (scanBasePackages = {"${mosip.auth.adapter.impl.basepackage}, io.mosip.kernel.bio.*"})
@EnableAsync
@Import(TestSecurityConfig.class)
public class TestBootApplication  {
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}
}