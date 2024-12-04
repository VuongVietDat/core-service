package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends BaseService implements NotificationService {
    
    private final LoyaltyEventGetwayClient loyaltyEventGetwayClient;

    private final CustomerBalanceService customerBalanceService;

    @Override
    public ResponseData<List<NotificationOutput>> sendNotification(String title,
                                                                   String content,
                                                                   String phoneNumber) {
      return loyaltyEventGetwayClient.sendNotification(
              RequestUtils.extractRequestId(),
              mappingNotificationRequest(title, content, phoneNumber));
    }

    public static NotificationInput mappingNotificationRequest(
            String title,
            String content,
            String phoneNumber){
        NotificationInput notificationInput = new NotificationInput();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS");
        String startTime = LocalDateTime.now().format(formatter);
        long date = new Date().getTime();
        notificationInput.setLanguage(Constants.LOCATION_VN);
        notificationInput.setRequestId(String.valueOf(date));
        notificationInput.setClientTime(startTime);
        notificationInput.setTransTime(String.valueOf(date));
        notificationInput.setTitle(title);
        notificationInput.setContent(content);
        notificationInput.setUserName(phoneNumber);
        return notificationInput;
    }
}
