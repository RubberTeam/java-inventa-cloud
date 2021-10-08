package com.rubbers.team.data.entity.audit;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность для сохранения событий в аудит
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Id
    @NonNull
    @Builder.Default
    private UUID auditID = UUID.randomUUID();

    @Builder.Default
    private LocalDateTime auditEvenTime = LocalDateTime.now();

    @Builder.Default
    private String auditDescription = "";

}