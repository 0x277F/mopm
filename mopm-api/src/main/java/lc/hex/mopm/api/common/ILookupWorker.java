package lc.hex.mopm.api.common;

public interface ILookupWorker {
    ILookupBoss getBoss();

    boolean isValid();

    int getId();

    void stop();

    void start();

    void pause();
}
