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
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reminderList",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<ReminderEntity> reminders = new ArrayList<>();
}
