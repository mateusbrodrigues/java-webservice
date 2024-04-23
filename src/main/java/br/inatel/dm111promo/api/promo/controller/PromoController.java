package br.inatel.dm111promo.api.promo.controller;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.core.AppErrorCode;
import br.inatel.dm111promo.api.core.ProductIdNotFoundException;
import br.inatel.dm111promo.api.promo.PromoRequest;
import br.inatel.dm111promo.api.promo.PromoResponse;
import br.inatel.dm111promo.api.promo.service.PromoService;
import br.inatel.dm111promo.api.supermaketlist.service.SuperMarketListService;
import br.inatel.dm111promo.persistence.product.Product;
import br.inatel.dm111promo.persistence.product.ProductRepository;
import br.inatel.dm111promo.persistence.supermarketlist.SuperMarketList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import br.inatel.dm111promo.persistence.supermarketlist.SuperMarketList;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dm111")
public class PromoController {

    private final PromoService promoService;
    private final SuperMarketListService superMarketListService;
    private final ProductRepository productRepository;

    public PromoController(PromoService promoService, SuperMarketListService superMarketListService, ProductRepository productRepository) {
        this.promoService = promoService;
        this.superMarketListService = superMarketListService;
        this.productRepository = productRepository;
    }

    //Get all promos without restriction (for tests)
    /*@GetMapping("/promos")
    public ResponseEntity<List<PromoRequest>> getAllPromos() throws ApiException{
        var promos = promoService.getAllPromos();
        return ResponseEntity.ok(promos);
    }
    */
    //Get only promos within current date
    @GetMapping("/promos")
    public ResponseEntity<List<PromoRequest>> getAllValidPromos() throws ApiException{
        var promos = promoService.getAllValidPromos();
        return ResponseEntity.ok(promos);
    }
    //Get a specific promo within current date
    @GetMapping("/promos/{id}")
    public ResponseEntity<PromoRequest> getPromoById(@PathVariable String id) throws ApiException {
        try {
            var promo = promoService.getAllValidPromos().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ApiException(AppErrorCode.PROMO_NOT_FOUND));

            return ResponseEntity.ok(promo);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all valid promos by userID that has a product in both promo and in the last 3 supermarketLists (for tests)
    // this endpoint does not return productsForYou.

   /*@GetMapping("/promos/users/{userId}")
    public ResponseEntity<List<PromoRequest>> fetchPromoByUserId(@PathVariable String userId) {
        try {
            List<SuperMarketList> userLists = superMarketListService.searchAllLists(userId);
            List<String> products = userLists.stream()
                    .sorted(Comparator.comparing(SuperMarketList::getId).reversed())
                    .limit(3) // last 3 supermarket lists of the user
                    .flatMap(list -> list.getProducts().stream())
                    .collect(Collectors.toList());

            List<PromoRequest> promos = promoService.getAllValidPromos().stream()
                    .filter(promo -> promo.getProducts().stream()
                            .anyMatch(product -> products.contains(product.getProductId())))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(promos);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    */

    @GetMapping("/promos/users/{userId}")
    public ResponseEntity<List<PromoResponse>> fetchPromoByUserId(@PathVariable String userId) {
        try {
            List<SuperMarketList> userLists = superMarketListService.searchAllLists(userId);

            List<String> productIds = userLists.stream()
                    .sorted(Comparator.comparing(SuperMarketList::getId).reversed())
                    .limit(3) // last 3 supermarket lists of the user
                    .flatMap(list -> list.getProducts().stream())
                    .collect(Collectors.toList());

            List<PromoRequest> allValidPromos = promoService.getAllValidPromos();

            List<PromoResponse> promoResponses = allValidPromos.stream()
                    .map(promo -> {
                        List<PromoRequest.Product> productsForYou = promo.getProducts().stream()
                                .filter(product -> productIds.contains(product.getProductId()))
                                .collect(Collectors.toList());
                        List<PromoRequest.Product> otherProducts = promo.getProducts().stream()
                                .filter(product -> !productIds.contains(product.getProductId()))
                                .collect(Collectors.toList());
                        return new PromoResponse(promo.getId(), promo.getName(), promo.getStarting(), promo.getExpiration(), productsForYou, otherProducts);
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(promoResponses);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/promos")
    public ResponseEntity<Object> createPromo(@Valid @RequestBody PromoRequest promoRequest) {
        try {
            PromoRequest createdPromo = promoService.createPromo(promoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPromo);
        } catch (ProductIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid discount value: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/promos/{id}")
    public ResponseEntity<String> updatePromo(@PathVariable String id, @RequestBody PromoRequest promoRequest) {
        try {
            PromoRequest updatedPromo = promoService.updatePromo(id, promoRequest);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPromo.toString());
        } catch (ProductIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid discount value: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/promos/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id")String id) throws ApiException {
        promoService.removePromo(id);
        return ResponseEntity.noContent().build();
    }

}
