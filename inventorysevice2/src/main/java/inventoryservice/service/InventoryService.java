package inventoryservice.service;


import inventoryservice.dto.InventoryResponse;
import inventoryservice.model.Inventory;
import inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    final private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {

            List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCode);
            if (inventories.size() != skuCode.size()) {
                List<String> foundSkuCodes = inventories.stream()
                        .map(Inventory::getSkuCode)
                        .toList();

                skuCode.removeAll(foundSkuCodes);
                throw new IllegalArgumentException("Product with skuCode not found in stock: " + skuCode);
            }


            return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                    .map(inventory ->
                            InventoryResponse.builder()
                                    .skuCode(inventory.getSkuCode())
                                    .isInStock(inventory.getQuantity() > 0)
                                    .build()
                    ).toList();

    }
}
