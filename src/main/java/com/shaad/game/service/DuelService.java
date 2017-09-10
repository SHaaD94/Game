package com.shaad.game.service;

import com.shaad.game.domain.Duel;
import com.shaad.game.domain.DuelStatus;
import com.shaad.game.domain.FighterState;
import com.shaad.game.repository.DuelRepository;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DuelService {
    private static final long OPPONENT_SCAN_PERIOD = 5 * 1000;
    private static final String ATTACK_LOG_TEMPLATE = "%%{0}%% attacked %%{1}%% for {2} damage";
    private static final String KILLED_LOG_TEMPLATE = "%%{0}%% killed %%{1}%%";
    private final DuelRepository duelRepository;
    private final FighterService fighterService;
    private final Queue<Long> fighterQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);

    private final Map<Duel, Lock> duel2Lock = new ConcurrentHashMap<>();
    private final Map<Long, Duel> userId2Duel = new ConcurrentHashMap<>();

    public DuelService(DuelRepository duelRepository, FighterService fighterService) {
        this.duelRepository = duelRepository;
        this.fighterService = fighterService;

        scheduledService.scheduleAtFixedRate(() -> {
            log.debug("Searching for an opponents");

            while (fighterQueue.size() > 1) {
                Long firstUser = fighterQueue.poll();
                Long secondUser = fighterQueue.poll();

                log.info("Create battle for users {} and {}", firstUser, secondUser);
                Duel duel = this.duelRepository.createDuel(firstUser, secondUser);

                duel.setFighter1(new FighterState(this.fighterService.findFighter(firstUser)));
                duel.setFighter2(new FighterState(this.fighterService.findFighter(secondUser)));
                userId2Duel.put(firstUser, duel);
                userId2Duel.put(secondUser, duel);
            }
        }, OPPONENT_SCAN_PERIOD, OPPONENT_SCAN_PERIOD, TimeUnit.MILLISECONDS);
    }

    public void registerInQueue(Long userId) {
        fighterQueue.add(userId);
    }

    public void unregister(Long userId) {
        fighterQueue.remove(userId);
    }

    public Duel getDuelByUserId(Long userId) {
        return userId2Duel.get(userId);
    }

    public Duel handleAttack(long userId) {
        Duel duel = userId2Duel.get(userId);
        if (duel == null) {
            log.error("Failed to find duel");
            return null;
        }
        Lock lock = duel2Lock.computeIfAbsent(duel, d -> new ReentrantLock());
        lock.lock();

        if (duel.getStatus() == DuelStatus.FINISHED) {
            return duel;
        }

        try {
            FighterState yourFighter = duel.getYourFighter(userId);
            FighterState opponentFighter = duel.getOpponentFighter(userId);
            Long opponentUserId = duel.getOpponentUserId(userId);
            opponentFighter.setHealth(opponentFighter.getHealth() - yourFighter.getDamage());

            duel.addLogLine(MessageFormat.format(ATTACK_LOG_TEMPLATE,
                    userId, opponentUserId, yourFighter.getDamage()));

            if (opponentFighter.getHealth() <= 0) {
                duel.addLogLine(MessageFormat.format(KILLED_LOG_TEMPLATE, userId, opponentUserId));
                duel.setStatus(DuelStatus.FINISHED);
                duelRepository.updateDuel(duel);
                userId2Duel.remove(userId);
                userId2Duel.remove(duel.getOpponentUserId(userId));
                fighterService.increaseFighterStats(userId, true);
                fighterService.increaseFighterStats(opponentUserId, false);
                duel2Lock.remove(duel);
                return duel;
            }

            duelRepository.updateDuelLogs(duel);
            duel2Lock.remove(duel);
            return duel;

        } finally {
            lock.unlock();
        }
    }
}
