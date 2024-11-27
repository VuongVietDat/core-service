package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "C_MISSION_SEQUENTIAL")
public class CMissionSequential {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_MISSION_SEQUENTIAL_id_gen")
    @SequenceGenerator(name = "C_MISSION_SEQUENTIAL_id_gen", sequenceName = "C_MISSION_SEQUENTIAL_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "MISSION_ID")
    private Long missionId;

    @Column(name = "PARENT_ID")
    private Long parent;

    @Column(name = "MISSION_TYPE", length = 1)
    private String missionType;

    @Column(name = "ORDER_NO")
    private Short orderNo;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Size(max = 20)
    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Size(max = 20)
    @Column(name = "UPDATED_BY", length = 20)
    private String updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

}