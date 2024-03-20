package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.CustomerService;
import vn.com.atomi.loyalty.core.service.MasterDataService;
import vn.com.atomi.loyalty.core.utils.Constants;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends BaseService implements CustomerService {

  private final CustomerRepository customerRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  private final LoyaltyConfigClient loyaltyConfigClient;

  private final MasterDataService masterDataService;

  @Override
  public CustomerPointAccountOutput getCustomerPointAccount(Long customerId) {
    var pointAccountProjection = customerRepository.findByDeletedFalseAndPointAccount(customerId);
    if (pointAccountProjection == null || pointAccountProjection.getCustomerId() == null) {
      throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
    }
    // lấy danh sách rank để map tên rank
    var uniqueType = masterDataService.getDictionary(Constants.DICTIONARY_UNIQUE_TYPE, false);
    var out = super.modelMapper.convertToCustomerPointAccountOutput(pointAccountProjection, uniqueType);
    // lấy điểm gần hết hạn
    customerBalanceHistoryRepository
        .findPointAboutExpire(customerId)
        .ifPresent(
            customerBalanceHistory -> {
              out.setPointAboutExpire(
                  customerBalanceHistory.getAmount() - customerBalanceHistory.getAmountUsed());
              out.setMostRecentExpirationDate(customerBalanceHistory.getExpireAt());
            });
    return out;
  }

  @Override
  public ResponsePage<CustomerPointAccountPreviewOutput> getCustomerPointAccounts(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String cifWallet,
      String uniqueValue,
      Long pointFrom,
      Long pointTo,
      Pageable pageable) {
    var pointAccountProjectionPage =
        customerRepository.findByDeletedFalseAndPointAccount(
            status,
            customerId,
            customerName,
            cifBank,
            cifWallet,
            uniqueValue,
            pointFrom,
            pointTo,
            pageable);
    return new ResponsePage<>(
        pointAccountProjectionPage,
        super.modelMapper.convertToCustomerPointAccountPreviewOutput(
            pointAccountProjectionPage.getContent()));
  }
}
