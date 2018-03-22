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
package io.mifos.notification.service.internal.mapper;

import io.mifos.notification.service.internal.repository.SampleJpaEntity;
import io.mifos.notification.api.v1.domain.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SampleMapper {

  private SampleMapper() {
    super();
  }

  public static Sample map(final SampleJpaEntity sampleJpaEntity) {
    final Sample sample = new Sample();
    sample.setIdentifier(sampleJpaEntity.getIdentifier());
    sample.setPayload(sampleJpaEntity.getPayload());
    return sample;
  }

  public static List<Sample> map(final List<SampleJpaEntity> sampleJpaEntities) {
    final ArrayList<Sample> samples = new ArrayList<>(sampleJpaEntities.size());
    samples.addAll(sampleJpaEntities.stream().map(SampleMapper::map).collect(Collectors.toList()));
    return samples;
  }
}
