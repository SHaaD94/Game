package com.shaad.game.net.session;

import com.google.common.base.Strings;
import com.shaad.game.net.Request;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.shaad.game.service.Constants.SESSION_ID;

public class SessionManager {
    private final Map<UUID, SessionData> token2Session = new ConcurrentHashMap<>();

    public UUID createSession() {
        UUID uuid = UUID.randomUUID();
        token2Session.put(uuid, new SessionData());
        return uuid;
    }

    public boolean setSessionValue(UUID sessionUUID, String objectName, Object value) {
        SessionData sessionData = token2Session.get(sessionUUID);
        if (sessionData == null) {
            return false;
        }
        sessionData.updateDate();
        sessionData.setValue(objectName, value);
        return true;
    }

    public boolean removeSessionValue(UUID sessionUUID, String objectName) {
        SessionData sessionData = token2Session.get(sessionUUID);
        if (sessionData == null) {
            return false;
        }
        sessionData.updateDate();
        sessionData.getSessionValues().remove(objectName);
        return true;
    }

    public <T> T getValueFromSession(UUID sessionUUID, String objectName, Class<T> clazz) {
        SessionData sessionData = token2Session.get(sessionUUID);
        if (sessionData == null) {
            return null;
        }
        sessionData.updateDate();
        return sessionData.getValue(objectName, clazz);
    }

    public void invalidateSession(UUID uuid) {
        token2Session.remove(uuid);
    }

    public UUID parseSessionId(Request request) {
        Optional<String> sessionIdCookie = request.getHeaders().stream().filter(x -> x.startsWith("Cookie")).filter(x -> x.contains(SESSION_ID))
                .findFirst();

        if (!sessionIdCookie.isPresent()) {
            return null;
        }
        String cookie = sessionIdCookie.get();
        String sessionUUID = cookie.replaceAll(".*" + SESSION_ID + "=", "")
                .replaceAll(";.*", "");
        if (Strings.isNullOrEmpty(sessionUUID)) {
            return null;
        }
        try {
            return UUID.fromString(sessionUUID);
        } catch (IllegalArgumentException e) {
            //handle illegal UUID
            return null;
        }
    }

    Map<UUID, SessionData> getSessions() {
        return this.token2Session;
    }

    @Data
    static class SessionData {
        private final Map<String, Object> sessionValues = new ConcurrentHashMap<>();

        private long updateDate = System.currentTimeMillis();

        public void setValue(String name, Object value) {
            sessionValues.put(name, value);
        }

        public <T> T getValue(String name, Class<T> valueType) {
            return valueType.cast(sessionValues.get(name));
        }

        void updateDate() {
            this.updateDate = System.currentTimeMillis();
        }
    }

}
