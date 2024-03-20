package vn.com.atomi.loyalty.core.service;

import java.util.List;
import vn.com.atomi.loyalty.core.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface MasterDataService {

  List<DictionaryOutput> getDictionary();

  List<DictionaryOutput> getDictionary(String type, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(Status status);

  List<DictionaryOutput> getDictionary(String type, Status status, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(List<DictionaryOutput> node, String type, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, Status status, boolean isSubLeaf);
}
