package __0x277F.plugins.mopm.common;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class IPLookupThread extends Thread {
    private Queue<InetAddress> lookupQueue;
    private Map<InetAddress, Consumer<ProxyBlacklist>> callbacks;

    public IPLookupThread() {
        super("MOPM IP Lookup Thread");
        lookupQueue = new ConcurrentLinkedQueue<>();
        callbacks = new HashMap<>();
    }

    public void scheduleLookup(InetAddress address, Consumer<ProxyBlacklist> callback) {
        lookupQueue.add(address);
        callbacks.put(address, callback);
    }

    @Override
    public void run() {
        while(MopmAPI.isEnabled()) {
            if(!lookupQueue.isEmpty()) {
                InetAddress address = lookupQueue.remove();
                boolean matched = false;
                l: for(ProxyBlacklist blacklist : MopmAPI.getAllBlacklists()) {
                    if(blacklist.matches(address)) {
                        callbacks.get(address).accept(blacklist);
                        callbacks.remove(address);
                        matched = true;
                        break l;
                    }
                }
                if(!matched) {
                    callbacks.get(address).accept(null);
                    callbacks.remove(address);
                }
            }
        }
    }
}
