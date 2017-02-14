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

package io.lunamc.plugins.example;

import io.lunamc.platform.plugin.PluginAdapter;
import io.lunamc.platform.plugin.PluginContext;
import io.lunamc.platform.plugin.annotation.LunaPlugin;
import io.lunamc.platform.plugin.annotation.LunaPluginDependency;
import io.lunamc.platform.service.ServiceRegistry;
import io.lunamc.common.host.StaticVirtualHost;
import io.lunamc.common.host.VirtualHostManager;
import io.lunamc.common.json.JsonMapper;
import io.lunamc.common.text.builder.ComponentBuilderFactory;
import io.lunamc.plugins.example.netty.ExamplePlayHandlerFactory;
import io.lunamc.plugins.example.status.ExampleStatusProvider;
import io.lunamc.plugins.netty.handler.PlayHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LunaPlugin(
        id = "luna-example",
        version = "0.0.1",
        pluginDependencies = {
                @LunaPluginDependency(id = "luna-common", versionExpression = "0.*"),
                @LunaPluginDependency(id = "luna-netty", versionExpression = "0.*")
        }
)
public class ExamplePlugin extends PluginAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamplePlugin.class);

    @Override
    public void initialize(PluginContext context) {
        ServiceRegistry serviceRegistry = context.getServiceRegistry();

        serviceRegistry.setService(PlayHandlerFactory.class, new ExamplePlayHandlerFactory(
                serviceRegistry.getService(ComponentBuilderFactory.class),
                serviceRegistry.getService(JsonMapper.class)
        ));
    }

    @Override
    public void start(PluginContext context) {
        ServiceRegistry serviceRegistry = context.getServiceRegistry();
        VirtualHostManager virtualHostManager = serviceRegistry.getService(VirtualHostManager.class).requireInstance();

        virtualHostManager.setFallbackHost(new StaticVirtualHost(
                null,
                new ExampleStatusProvider(serviceRegistry.getService(ComponentBuilderFactory.class)),
                false,
                null
        ));

        LOGGER.info("Fallback virtual host set");
    }
}
