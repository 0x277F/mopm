package lc.hex.mopm.api.common;

public interface ThreadMagic {
    void runAsync(Runnable runnable);

    void runSync(Runnable runnable);
}
