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

package io.lunamc.plugins.example.netty;

import io.lunamc.platform.service.ServiceRegistration;
import io.lunamc.common.json.JsonMapper;
import io.lunamc.common.network.AuthorizedConnection;
import io.lunamc.common.text.builder.ComponentBuilderFactory;
import io.lunamc.plugins.netty.handler.PlayHandlerFactory;
import io.netty.channel.ChannelHandler;

import java.util.Objects;

public class ExamplePlayHandlerFactory implements PlayHandlerFactory {

    private final ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory;
    private final ServiceRegistration<JsonMapper> jsonMapper;

    public ExamplePlayHandlerFactory(ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory,
                                     ServiceRegistration<JsonMapper> jsonMapper) {
        this.componentBuilderFactory = Objects.requireNonNull(componentBuilderFactory, "componentBuilderFactory must not be null");
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper must not be null");
    }

    @Override
    public ChannelHandler createHandler(AuthorizedConnection connection) {
        // If you're going to write your own ChannelHandler(s) you would be able to choose an implementation based on
        // the decided virtual host or the supported protocol version
        return new ExamplePlayHandler(connection, componentBuilderFactory, jsonMapper);
    }
}
