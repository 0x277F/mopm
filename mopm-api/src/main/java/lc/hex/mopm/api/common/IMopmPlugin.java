package lc.hex.mopm.api.common;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMopmPlugin extends ThreadMagic {
    boolean isEnabled();

    @Nonnull
    ILookupBoss getLookupBoss();

    @Nonnull
    List<ProxyBlacklist> getAllProxyBlacklists();

    void addProxyBlacklist(@Nullable ProxyBlacklist blacklist);

    @Nonnull
    Logger getLogger();

    @Nonnull
    MopmConfiguration getConfiguration();

    @Nonnull
    ThreadMagic getThreadMagic();
}
