package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CustomerRank;

@Repository
public interface CustomerRankRepository extends JpaRepository<CustomerRank, Long> {
  @Query(value = "select c_customer_rank_id_seq.nextval from DUAL", nativeQuery = true)
  Long getSequence();
}
