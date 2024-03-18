package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerBalanceRepository extends JpaRepository<CustomerBalance, Long> {

  @Query(
      value =
          "select cb.customerId           as customerId, "
              + "       c.customerName         as customerName, "
              + "       c.cifBank         as cifBank, "
              + "       c.cifWallet         as cifWallet, "
              + "       cb.totalAmount       as totalAmount, "
              + "       cb.lockAmount       as lockAmount, "
              + "       cb.availableAmount    as availableAmount "
              + "from CustomerBalance cb "
              + "         join Customer c on cb.customerId = cb.id "
              + "where cb.deleted = false "
              + "  and c.deleted = false "
              + "  and (:cifBank is null or c.cifBank = :cifBank) "
              + "  and (:cifWallet is null or c.cifWallet = :cifWallet)")
  CustomerBalanceProjection findCurrentBalance(String cifBank, String cifWallet);
}
