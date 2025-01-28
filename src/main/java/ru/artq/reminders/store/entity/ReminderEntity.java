package ru.artq.reminders.store.entity;

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
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReminderPriority priority = ReminderPriority.NONE;

    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_list_id", nullable = false)
    private ReminderListEntity list;
}
