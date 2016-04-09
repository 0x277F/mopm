package __0x277F.plugins.mopm.bukkit;

import __0x277F.plugins.mopm.common.ILookupBoss;
import __0x277F.plugins.mopm.common.IMopmPlugin;
import __0x277F.plugins.mopm.common.MopmAPI;
import __0x277F.plugins.mopm.common.MopmConfiguration;
import __0x277F.plugins.mopm.common.MopmLookupBoss;
import __0x277F.plugins.mopm.common.ProxyBlacklist;
import com.torchmind.minecraft.annotation.Plugin;
import com.torchmind.minecraft.annotation.permission.Permission;
import com.torchmind.minecraft.annotation.permission.Permissions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Plugin(name = "mopm", author = "__0x277F", description = "Minecraft Open Proxy Monitor", version = "1.0-SNAPSHOT")
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
        blacklists.add(blacklist);
    }

    @Override
    public MopmConfiguration getConfiguration() {
        return config;
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
