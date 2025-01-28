package ru.artq.reminders.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "list_reminders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListRemindersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "list", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReminderEntity> reminders = new ArrayList<>();
}
