package __0x277F.plugins.mopm.bukkit;

import __0x277F.plugins.mopm.common.IMopmPlugin;
import __0x277F.plugins.mopm.common.IPLookupThread;
import __0x277F.plugins.mopm.common.MopmAPI;
import __0x277F.plugins.mopm.common.MopmConfiguration;
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
    private IPLookupThread lookupThread;
    private List<ProxyBlacklist> blacklists;
    private ConnectionListener listener;
    private MopmConfiguration config;

    @Override
    public void onEnable() {
        MopmAPI.setPlugin(this);
        this.lookupThread = new IPLookupThread();
        this.blacklists = new ArrayList<>();
        this.listener = new ConnectionListener(this);
        this.config = new MopmConfiguration(this);
        this.config.readConfig((YamlConfiguration) this.getConfig());

        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        MopmAPI.setPlugin(null);
    }

    @Override
    public IPLookupThread getLookupThread() {
        return lookupThread;
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
}
