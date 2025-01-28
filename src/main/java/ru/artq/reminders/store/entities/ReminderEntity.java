package ru.artq.reminders.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PriorityReminder priority = PriorityReminder.NONE;

    private Instant time = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_list_id", nullable = false)
    private ListRemindersEntity list;
}
