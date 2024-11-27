package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.input.PurchaseChainMissionInput;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public interface NotificationService {

    public ResponseData<List<NotificationOutput>> sendNotification(String caculationType, Long point, String phoneNumber);
}
