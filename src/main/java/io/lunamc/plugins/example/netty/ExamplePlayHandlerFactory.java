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
