package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.LoginPeriod;

import java.util.Optional;

@Repository
public interface LoginPeriodRepository extends JpaRepository<LoginPeriod, Long> {

    LoginPeriod findByCustomerId(Long customerId);
}
