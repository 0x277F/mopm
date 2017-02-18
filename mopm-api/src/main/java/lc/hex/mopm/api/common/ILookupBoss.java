package lc.hex.mopm.api.common;

import java.net.InetAddress;
import java.util.Optional;
import java.util.function.Consumer;

public interface ILookupBoss {
    void scheduleLookup(InetAddress address);

    void scheduleLookup(InetAddress address, Consumer<Optional<ProxyBlacklist>> callback);

    int getWorkers();

    Unsafe unsafe();

    interface Unsafe {
        ILookupWorker getWorker(int id);

        void kill(int id);
    }
}
