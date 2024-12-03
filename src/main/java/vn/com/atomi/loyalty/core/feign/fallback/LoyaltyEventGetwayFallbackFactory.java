package vn.com.atomi.loyalty.core.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.output.EGCBiometricOutput;
import vn.com.atomi.loyalty.core.dto.output.EGLoginOutput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoyaltyEventGetwayFallbackFactory implements FallbackFactory<LoyaltyEventGetwayClient> {

    @Override
    public LoyaltyEventGetwayClient create(Throwable cause) {
        log.error("An exception occurred when calling the AuthClient", cause);
        return new LoyaltyEventGetwayClient() {
            @Override
            public ResponseData<List<NotificationOutput>> sendNotification(
                    String requestId, NotificationInput request) {
                return new ResponseData<List<NotificationOutput>>().success(new ArrayList<>());
            }

            @Override
            public ResponseData<List<EGCBiometricOutput>> getLstCompleteBiometric(String requestId) {
                log.info("getLstCompleteBiometric: set default empty array");
                return new ResponseData<List<EGCBiometricOutput>>().success(new ArrayList<>());
            }

            @Override
            public ResponseData<String> automaticupdate(String requestId, String cifBank) {
                throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR, cause);
            }

            @Override
            public ResponseData<List<EGLoginOutput>> getListEGLogin(String requestId) {
                log.info("getListEGLogin: set default empty array");
                return new ResponseData<List<EGLoginOutput>>().success(new ArrayList<>());
            }

        };
    }
}
