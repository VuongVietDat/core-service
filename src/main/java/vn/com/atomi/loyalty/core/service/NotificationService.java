package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public interface NotificationService {

    public ResponseData<List<NotificationOutput>> sendNotification(
                                                                   String title,
                                                                   String content,
                                                                   String phoneNumber);
}
