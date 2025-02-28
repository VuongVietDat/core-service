package vn.com.atomi.loyalty.base.data;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base class for all entities in the application. <br>
 * An entity represents a single result row (record) retrieved from the database and provides
 * methods for retrieving and storing properties associated with the row (record).
 *
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @CreatedDate
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @CreatedBy
  @Column(name = "CREATED_BY")
  private String createdBy;

  @LastModifiedBy
  @Column(name = "UPDATED_BY")
  private String updatedBy;

  @Column(name = "IS_DELETED")
  private boolean deleted = false;
}
