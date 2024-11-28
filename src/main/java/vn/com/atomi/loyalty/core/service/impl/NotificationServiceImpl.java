package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;
import vn.com.atomi.loyalty.core.service.NotificationService;

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
