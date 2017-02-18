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

import lc.hex.mopm.api.common.ILookupBoss;
import lc.hex.mopm.api.common.IMopmPlugin;
import lc.hex.mopm.api.common.MopmAPI;
import lc.hex.mopm.api.common.MopmConfiguration;
import lc.hex.mopm.api.common.MopmInfo;
import lc.hex.mopm.api.common.ThreadMagic;
import lc.hex.mopm.common.MopmLookupBoss;
import lc.hex.mopm.api.common.ProxyBlacklist;
import com.torchmind.minecraft.annotation.Plugin;
import com.torchmind.minecraft.annotation.permission.Permission;
import com.torchmind.minecraft.annotation.permission.Permissions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

@Plugin(name = "mopm", author = "Hex", description = "Minecraft Open Proxy Monitor", version = MopmInfo.VERSION)
@Permissions({@Permission(name = "mopm.bypass", description = "Bypass all actions taken when connecting via open proxy", defaultValue = PermissionDefault.OP)})
public class MopmBukkitPlugin extends JavaPlugin implements IMopmPlugin {
    private MopmLookupBoss boss;
    private List<ProxyBlacklist> blacklists;
    private ConnectionListener listener;
    private MopmConfiguration config;

    @Override
    public void onEnable() {
        MopmAPI.setPlugin(this);
        this.blacklists = new ArrayList<>();
        this.listener = new ConnectionListener(this);
        this.config = new MopmConfiguration(this);
        this.config.readConfig((YamlConfiguration) this.getConfig());
        this.boss = new MopmLookupBoss(this.getConfig().getInt("advanced.workers"));

        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        MopmAPI.setPlugin(null);
    }


    @Override
    public ILookupBoss getLookupBoss() {
        return boss;
    }

    @Override
    public List<ProxyBlacklist> getAllProxyBlacklists() {
        return blacklists;
    }

    @Override
    public void addProxyBlacklist(ProxyBlacklist blacklist) {
        if (blacklist != null) {
            blacklists.add(blacklist);
        }
    }

    @Override
    public MopmConfiguration getConfiguration() {
        return config;
    }

    @Nonnull
    @Override
    public ThreadMagic getThreadMagic() {
        return this;
    }

    @Override
    public void runAsync(Runnable runnable) {
        this.getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    @Override
    public void runSync(Runnable runnable) {
        this.getServer().getScheduler().runTask(this, runnable);
    }
}
