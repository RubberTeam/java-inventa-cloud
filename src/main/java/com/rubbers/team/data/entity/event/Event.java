package com.rubbers.team.data.entity.event;

import com.rubbers.team.data.entity.issue.Issue;
import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Описывает контракт взаимодействия с модильным клиентом
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @NonNull
    @Builder.Default
    private UUID eventID = UUID.randomUUID();

    @NonNull
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @NonNull
    @Builder.Default
    private EventStatus eventStatus = EventStatus.OK;

    @NonNull
    private UUID itemID;

    @NonNull
    private UUID taskID;

    @Nullable
    private Issue issue;

    enum EventStatus {
        OK, ISSUE
    }
}
