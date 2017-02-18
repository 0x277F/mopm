/*
 * Copyright 2017 Hex <hex@hex.lc>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lc.hex.mopm.bukkit;

import lc.hex.mopm.api.bukkit.AsyncProxyDetectedEvent;
import lc.hex.mopm.api.common.ProxyBlacklist;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class ConnectionListener implements Listener {
    private MopmBukkitPlugin plugin;
    private Map<UUID, List<Predicate<Player>>> onJoinActions;
    private Map<UUID, ProxyBlacklist> finders;

    public ConnectionListener(MopmBukkitPlugin plugin) {
        this.plugin = plugin;
        this.onJoinActions = new HashMap<>();
        this.finders = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getLookupBoss().scheduleLookup(event.getAddress(), optBl -> optBl.ifPresent(proxyBlacklist -> {
            AsyncProxyDetectedEvent detectedEvent = new AsyncProxyDetectedEvent(proxyBlacklist, event.getAddress(), event.getUniqueId());
            plugin.getServer().getPluginManager().callEvent(detectedEvent);
            this.onJoinActions.put(detectedEvent.getUuid(), detectedEvent.getActions());
            this.finders.put(event.getUniqueId(), proxyBlacklist);
        }));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (onJoinActions.containsKey(event.getPlayer().getUniqueId())) {
            ProxyBlacklist blacklist = finders.get(event.getPlayer().getUniqueId());
            plugin.getLogger().info("Player " + event.getPlayer().getName() + " connected with an open proxy at " + event.getPlayer().getAddress().getAddress().getHostAddress() + " as detected by " + blacklist.getName());
            if (event.getPlayer().hasPermission("mopm.bypass")) {
                plugin.getLogger().info("Player " + event.getPlayer().getName() + " bypassed open proxy detection by permission");
            }
            for (Predicate<Player> p : onJoinActions.get(event.getPlayer().getUniqueId())) {
                if (p.test(event.getPlayer())) {
                    return;
                }
            }
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), blacklist.getDefaultCommand()
                    .replace("%player%", event.getPlayer().getName())
                    .replace("%bl%", blacklist.getName()));
        }
    }
}
