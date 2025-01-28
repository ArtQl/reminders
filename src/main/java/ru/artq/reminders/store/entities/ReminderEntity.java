package ru.artq.reminders.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private LocalDate date = LocalDate.now();

    private LocalTime time = LocalTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_list_id", nullable = false)
    private ListRemindersEntity list;
}
