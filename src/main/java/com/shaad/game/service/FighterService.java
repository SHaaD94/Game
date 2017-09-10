package com.shaad.game.service;

import com.shaad.game.domain.Fighter;
import com.shaad.game.repository.FighterRepository;

public class FighterService {
    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    public Fighter findFighter(Long userId) {
        return fighterRepository.getUserFighter(userId);
    }

    public void increaseFighterStats(Long userId, boolean isWinner) {
        Fighter fighter = fighterRepository.getUserFighter(userId);
        int rating = fighter.getRating() + (isWinner ? 1 : -1);
        fighter.setRating(rating);
        fighter.setDamage(fighter.getDamage() + 1);
        fighter.setHealth(fighter.getHealth() + 1);
        fighterRepository.updateStats(fighter);
    }
}
