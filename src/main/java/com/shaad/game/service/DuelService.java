package com.shaad.game.service;

import com.shaad.game.domain.Duel;
import com.shaad.game.repository.DuelRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
public class DuelService {
    private static final long OPPONENT_SCAN_PERIOD = 5 * 1000;
    private final DuelRepository duelRepository;
    private final Queue<Long> fighterQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);

    private final Map<Long, Duel> userId2Duel = new ConcurrentHashMap<>();

    public DuelService(DuelRepository duelRepository) {
        this.duelRepository = duelRepository;

        scheduledService.scheduleAtFixedRate(() -> {
            log.debug("Searching for an opponents");

            while (fighterQueue.size() > 1) {
                Long firstUser = fighterQueue.poll();
                Long secondUser = fighterQueue.poll();

                log.info("Create battle for users {} and {}", firstUser, secondUser);
                Duel duel = this.duelRepository.createDuel(firstUser, secondUser);
                userId2Duel.put(firstUser, duel);
                userId2Duel.put(secondUser, duel);
            }
        }, OPPONENT_SCAN_PERIOD, OPPONENT_SCAN_PERIOD, TimeUnit.MILLISECONDS);
    }

    public void registerInQueue(Long userId) {
        fighterQueue.add(userId);
    }

    public void unregister(Long userId) {
        fighterQueue.add(userId);
    }

    public Duel getDuelByUserId(Long userId) {
        return userId2Duel.get(userId);
    }
}
