package vn.com.atomi.loyalty.core.service.impl;

import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.UsePointInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.PointType;
import vn.com.atomi.loyalty.core.repository.CustomRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.repository.PointExpiredHistoryRepository;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;
import vn.com.atomi.loyalty.core.utils.Constants;
import vn.com.atomi.loyalty.core.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class CustomerBalanceServiceImpl extends BaseService implements CustomerBalanceService {

  private final CustomerBalanceRepository customerBalanceRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  private final CustomRepository customRepository;

  private final PointExpiredHistoryRepository pointExpiredHistoryRepository;

  @Override
  public CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet) {
    // bắt buộc truyền 1 trong 2 param
    if (StringUtils.isBlank(cifBank) && StringUtils.isBlank(cifWallet)) {
      throw new BaseException(ErrorCode.INPUT_INVALID);
    }
    CustomerBalanceProjection balanceProjection =
        customerBalanceRepository.findCurrentBalance(cifBank, cifWallet);
    if (balanceProjection == null || balanceProjection.getCustomerId() == null) {
      throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
    }
    // TODO
    return super.modelMapper.convertToCustomerBalanceOutput(balanceProjection);
  }

  @Override
  public ResponsePage<ExternalCustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId,
      ChangeType changeType,
      String startTransactionDate,
      String endTransactionDate,
      Pageable pageable) {
    var page =
        customerBalanceHistoryRepository.findHistory(
            customerId,
            changeType,
            PointType.CONSUMPTION_POINT,
            Utils.reformatStringDate(
                startTransactionDate,
                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
            Utils.reformatStringDate(
                endTransactionDate,
                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
            null,
            null,
            pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToExternalCustomerBalanceHistoryOutputs(page.getContent()));
  }

  @Override
  public ResponsePage<CustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId,
      ChangeType changeType,
      PointType pointType,
      String startTransactionDate,
      String endTransactionDate,
      String startExpiredDate,
      String endExpiredDate,
      Pageable pageable) {
    var page =
        customerBalanceHistoryRepository.findHistory(
            customerId,
            changeType,
            pointType,
            Utils.reformatStringDate(
                startTransactionDate,
                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
            Utils.reformatStringDate(
                endTransactionDate,
                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
            Utils.convertToLocalDate(startExpiredDate),
            Utils.convertToLocalDate(endExpiredDate),
            pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToCustomerBalanceHistoryOutputs(page.getContent()));
  }

  @Override
  public void executeTransactionMinus(UsePointInput usePointInput) {}

  @Override
  public void executePointExpiration() {
    pointExpiredHistoryRepository
        .findTop1ByDeletedFalseOrderByExpiredAtDesc()
        .ifPresentOrElse(
            pointExpiredHistory -> {
              if (pointExpiredHistory.getEndAt() == null) {
                LOGGER.info("Another process is running");
              } else {
                if (pointExpiredHistory.getExpiredAt().isBefore(LocalDate.now())) {
                  LOGGER.info("Processed up to the latest date");
                } else {
                  customRepository.expiredAmount(
                      UUID.randomUUID().toString(),
                      pointExpiredHistory.getExpiredAt().plusDays(1),
                      Constants.EXPIRED_POINT_CONTENT,
                      PointType.CONSUMPTION_POINT);
                }
              }
            },
            () -> {
              LOGGER.info("Perform processing for the first time");
              customRepository.expiredAmount(
                  UUID.randomUUID().toString(),
                  LocalDate.now().minusDays(1),
                  Constants.EXPIRED_POINT_CONTENT,
                  PointType.CONSUMPTION_POINT);
            });
  }
}
