package ru.artq.reminders.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reminder_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "reminderList",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<ReminderEntity> reminders = new ArrayList<>();
}
