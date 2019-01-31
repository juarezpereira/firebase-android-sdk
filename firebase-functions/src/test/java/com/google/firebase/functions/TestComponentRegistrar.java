/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.functions;

import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.util.Collections;
import java.util.List;

public class TestComponentRegistrar implements ComponentRegistrar {
  @Override
  public List<Component<?>> getComponents() {
    return Collections.singletonList(
        Component.builder(TestUserAgentDependentComponent.class)
            .add(Dependency.required(UserAgentPublisher.class))
            .factory(
                container ->
                    new TestUserAgentDependentComponent(container.get(UserAgentPublisher.class)))
            .build());
  }
}
