package vn.com.atomi.loyalty.core.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyCommonClient;
import vn.com.atomi.loyalty.core.repository.redis.MasterDataRepository;
import vn.com.atomi.loyalty.core.service.MasterDataService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl extends BaseService implements MasterDataService {

  private final LoyaltyCommonClient loyaltyCommonClient;

  private final MasterDataRepository masterDataRepository;

  @Override
  public List<DictionaryOutput> getDictionary() {
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, null, null)
          .getData();
    }
    return out;
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type, boolean isSubLeaf) {
    if (StringUtils.isEmpty(type)) {
      return this.getDictionary();
    }
    var node = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(node)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, null, isSubLeaf)
          .getData();
    }
    return this.getDictionary(node, type, isSubLeaf);
  }

  @Override
  public List<DictionaryOutput> getDictionary(Status status) {
    if (status == null) {
      return this.getDictionary();
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, status, null)
          .getData();
    }
    return out.stream().filter(v -> v.getStatus().equals(status)).collect(Collectors.toList());
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type, Status status, boolean isSubLeaf) {
    if (StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary();
    }
    if (!StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary(type, isSubLeaf);
    }
    if (StringUtils.isEmpty(type) && status != null) {
      return this.getDictionary(status);
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, status, isSubLeaf)
          .getData();
    }
    return this.getDictionary(out, type, status, isSubLeaf);
  }

  @Override
  public List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, boolean isSubLeaf) {
    List<DictionaryOutput> leafs =
        node.stream().filter(v -> type.equals(v.getParentCode())).collect(Collectors.toList());
    return this.appendSubLeaf(node, isSubLeaf, leafs);
  }

  @Override
  public List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, Status status, boolean isSubLeaf) {
    List<DictionaryOutput> leafs =
        node.stream()
            .filter(v -> type.equals(v.getParentCode()) && v.getStatus().equals(status))
            .collect(Collectors.toList());
    return this.appendSubLeaf(node, isSubLeaf, leafs);
  }

  private List<DictionaryOutput> appendSubLeaf(
      List<DictionaryOutput> node, boolean isSubLeaf, List<DictionaryOutput> leafs) {
    if (isSubLeaf) {
      List<String> leafCode = leafs.stream().map(DictionaryOutput::getCode).toList();
      var subLeaf = node.stream().filter(v -> leafCode.contains(v.getParentCode())).toList();
      leafs.addAll(subLeaf);
    }
    return leafs;
  }
}
