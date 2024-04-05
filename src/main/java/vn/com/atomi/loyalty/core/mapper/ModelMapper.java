package vn.com.atomi.loyalty.core.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.input.CustomerKafkaInput;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.message.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.dto.projection.CustomerPointAccountProjection;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.CustomerBalanceHistory;
import vn.com.atomi.loyalty.core.entity.CustomerGroup;
import vn.com.atomi.loyalty.core.entity.CustomerGroupApproval;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.PointEventSource;
import vn.com.atomi.loyalty.core.enums.PointType;

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

  @Named("findDictionaryName")
  default String findDictionaryName(
      String code, @Context List<DictionaryOutput> dictionaryOutputs) {
    if (code == null || dictionaryOutputs == null) {
      return null;
    }
    for (DictionaryOutput value : dictionaryOutputs) {
      if (value != null && code.equals(value.getCode())) {
        return value.getName();
      }
    }
    return null;
  }

  @Named("findRankName")
  default String findRankName(String code, @Context List<RankOutput> rankOutputs) {
    if (code == null || rankOutputs == null) {
      return null;
    }
    for (RankOutput value : rankOutputs) {
      if (value != null && code.equals(value.getCode())) {
        return value.getName();
      }
    }
    return code;
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

  @Mapping(target = "transactionAt", source = "createdAt")
  ExternalCustomerBalanceHistoryOutput convertToExternalCustomerBalanceHistoryOutput(
      CustomerBalanceHistory customerBalanceHistories);

  List<ExternalCustomerBalanceHistoryOutput> convertToExternalCustomerBalanceHistoryOutputs(
      List<CustomerBalanceHistory> customerBalanceHistories);

  @Mapping(
      target = "uniqueTypeName",
      source = "customerPointAccountProjections.uniqueType",
      qualifiedByName = "findDictionaryName")
  CustomerPointAccountPreviewOutput convertToCustomerPointAccountPreviewOutput(
      CustomerPointAccountProjection customerPointAccountProjections,
      @Context List<DictionaryOutput> dictionaryOutputs);

  List<CustomerPointAccountPreviewOutput> convertToCustomerPointAccountPreviewOutputs(
      List<CustomerPointAccountProjection> customerPointAccountProjections,
      @Context List<DictionaryOutput> dictionaryOutputs);

  @Mapping(
      target = "uniqueTypeName",
      source = "pointAccountProjection.uniqueType",
      qualifiedByName = "findDictionaryName")
  CustomerPointAccountOutput convertToCustomerPointAccountOutput(
      CustomerPointAccountProjection pointAccountProjection,
      @Context List<DictionaryOutput> dictionaryOutputs);

  @Mapping(target = "transactionAt", source = "createdAt")
  CustomerBalanceHistoryOutput convertToCustomerBalanceHistoryOutput(
      CustomerBalanceHistory content);

  List<CustomerBalanceHistoryOutput> convertToCustomerBalanceHistoryOutputs(
      List<CustomerBalanceHistory> content);

  @Mapping(
      target = "genderName",
      source = "customer.gender",
      qualifiedByName = "findDictionaryName")
  @Mapping(
      target = "nationalityName",
      source = "customer.nationality",
      qualifiedByName = "findDictionaryName")
  @Mapping(
      target = "uniqueTypeName",
      source = "customer.uniqueType",
      qualifiedByName = "findDictionaryName")
  @Mapping(
      target = "customerTypeName",
      source = "customer.customerType",
      qualifiedByName = "findDictionaryName")
  CustomerOutput convertToCustomerOutput(
      Customer customer, @Context List<DictionaryOutput> dictionaryOutputs);

  CustomerOutput convertToCustomerOutput(Customer customer);

  List<CustomerOutput> convertToCustomerOutputs(List<Customer> customers);

  @Mapping(target = "eventSource", source = "eventSource")
  @Mapping(target = "expireAt", source = "expireAt")
  @Mapping(target = "customerId", source = "customerId")
  @Mapping(target = "ruleId", source = "ruleOutput.id")
  @Mapping(target = "ruleCode", source = "ruleOutput.code")
  @Mapping(target = "pointType", source = "pointType")
  @Mapping(target = "amount", source = "amount")
  @Mapping(target = "transactionAmount", source = "allocationPointTransactionInput.amount")
  TransactionInput convertToTransactionInput(
      AllocationPointTransactionInput allocationPointTransactionInput,
      PointType pointType,
      Long amount,
      Long customerId,
      RuleOutput ruleOutput,
      PointEventSource eventSource,
      LocalDate expireAt);

  @Mapping(target = "dob", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "issueDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  Customer fromCustomerKafkaInput(@MappingTarget Customer customer, CustomerKafkaInput input);
}
