package inventoryservice.controller;

import inventoryservice.dto.InventoryResponse;
import inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    final private InventoryService repositoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam  List<String> skuCode ){
        return repositoryService.isInStock(skuCode);
    }
}
