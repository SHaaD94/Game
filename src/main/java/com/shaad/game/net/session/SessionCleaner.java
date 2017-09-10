package com.shaad.game.net.session;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class SessionCleaner {
    private static final int SESSION_LIFE_TIME = 60 * 60 * 1000; // one hour
    private static final int CLEANING_PERIOD = 60 * 1000; // one minute
    private final SessionManager sessionManager;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public SessionCleaner(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void run() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Set<UUID> sessionsToRemove = sessionManager.getSessions()
                    .entrySet()
                    .stream()
                    .filter(x -> x.getValue().getUpdateDate() + SESSION_LIFE_TIME < System.currentTimeMillis())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            if (!sessionsToRemove.isEmpty()) {
                log.info("Removed {} outdated session", sessionsToRemove.size());
                sessionsToRemove.forEach(sessionManager::invalidateSession);
            }
        }, CLEANING_PERIOD, CLEANING_PERIOD, TimeUnit.MILLISECONDS);
    }
}
