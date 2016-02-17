package __0x277F.plugins.mopm.common;

import java.util.List;
import java.util.logging.Logger;

public interface IMopmPlugin {
    boolean isEnabled();
    IPLookupThread getLookupThread();
    List<ProxyBlacklist> getAllProxyBlacklists();
    void addProxyBlacklist(ProxyBlacklist blacklist);
    Logger getLogger();
    MopmConfiguration getConfiguration();
}
