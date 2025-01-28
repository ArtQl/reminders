package ru.artq.reminders.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artq.reminders.store.entity.ListRemindersEntity;

import java.util.Optional;

@Repository
public interface ListRemindersRepository extends JpaRepository<ListRemindersEntity, Long> {

    Optional<ListRemindersEntity> findByName(String name);
}
