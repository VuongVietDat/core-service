package vn.com.atomi.loyalty.core.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.dto.projection.CustomerPointAccountProjection;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  @Query(value = "select c_customer_id_seq.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  @Query(
      value =
          "select cb.customerId            as customerId, "
              + "       c.customerName     as customerName, "
              + "       c.cifBank          as cifBank, "
              + "       c.cifWallet        as cifWallet, "
              + "       c.uniqueValue      as uniqueValue, "
              + "       c.uniqueType       as uniqueType, "
              + "       cr.totalPoint      as rankPoint, "
              + "       cb.availableAmount as availableAmount "
              + "from Customer c "
              + "         join CustomerBalance cb on cb.customerId = c.id "
              + "         join CustomerRank cr on cr.customerId = c.id "
              + "where c.deleted = false "
              + "  and cb.deleted = false "
              + "  and cr.deleted = false "
              + "  and (:status is null or cb.status = :status) "
              + "  and (:customerId is null or c.id = :customerId) "
              + "  and (:customerName is null or c.customerName like :customerName) "
              + "  and (:cifWallet is null or c.cifWallet = :cifWallet) "
              + "  and (:cifBank is null or c.cifBank = :cifBank) "
              + "  and (:uniqueValue is null or c.uniqueValue = :uniqueValue) "
              + "  and (:pointFrom is null or cb.totalAmount >= :pointFrom) "
              + "  and (:pointTo is null or cb.totalAmount <= :pointTo) ")
  Page<CustomerPointAccountProjection> findByDeletedFalseAndPointAccount(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String cifWallet,
      String uniqueValue,
      Long pointFrom,
      Long pointTo,
      Pageable pageable);

  @Query(
      value =
          "select cb.customerId            as customerId, "
              + "       c.customerName     as customerName, "
              + "       c.cifBank          as cifBank, "
              + "       c.cifWallet        as cifWallet, "
              + "       c.uniqueValue      as uniqueValue, "
              + "       c.uniqueType       as uniqueType, "
              + "       c.status       as status, "
              + "       cr.code            as customerRankCode, "
              + "       cb.code     as customerBalanceCode, "
              + "       cr.rank            as rank, "
              + "       cb.totalAmount     as totalAmount, "
              + "       cb.lockAmount      as lockAmount, "
              + "       cr.totalPoint      as rankPoint, "
              + "       cb.availableAmount as availableAmount, "
              + "       cb.totalPointsUsed as totalPointsUsed "
              + "from Customer c "
              + "         join CustomerBalance cb on cb.customerId = c.id "
              + "         join CustomerRank cr on cr.customerId = c.id "
              + "where c.deleted = false "
              + "  and cb.deleted = false "
              + "  and cr.deleted = false "
              + "  and (:customerId is null or c.id = :customerId) ")
  CustomerPointAccountProjection findByDeletedFalseAndPointAccount(Long customerId);

  @Query(
      """
              select c from Customer c
              where c.deleted = false
              and (:status is null or c.status = :status)
              and (:customerId is null or c.id = :customerId)
              and (:customerName is null or c.customerName = :customerName)
              and (:cifBank is null or c.cifBank = :cifBank)
              and (:rank is null or c.rank = :rank)
              and (:segment is null or c.segment = :segment)
                      """)
  Page<Customer> findByCondition(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String rank,
      String segment,
      Pageable pageable);

  Optional<Customer> findByCifBank(String cif);

  @Query(
      value =
          "select c "
              + "from Customer c "
              + "where c.deleted = false "
              + "  and (:cifBank is null or c.cifBank = :cifBank) "
              + "  and (:cifWallet is null or c.cifWallet = :cifWallet)")
  Optional<Customer> findByDeletedFalseAndCifWallet(String cifWallet, String cifBank);
}
