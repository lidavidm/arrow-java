/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.arrow.driver.jdbc.utils;

import static java.lang.Runtime.getRuntime;
import static org.apache.arrow.driver.jdbc.utils.ArrowFlightConnectionConfigImpl.ArrowFlightConnectionProperty.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.arrow.driver.jdbc.utils.ArrowFlightConnectionConfigImpl.ArrowFlightConnectionProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class ArrowFlightConnectionConfigImplTest {

  private static final Random RANDOM = new Random(12L);

  private Properties properties;
  private ArrowFlightConnectionConfigImpl arrowFlightConnectionConfig;

  public ArrowFlightConnectionProperty property;
  public Object value;
  public Function<ArrowFlightConnectionConfigImpl, ?> arrowFlightConnectionConfigFunction;

  @BeforeEach
  public void setUp() {
    properties = new Properties();
    arrowFlightConnectionConfig = new ArrowFlightConnectionConfigImpl(properties);
  }

  @ParameterizedTest
  @MethodSource("provideParameters")
  public void testGetProperty(
      ArrowFlightConnectionProperty property,
      Object value,
      Object expected,
      Function<ArrowFlightConnectionConfigImpl, ?> configFunction) {
    properties.put(property.camelName(), value);
    arrowFlightConnectionConfigFunction = configFunction;
    assertThat(configFunction.apply(arrowFlightConnectionConfig), is(expected));
    assertThat(
        arrowFlightConnectionConfigFunction.apply(arrowFlightConnectionConfig), is(expected));
  }

  public static Stream<Arguments> provideParameters() {
    int port = RANDOM.nextInt(Short.toUnsignedInt(Short.MAX_VALUE));
    boolean useEncryption = RANDOM.nextBoolean();
    int threadPoolSize = RANDOM.nextInt(getRuntime().availableProcessors());
    return Stream.of(
        Arguments.of(
            HOST,
            "host",
            "host",
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getHost),
        Arguments.of(
            PORT,
            port,
            port,
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getPort),
        Arguments.of(
            USER,
            "user",
            "user",
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getUser),
        Arguments.of(
            PASSWORD,
            "password",
            "password",
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getPassword),
        Arguments.of(
            USE_ENCRYPTION,
            useEncryption,
            useEncryption,
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::useEncryption),
        Arguments.of(
            THREAD_POOL_SIZE,
            threadPoolSize,
            threadPoolSize,
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::threadPoolSize),
        Arguments.of(
            CATALOG,
            "catalog",
            "catalog",
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getCatalog),
        Arguments.of(
            CONNECT_TIMEOUT_MILLIS,
            5000,
            Duration.ofMillis(5000),
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::getConnectTimeout),
        Arguments.of(
            USE_CLIENT_CACHE,
            false,
            false,
            (Function<ArrowFlightConnectionConfigImpl, ?>)
                ArrowFlightConnectionConfigImpl::useClientCache));
  }
}
