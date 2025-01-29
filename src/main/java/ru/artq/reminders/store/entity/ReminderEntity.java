package ru.artq.reminders.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.artq.reminders.api.exception.BadRequestException;

import java.time.Instant;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ReminderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    private String description = "";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReminderPriority priority = ReminderPriority.NONE;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_list_id", nullable = false)
    private ReminderListEntity reminderList;

    public void updateDescription(String description) {
        if (description != null) this.description = description;
    }

    public void updatePriority(String priority) {
        if (priority == null) return;
        try {
            this.priority = ReminderPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info("Priority name: {} is not correct.", priority);
            throw new BadRequestException("Invalid priority value: %s".formatted(priority));
        }
    }
}
