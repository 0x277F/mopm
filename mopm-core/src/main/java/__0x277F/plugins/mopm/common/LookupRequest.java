package __0x277F.plugins.mopm.common;

import java.net.InetAddress;
import java.util.Optional;
import java.util.function.Consumer;

public class LookupRequest {
    private final InetAddress address;
    private final LookupType type;
    private final Consumer<Optional<ProxyBlacklist>> callback;

    public LookupRequest(InetAddress address, LookupType type, Consumer<Optional<ProxyBlacklist>> callback) {
        this.address = address;
        this.type = type;
        this.callback = callback;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Consumer<Optional<ProxyBlacklist>> getCallback() {
        return callback;
    }

    public LookupType getType() {
        return type;
    }
}
