package lc.hex.mopm.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Optional;
import java.util.logging.Logger;

import lc.hex.mopm.api.common.ILookupBoss;
import lc.hex.mopm.api.common.ILookupWorker;
import lc.hex.mopm.api.common.MopmAPI;
import lc.hex.mopm.api.common.ProxyBlacklist;

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
        ProxyBlacklist matcher = null;
        InetAddress inetAddress = request.getAddress();
        for (ProxyBlacklist blacklist : MopmAPI.getAllBlacklists()) {
            if (inetAddress instanceof Inet4Address) {
                if (blacklist.test(inetAddress)) {
                    matcher = blacklist;
                    break;
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
