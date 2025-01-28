package ru.artq.reminders.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artq.reminders.store.entity.ReminderListEntity;

import java.util.Optional;

@Repository
public interface ReminderListRepository extends JpaRepository<ReminderListEntity, Long> {

    Optional<ReminderListEntity> findByName(String name);
}
