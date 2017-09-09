package com.shaad.game.net;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    Map<UUID, Long> token2UserId = new ConcurrentHashMap<>();

    public Long getUserByToken(UUID token) {
        return token2UserId.get(token);
    }

    public UUID createSession(long userId) {
        UUID uuid = UUID.randomUUID();
        token2UserId.put(uuid, userId);
        return uuid;
    }
}
