package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.output.PartnersOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.PartnerRepository;
import vn.com.atomi.loyalty.core.service.PartnerService;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl extends BaseService implements PartnerService {
    private final PartnerRepository partnerRepository;
    @Override
    public ResponsePage<PartnersOutput> getListPartners(Status status, Pageable pageable) {
        var page =
                partnerRepository.findByCondition(status, pageable);

        if (CollectionUtils.isEmpty(page.getContent()))
            return new ResponsePage<>(page, new ArrayList<>());

        return new ResponsePage<>(page, modelMapper.convertToPartnerOutputs(page.getContent()));
    }
}
