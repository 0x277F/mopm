package __0x277F.plugins.mopm.common;

public interface ILookupWorker {
    ILookupBoss getBoss();

    boolean isValid();

    int getId();

    void stop();

    void start();

    void pause();
}
