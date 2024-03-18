package vn.com.atomi.loyalty.core.mapper;

import org.mapstruct.Mapper;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface ModelMapper {

  CustomerBalanceOutput convertToCustomerBalanceOutput(
      CustomerBalanceProjection customerBalanceProjection);
}
