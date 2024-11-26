package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CountPeriod;

import java.util.Optional;

@Repository
public interface CountPeriodRepository extends JpaRepository<CountPeriod, Long> {

    Optional<CountPeriod> findByCustomerId(Long customerId);
}
