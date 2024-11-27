package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.PkgPurchaseHistory;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.repository.PackageRepository;
import vn.com.atomi.loyalty.core.repository.PkgBenefitRepository;
import vn.com.atomi.loyalty.core.repository.PkgPurchaseHistoryRepository;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends BaseService implements NotificationService {
    
    private final LoyaltyEventGetwayClient loyaltyEventGetwayClient;

    @Override
    public ResponseData<List<NotificationOutput>> sendNotification(String caculationType, Long point, String phoneNumber) {
      return loyaltyEventGetwayClient.sendNotification(
              RequestUtils.extractRequestId(),
              mappingNotificationRequest(caculationType, point,phoneNumber));
    }

    public static NotificationInput mappingNotificationRequest(
            String caculationType,
            Long point, String phoneNumber){
        NotificationInput notificationInput = new NotificationInput();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS");
        String startTime = LocalDateTime.now().format(formatter);
        long date = new Date().getTime();
        notificationInput.setLanguage("VN");
        notificationInput.setRequestId(String.valueOf(date));
        notificationInput.setClientTime(startTime);
        notificationInput.setTransTime(String.valueOf(date));
        notificationInput.setTitle("Cộng điểm Loyalty");
        notificationInput.setContent("Số điểm của quý khách " + caculationType + point + " điểm Loyalty");
        notificationInput.setUserName(phoneNumber);
        return notificationInput;
    }
}
