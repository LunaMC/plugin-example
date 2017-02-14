/*
 *  Copyright 2017 LunaMC.io
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.lunamc.plugins.example.status;

import io.lunamc.platform.service.ServiceRegistration;
import io.lunamc.common.network.Connection;
import io.lunamc.common.network.DecidedConnection;
import io.lunamc.common.status.BetaStatusResponse;
import io.lunamc.common.status.LegacyStatusResponse;
import io.lunamc.common.status.StaticStatusResponse;
import io.lunamc.common.status.StatusProvider;
import io.lunamc.common.status.StatusResponse;
import io.lunamc.common.text.TextComponent;
import io.lunamc.common.text.builder.ComponentBuilderFactory;

import java.util.Collections;
import java.util.Objects;

public class ExampleStatusProvider implements StatusProvider {

    private final ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory;

    public ExampleStatusProvider(ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory) {
        this.componentBuilderFactory = Objects.requireNonNull(componentBuilderFactory, "componentBuilderFactory must not be null");
    }

    @Override
    public StatusResponse createStatusResponse(DecidedConnection connection) {
        TextComponent message = componentBuilderFactory.requireInstance().createTextComponentBuilder()
                .text("Hello World")
                .build();

        return new StaticStatusResponse(
                new StaticStatusResponse.StaticVersion("multi", connection.getProtocolVersion()),
                new StaticStatusResponse.StaticPlayers(1, 0, Collections.emptyList()),
                message,
                null
        );
    }

    @Override
    public LegacyStatusResponse createLegacy16StatusResponse(DecidedConnection connection) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public LegacyStatusResponse createLegacy14StatusResponse(Connection connection) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public BetaStatusResponse createBetaStatusResponse(Connection connection) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
