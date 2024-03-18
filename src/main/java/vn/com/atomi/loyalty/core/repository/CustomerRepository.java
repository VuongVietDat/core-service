package vn.com.atomi.loyalty.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.Customer;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      value =
          "select c "
              + "from Customer c "
              + "where c.deleted = false "
              + "  and (:cifBank is null or c.cifBank = :cifBank) "
              + "  and (:cifWallet is null or c.cifWallet = :cifWallet)")
  Optional<Customer> findByDeletedFalseAndCifBankOrCifWallet(String cifBank, String cifWallet);
}
