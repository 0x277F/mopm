package __0x277F.plugins.mopm.common;

import java.util.List;

public class MopmAPI {
    static IMopmPlugin plugin;

    public static void setPlugin(IMopmPlugin plugin) {
        MopmAPI.plugin = plugin;
    }

    public static boolean isEnabled() {
        return plugin.isEnabled();
    }

    public static MopmConfiguration getConfiguration() {
        return plugin.getConfiguration();
    }

    public static List<ProxyBlacklist> getAllBlacklists() {
        return plugin.getAllProxyBlacklists();
    }
}
