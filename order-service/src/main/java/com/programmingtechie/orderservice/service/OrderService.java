package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.exception.result.ProductNotInStockException;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    final private OrderRepository orderRepository;
    final private  WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) throws Exception {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        //Convert list of "OrderLineItemsDto" to list of "OrderLineItems"
        //1.Method 1:
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        //1.Method 2:
//        List<OrderLineItems> orderLineItemsList = new ArrayList<>();
//        for(OrderLineItemsDto orderLineItemsDto: orderRequest.getOrderLineItemsDtoList()){
//            OrderLineItems orderLineItems  = mapToDto(orderLineItemsDto);
//            orderLineItemsList.add(orderLineItems);
//        }
        order.setOrderLineItemsList(orderLineItemsList);
        //Map and get skuCode
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        try {
            //Call Inventory service, and place order if product is in stock
            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            if (inventoryResponsesArray.length > 0) {
                boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                        .allMatch(InventoryResponse::isInStock);
                if (allProductsInStock) {
                    orderRepository.save(order);
                } else {
                    Arrays.stream(inventoryResponsesArray)
                            .filter(response -> !response.isInStock())
                            .findFirst()
                            .ifPresent(response -> {
                                throw new ProductNotInStockException("Product with skuCode " + response.getSkuCode() + " is not in stock");
                            });
                }
            }
        } catch (ProductNotInStockException e) {
            throw e;
        } catch ( Exception e) {
            throw new IllegalArgumentException("Error communicating with  Inventory Service");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());

        return orderLineItems;
    }
}
