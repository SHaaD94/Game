package com.shaad.game.net;

import com.google.common.base.Strings;

import java.util.Map;
import java.util.Optional;
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

    public void invalidateSession(UUID uuid) {
        token2UserId.remove(uuid);
    }

    public UUID parseSessionId(Request request) {
        Optional<String> sessionIdCookie = request.getHeaders().stream().filter(x -> x.startsWith("Cookie")).filter(x -> x.contains("SessionId"))
                .findFirst();

        if (!sessionIdCookie.isPresent()) {
            return null;
        }
        String cookie = sessionIdCookie.get();
        String sessionUUID = cookie.replaceAll(".*SessionId=", "")
                .replaceAll(";.*", "");
        if (Strings.isNullOrEmpty(sessionUUID)) {
            return null;
        }
        return UUID.fromString(sessionUUID);
    }
}
