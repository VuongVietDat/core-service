package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.output.PartnersOutput;
import vn.com.atomi.loyalty.core.enums.Status;

public interface PartnerService {

    ResponsePage<PartnersOutput> getListPartners(Status status, Pageable pageable);
}
