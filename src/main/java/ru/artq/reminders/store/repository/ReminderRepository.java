package ru.artq.reminders.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
    Optional<ReminderEntity> findByName(String name);

    Optional<ReminderEntity> findByNameAndReminderList(String name, ReminderListEntity reminderList);

    boolean existsByName(String name);
}
