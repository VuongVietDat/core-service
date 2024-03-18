package vn.com.atomi.loyalty.core.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerGroupService {

  void createCustomerGroup(CustomerGroupInput customerGroupInput);

  ResponsePage<CustomerGroupApprovalPreviewOutput> getCustomerGroupApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable);

  CustomerGroupApprovalOutput getCustomerGroupApproval(Long id);

  List<ComparisonOutput> getCustomerGroupApprovalComparison(Long id);

  void recallCustomerGroupApproval(Long id);

  ResponsePage<CustomerGroupPreviewOutput> getCustomerGroups(
      Status status, String name, String code, Pageable pageable);

  CustomerGroupOutput getCustomerGroup(Long id);

  void updateCustomerGroup(Long id, CustomerGroupInput customerGroupInput);

  void approveCustomerGroup(ApprovalInput input);

  Boolean checkCustomerGroupExisted(Long id);
}
