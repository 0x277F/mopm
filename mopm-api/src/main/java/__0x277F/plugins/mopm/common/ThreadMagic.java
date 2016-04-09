package __0x277F.plugins.mopm.common;

public interface ThreadMagic {
    void runAsync(Runnable runnable);

    void runSync(Runnable runnable);
}
