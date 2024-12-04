package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.PartnerInput;
import vn.com.atomi.loyalty.core.dto.output.PartnersOutput;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.PartnerRepository;
import vn.com.atomi.loyalty.core.service.PartnerService;
import vn.com.atomi.loyalty.core.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl extends BaseService implements PartnerService {
    private final PartnerRepository partnerRepository;
    @Override
    public ResponsePage<PartnersOutput> getListPartners(Status status,String keyword,String startDate, Pageable pageable) {
        var page =
                partnerRepository.findByCondition(status, keyword, Utils.convertToLocalDate(startDate), pageable);

        if (CollectionUtils.isEmpty(page.getContent()))
            return new ResponsePage<>(page, new ArrayList<>());

        return new ResponsePage<>(page, modelMapper.convertToPartnerOutputs(page.getContent()));
    }

    public void createPartner(PartnerInput partnerInput) {
        var startDate = Utils.convertToLocalDate(partnerInput.getStartDate());
        var endDate = Utils.convertToLocalDate(partnerInput.getEndDate());
        if (partnerRepository
                .findByDeletedFalseAndCode(partnerInput.getCode())
                .isPresent()) {
            throw new BaseException(ErrorCode.EXISTED_PARTNER_CODE);
        }
        var partner = super.modelMapper.createPartner(partnerInput, startDate, endDate);
        partnerRepository.save(partner);
    }

    @Override
    public void update(Long id, PartnerInput input) {
        // lấy record hiện tại
        var record =
                partnerRepository
                        .findByDeletedFalseAndId(id)
                        .orElseThrow(() -> new BaseException(ErrorCode.PARTNER_NOT_EXISTED));
        // mapping new values
        var newPartner = super.modelMapper.mappingToPartner(record, input);
        // lưu
        partnerRepository.save(newPartner);
    }

    @Override
    public PartnersOutput get(Long id) {
        var giftPartner =
                partnerRepository
                        .findByDeletedFalseAndId(id)
                        .orElseThrow(() -> new BaseException(ErrorCode.PARTNER_NOT_EXISTED));
        var out =  super.modelMapper.convertToPartnerOutput(giftPartner);
        return out;
    }
}
