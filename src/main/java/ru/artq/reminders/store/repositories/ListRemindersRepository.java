package ru.artq.reminders.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artq.reminders.store.entities.ListRemindersEntity;

@Repository
public interface ListRemindersRepository extends JpaRepository<ListRemindersEntity, Long> {
}
