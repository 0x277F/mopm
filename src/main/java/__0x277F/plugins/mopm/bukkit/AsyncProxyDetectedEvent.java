package __0x277F.plugins.mopm.bukkit;

import __0x277F.plugins.mopm.common.ProxyBlacklist;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class AsyncProxyDetectedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private ProxyBlacklist blacklist;
    private InetAddress address;
    private UUID uuid;
    private List<Predicate<Player>> actions;

    public AsyncProxyDetectedEvent(ProxyBlacklist blacklist, InetAddress address, UUID uuid) {
        this.blacklist = blacklist;
        this.address = address;
        this.uuid = uuid;
        this.actions = new ArrayList<>();
    }

    /**
     * Schedule an action to be run synchronously when the connection is fully established.
     * @param action Predicate that is run when the player connects. Return true to halt execution (should be done if the player is no longer online, etc.)
     */
    public void scheduleOnJoin(Predicate<Player> action){
        actions.add(action);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ProxyBlacklist getBlacklist() {
        return blacklist;
    }

    public InetAddress getAddress() {
        return address;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Predicate<Player>> getActions() {
        return this.actions;
    }
}
