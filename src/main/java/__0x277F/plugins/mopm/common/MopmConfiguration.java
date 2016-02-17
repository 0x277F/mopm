package __0x277F.plugins.mopm.common;

import org.bukkit.configuration.file.YamlConfiguration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MopmConfiguration {
    private IMopmPlugin plugin;

    public MopmConfiguration(IMopmPlugin plugin) {
        this.plugin = plugin;
    }

    public void readConfig(YamlConfiguration config) {
        for(String name : config.getStringList("blacklists")) {
            ProxyBlacklist blacklist = new ProxyBlacklist(config.getString("blacklists." + name + ".address"), name, (bl, inetAddress) -> {
                if(inetAddress instanceof Inet4Address) {
                    String addr = inetAddress.getHostAddress();
                    String[] segs = addr.split(".");
                    StringBuilder builder = new StringBuilder();
                    for(int i = segs.length - 1; i >=0; i--) {
                        builder.append(segs[i]);
                        builder.append(".");
                    }
                    builder.append(bl);
                    String lookup = builder.toString();
                    try {
                        InetAddress.getByName(lookup);
                    } catch (UnknownHostException e) {
                        return false;
                    }
                    return true;
                } else {
                    plugin.getLogger().warning("Inet Address " + inetAddress.getCanonicalHostName() + " is not IPv4, somehow.");
                    return false;
                }
            }, config.getString("blacklists." + name + ".on-match"));
            plugin.addProxyBlacklist(blacklist);
        }
    }
}
