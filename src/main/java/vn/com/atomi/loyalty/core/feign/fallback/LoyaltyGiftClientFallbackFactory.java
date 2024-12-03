package vn.com.atomi.loyalty.core.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyGiftClient;
import vn.com.atomi.loyalty.gift.dto.output.GiftPartnerOutput;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class LoyaltyGiftClientFallbackFactory  implements FallbackFactory<LoyaltyGiftClient> {


    @Override
    public LoyaltyGiftClient create(Throwable cause) {
        log.error("An exception occurred when calling the AuthClient", cause);

        return new LoyaltyGiftClient() {

            @Override
            public ResponseData<List<GiftPartnerOutput>> getGiftPartners(Status status, String effectiveDate, String name, Long partnerId, Long categoryId, String categoryCode) {
                return
                       new ResponseData<List<GiftPartnerOutput>>().success(new ArrayList<>());
            }
        };
    }
}
