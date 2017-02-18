package lc.hex.mopm.api.common;

import java.net.InetAddress;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ProxyBlacklist implements Predicate<InetAddress> {
    private final BiPredicate<String, InetAddress> matcher;
    private String rootAddress;
    private String name;
    private String defaultCommand;

    public ProxyBlacklist(String rootAddress, String name, BiPredicate<String, InetAddress> matcher, String defaultCommand) {
        this.rootAddress = rootAddress;
        this.name = name;
        this.matcher = matcher;
        this.defaultCommand = defaultCommand;
    }

    public String getRootAddress() {
        return rootAddress;
    }

    public boolean test(InetAddress address) {
        return matcher.test(this.rootAddress, address);
    }

    public String getDefaultCommand() {
        return defaultCommand;
    }

    public String getName() {
        return name;
    }
}
