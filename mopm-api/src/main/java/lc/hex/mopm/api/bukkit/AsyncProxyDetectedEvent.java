package lc.hex.mopm.api.bukkit;

import lc.hex.mopm.api.common.ProxyBlacklist;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

public class AsyncProxyDetectedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private ProxyBlacklist blacklist;
    private InetAddress address;
    private UUID uuid;
    private List<Predicate<Player>> actions;

    public AsyncProxyDetectedEvent(@Nonnull ProxyBlacklist blacklist, @Nonnull InetAddress address, @Nonnull UUID uuid) {
        super(true);
        this.blacklist = blacklist;
        this.address = address;
        this.uuid = uuid;
        this.actions = new ArrayList<>();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Schedule an action to be run synchronously when the connection is fully established.
     *
     * @param action Predicate that is run when the player connects. Return true to halt execution (should be done if the player is no longer online, etc.)
     */
    public void scheduleOnJoin(@Nonnull Predicate<Player> action) {
        actions.add(action);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public ProxyBlacklist getBlacklist() {
        return blacklist;
    }

    @Nonnull
    public InetAddress getAddress() {
        return address;
    }

    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    @Nonnull
    public List<Predicate<Player>> getActions() {
        return this.actions;
    }
}
