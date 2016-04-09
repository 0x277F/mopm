package __0x277F.plugins.mopm.common;

import com.google.common.collect.HashBiMap;

import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MopmLookupBoss implements ILookupBoss {
    final BlockingQueue<LookupRequest> requestQueue = new LinkedBlockingQueue<>();
    private HashBiMap<Integer, MopmLookupWorker> workers;
    private int workerNum;
    private Logger logger;
    private Unsafe unsafe = new Unsafe();

    public MopmLookupBoss(int workerNum) {
        this.workers = HashBiMap.create();
        this.workerNum = workerNum;
        this.logger = Logger.getLogger("MOPM Lookup Boss");
    }

    public void start() {
        logger.info("Attempting to start " + workerNum + " worker threads...");
        for (int i = 1; i <= workerNum; i++) {
            MopmLookupWorker worker = new MopmLookupWorker(this, i);
            MopmAPI.plugin.runAsync(worker);
            workers.put(i, worker);
        }

        if (workers.size() != workerNum) {
            logger.warning("Unable to initialize " + (workerNum - workers.size()) + " workers.");
        }

        logger.info("Successfully started " + workers.size() + "/" + workerNum + " worker threads.");
        this.workerNum = workers.size();
    }

    @Override
    public void scheduleLookup(InetAddress address) {
        scheduleLookup(address, bl -> {
        });
    }

    @Override
    public void scheduleLookup(InetAddress address, Consumer<Optional<ProxyBlacklist>> callback) {
        LookupRequest request = new LookupRequest(address, LookupType.A, callback);
        requestQueue.add(request);
    }

    public String getLatestVersion() {
        throw new UnsupportedOperationException("Automatic updating has not been enabled.");
    }

    @Override
    public int getWorkers() {
        return workerNum;
    }

    @Override
    public Unsafe unsafe() {
        return unsafe;
    }

    synchronized void kill(int id) {
        logger.warning("Killing worker #" + id);
        MopmLookupWorker worker = workers.get(id);
        if (worker != null) {
            worker.stop();
        }
        workers.remove(id);
        workerNum = workers.size();
        logger.warning("        Worker thread #" + id + " killed by " + Thread.currentThread().getName());
    }
    public class Unsafe implements ILookupBoss.Unsafe {
        @Override
        public ILookupWorker getWorker(int id) {
            return MopmLookupBoss.this.workers.get(id);
        }

        @Override
        public synchronized void kill(int id) {
            MopmLookupBoss.this.kill(id);
        }
    }
}
