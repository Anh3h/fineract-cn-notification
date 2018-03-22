/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.mifos.notification.service;

import java.util.Properties;

import io.mifos.anubis.config.EnableAnubis;
import io.mifos.core.async.config.EnableAsync;
import io.mifos.core.cassandra.config.EnableCassandra;
import io.mifos.core.command.config.EnableCommandProcessing;
import io.mifos.core.lang.config.EnableServiceException;
import io.mifos.core.lang.config.EnableTenantContext;
import io.mifos.core.mariadb.config.EnableMariaDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("WeakerAccess")
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableAsync
@EnableTenantContext
@EnableCassandra
@EnableMariaDB
@EnableCommandProcessing
@EnableAnubis
@EnableServiceException
@ComponentScan({
    "io.mifos.notification.service.rest",
    "io.mifos.notification.service.internal.service",
    "io.mifos.notification.service.internal.repository",
    "io.mifos.notification.service.internal.command.handler",
    "io.mifos.notification.service.twilio",
    "io.mifos.notification.service.smtp",
})
@EnableJpaRepositories({
    "io.mifos.notification.service.internal.repository"
})
public class NotificationConfiguration extends WebMvcConfigurerAdapter {

  @Value("${spring.mail.host}")
  private static String host;
  @Value("${spring.mail.port}")
  private static String port;
  @Value("${spring.mail.username}")
  private static String username;
  @Value("${spring.mail.password}")
  private static String password;
  @Value("${spring.mail.protocol}")
  private static String protocol;
  @Value("${spring.mail.properties.mail.smtp.auth}")
  private static String smtpAuth;
  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private static String starttlsEnable;

  public NotificationConfiguration() {
    super();
  }

  @Bean(name = ServiceConstants.LOGGER_NAME)
  public Logger logger() {
    return LoggerFactory.getLogger(ServiceConstants.LOGGER_NAME);
  }

  @Bean
  public JavaMailSender getJavaMailSender(){
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(Integer.parseInt(port));

    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", protocol);
    props.put("mail.smtp.auth", smtpAuth);
    props.put("mail.smtp.starttls.enable", starttlsEnable);
    return mailSender;
  }

  @Override
  public void configurePathMatch(final PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(Boolean.FALSE);
  }
}
