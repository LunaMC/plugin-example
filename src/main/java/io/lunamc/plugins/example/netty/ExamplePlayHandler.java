package io.lunamc.plugins.example.netty;

import io.lunamc.platform.service.ServiceRegistration;
import io.lunamc.common.json.JsonMapper;
import io.lunamc.common.network.AuthorizedConnection;
import io.lunamc.common.text.TextComponent;
import io.lunamc.common.text.builder.ComponentBuilderFactory;
import io.lunamc.plugins.netty.utils.NettyUtils;
import io.lunamc.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ExamplePlayHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamplePlayHandler.class);

    private final AuthorizedConnection connection;
    private final ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory;
    private final ServiceRegistration<JsonMapper> jsonMapper;

    public ExamplePlayHandler(AuthorizedConnection connection,
                              ServiceRegistration<ComponentBuilderFactory> componentBuilderFactory,
                              ServiceRegistration<JsonMapper> jsonMapper) {
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
        this.componentBuilderFactory = Objects.requireNonNull(componentBuilderFactory, "componentBuilderFactory must not be null");
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper must not be null");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        writeJoinGame(ctx);
        writeBrand(ctx);
        writeKick(ctx);
    }

    private void writeJoinGame(ChannelHandlerContext ctx) {
        ByteBuf out = ctx.alloc().buffer();
        // Write packet id of join game (0x23)
        ProtocolUtils.writeVarInt(out, 0x23);
        // Write entity id
        out.writeInt(42);
        // Write game mode (0 = survival)
        out.writeByte(0);
        // Write dimension (0 = overworld)
        out.writeInt(0);
        // Write difficulty (0 = peaceful)
        out.writeByte(0);
        // Write max players
        out.writeByte(1);
        // Write level type
        ProtocolUtils.writeString(out, "default");
        // Write reduced debug info
        out.writeBoolean(false);

        // Send it to the client
        ctx.writeAndFlush(out, ctx.voidPromise());
    }

    private void writeBrand(ChannelHandlerContext ctx) {
        ByteBuf data = ctx.alloc().buffer();
        ByteBuf out = ctx.alloc().buffer();
        try {
            // Write plugin message data
            ProtocolUtils.writeString(data, "LunaMC_Example");

            // Write packet id of plugin message (0x18)
            ProtocolUtils.writeVarInt(out, 0x18);
            // Write channel name
            ProtocolUtils.writeString(out, "MC|Brand");
            // Write plugin message
            ProtocolUtils.writeVarInt(out, data.readableBytes());
            out.writeBytes(data);
        } finally {
            data.release();
        }

        // Send it to the client
        ctx.writeAndFlush(out, ctx.voidPromise());
    }

    private void writeKick(ChannelHandlerContext ctx) {
        LOGGER.info("Example play handling done for {}", connection.getProfile());
        TextComponent message = componentBuilderFactory.requireInstance().createTextComponentBuilder()
                .text("Thank you " + connection.getProfile().getName() + " for trying out the LunaMC Example Plugin!")
                .build();
        String json = jsonMapper.requireInstance().serialize(message);

        ByteBuf out = ctx.alloc().buffer();
        // Write packet id of disconnect (0x1a)
        ProtocolUtils.writeVarInt(out, 0x1a);
        // Write reason
        ProtocolUtils.writeString(out, json);

        // Send it to the client and closes the connection
        NettyUtils.writeFlushAndClose(ctx, out);
    }
}
