package ru.artq.reminders.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.artq.reminders.store.entity.ReminderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
    Optional<ReminderEntity> findByTitle(String title);

    boolean existsByTitle(String title);

    List<ReminderEntity> findByUserId(Long userId);

    List<ReminderEntity> findByRemindBeforeAndUser_IdAndCompletedIsFalse(LocalDateTime dateTime, Long userId);

    @Modifying
    @Query("update ReminderEntity r set r.completed = true " +
            "where r.id = :reminderId")
    int markAsCompleted(Long reminderId);
}
