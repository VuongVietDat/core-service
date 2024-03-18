package vn.com.atomi.loyalty.core.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.entity.CustomerGroup;
import vn.com.atomi.loyalty.core.entity.CustomerGroupApproval;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface ModelMapper {

  default String getApprover(ApprovalStatus approvalStatus, String updateBy) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateBy;
  }

  default LocalDateTime getApproveDate(ApprovalStatus approvalStatus, LocalDateTime updateAt) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateAt;
  }

  CustomerBalanceOutput convertToCustomerBalanceOutput(
      CustomerBalanceProjection customerBalanceProjection);

  @Mapping(target = "creator", source = "createdBy")
  @Mapping(target = "creationDate", source = "createdAt")
  @Mapping(
      target = "approveDate",
      expression = "java(getApproveDate(approval.getApprovalStatus(), approval.getUpdatedAt()))")
  @Mapping(
      target = "approver",
      expression = "java(getApprover(approval.getApprovalStatus(), approval.getUpdatedBy()))")
  CustomerGroupApprovalPreviewOutput convertToCustomerGroupApprovalPreviewOutput(
      CustomerGroupApproval approval);

  List<CustomerGroupApprovalPreviewOutput> convertToCustomerGroupApprovalPreviewOutputs(
      List<CustomerGroupApproval> customerGroupApprovals);

  List<CustomerGroupPreviewOutput> convertToCustomerGroupPreviewOutputs(
      List<CustomerGroup> content);

  CustomerGroupApprovalOutput convertCustomerGroupApprovalOutput(
      CustomerGroupApproval customerGroupApproval);

  CustomerGroupOutput convertCustomerGroupOutput(CustomerGroup customerGroup);

  CustomerGroup convertToCustomerGroup(CustomerGroupApproval customerGroupApproval);

  CustomerGroup convertToCustomerGroup(
      @MappingTarget CustomerGroup currentGroup, CustomerGroupApproval customerGroupApproval);

  CustomerGroup convertToCustomerGroup(
      @MappingTarget CustomerGroup customerGroup, CustomerGroupInput customerGroupInput);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "customerGroupId", source = "customerGroup.id")
  @Mapping(target = "approvalType", source = "approvalType")
  @Mapping(target = "approvalStatus", source = "approvalStatus")
  CustomerGroupApproval convertToGroupApproval(
      CustomerGroup customerGroup, ApprovalStatus approvalStatus, ApprovalType approvalType);
}
