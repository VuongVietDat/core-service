package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.core.dto.message.AllocationPointMessage;

/**
 * @author haidv
 * @version 1.0
 */
public interface AllocationPointService {

  void handlerAllocationPointEvent(AllocationPointMessage allocationPointMessage);
}
