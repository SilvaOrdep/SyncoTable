package com.ordep.syncotable.service.card;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.CardRepository;
import com.ordep.syncotable.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class CardLockService {

    private UserRepository users;
    private CardRepository cards;
    private CardService cardService;
    private final int LOCK_TIMEOUT_MINUTES = 5;

    @Transactional
    public boolean acquireLock(Long cardId, Long userId) {
        Card card = cardService.findCardById(cardId);
        User user = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if(isLockValid(card) && !card.getLockedBy().getId().equals(userId)) {
            return false;
        }

        card.setLockedBy(user);
        card.setLockedAt(Instant.now());
        Card saved = cards.save(card);

        return true;
    }

    @Transactional
    public boolean releaseLock(Long cardId, Long userId) {
        Card card = cardService.findCardById(cardId);

        if(isLockValid(card) && card.getLockedBy().getId().equals(userId)) {
            card.setLockedBy(null);
            card.setLockedAt(null);
            cards.save(card);
            return true;
        }

        return false;
    }

    @Transactional
    public void heartbeat(Long cardId, Long userId) {
        Card card = cardService.findCardById(cardId);

        if(card.getLockedBy() != null && card.getLockedBy().getId().equals(userId)) {
            card.setLockedAt(Instant.now());
            cards.save(card);
        }

    }

    public String getLockedByUsername(Long cardId) {
        Card card = cardService.findCardById(cardId);

        if (isLockValid(card)) {
            return card.getLockedBy().getUsername();
        }

        return null;
    }

    public boolean canEditCard(Long cardId, User user) {
        Card card = cardService.findCardById(cardId);

        if (!isLockValid(card)) {
            return true;
        }

        return card.getLockedBy().getId().equals(user.getId());
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void cleanupOrphanLocks() {
        cards.findAll().stream()
                .filter(card -> card.getLockedBy() != null && !isLockValid(card))
                .forEach(card -> {
                    card.setLockedBy(null);
                    card.setLockedAt(null);
                    cards.save(card);
                });
    }

    private boolean isLocked(Long cardId) {
        Card card = cardService.findCardById(cardId);

        return isLockValid(card);
    }


    private boolean isLockValid(Card card) {
        if (card.getLockedBy() == null || card.getLockedAt() == null) {
            return false;
        }
        
        Duration lockDuration = Duration.between(card.getLockedAt(), Instant.now());
        return lockDuration.toMinutes() < LOCK_TIMEOUT_MINUTES;
    }
    
}
