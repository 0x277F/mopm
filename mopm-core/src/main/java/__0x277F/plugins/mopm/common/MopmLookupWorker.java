package __0x277F.plugins.mopm.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.logging.Logger;

public class MopmLookupWorker implements ILookupWorker, Runnable {
    private volatile boolean working = false;
    private volatile boolean running = false;
    private volatile boolean stopped = true;
    private volatile boolean paused = false;
    private MopmLookupBoss boss;
    private int id;
    private Logger logger;
    private String threadName;

    public MopmLookupWorker(MopmLookupBoss boss, int id) {
        this.boss = boss;
        this.id = id;
        this.threadName = "MOPM Worker Thread #" + id;
        this.logger = Logger.getLogger(threadName);
    }

    @Override
    public ILookupBoss getBoss() {
        return boss;
    }

    @Override
    public synchronized boolean isValid() {
        return !working && !stopped && running;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public synchronized void stop() {
        running = false;
    }

    @Override
    public synchronized void start() {
        running = true;
        paused = false;
    }

    @Override
    public synchronized void pause() {
        paused = true;
    }

    private void handleARequest(LookupRequest request) {
        boolean matched = false;
        ProxyBlacklist matcher = null;
        InetAddress inetAddress = request.getAddress();
        for (ProxyBlacklist blacklist : MopmAPI.getAllBlacklists()) {
            if (matched) {
                break;
            }
            if (inetAddress instanceof Inet4Address) {
                byte[] b = inetAddress.getAddress();
                StringBuilder builder = new StringBuilder();
                for (int i = b.length - 1; i >= 0; i--) {
                    builder.append(String.valueOf(b[i] & 0xFF));
                    builder.append(".");
                }
                builder.append(blacklist.getName());
                String lookup = builder.toString();
                try {
                    InetAddress.getByName(lookup);
                    matched = true;
                    matcher = blacklist;
                } catch (UnknownHostException e) {
                    // Do nothing, blacklist did not find entry
                }
            } else {
                logger.warning("Inet Address " + inetAddress.getCanonicalHostName() + " is not IPv4, somehow.");
                break;
            }
        }
        request.getCallback().accept(Optional.ofNullable(matcher));
    }

    private void handleRequest(LookupRequest request) {
        switch (request.getType()) {
            case A:
                handleARequest(request);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized or unsupported lookup type.");
        }
    }

    @Override
    public void run() {
        if (!MopmAPI.isEnabled()) {
            logger.severe("Attempted to start thread while plugin is not enabled!");
            boss.kill(this.id);
            return;
        }
        running = true;
        stopped = false;
        try {
            LookupRequest request;
            while (running) {
                if (paused) {
                    continue;
                }
                try {
                    request = boss.requestQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                if (request != null) {
                    working = true;
                    handleRequest(request);
                    working = false;
                }
            }
        } finally {
            stopped = true;
        }
    }

    public String getThreadName() {
        return threadName;
    }
}
