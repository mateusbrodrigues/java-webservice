package br.inatel.dm111promo.api.promo.service;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.core.AppErrorCode;
import br.inatel.dm111promo.api.product.service.ProductService;
import br.inatel.dm111promo.api.promo.PromoRequest;
import br.inatel.dm111promo.persistence.product.Product;
import org.springframework.stereotype.Service;
import br.inatel.dm111promo.persistence.promo.PromoFirebaseRepository;
import br.inatel.dm111promo.api.core.ProductIdNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PromoService {

    private final PromoFirebaseRepository promoFirebaseRepository;
    private final ProductService productService;

    public PromoService(PromoFirebaseRepository promoFirebaseRepository, ProductService productService) {
        this.promoFirebaseRepository = promoFirebaseRepository;
        this.productService = productService;
    }
    public PromoRequest createPromo(PromoRequest promoRequest) throws Exception {
        List<Product> allProducts = productService.searchProducts();
        List<String> productIds = allProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        for (PromoRequest.Product product : promoRequest.getProducts()) {
            if (!productIds.contains(product.getProductId())) {
                throw new ProductIdNotFoundException("Product ID does not exist.");
            }
        }
        var newId = UUID.randomUUID().toString();
        promoRequest.setId(newId);
        return promoFirebaseRepository.save(promoRequest);
    }
    public PromoRequest updatePromo(String id, PromoRequest promoRequest) throws ApiException, ExecutionException, InterruptedException {
        var promoId = retrievePromo(id);
        List<Product> allProducts = productService.searchProducts();
        List<String> productIds = allProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        for (PromoRequest.Product product : promoRequest.getProducts()) {
            if (!productIds.contains(product.getProductId())) {
                throw new ProductIdNotFoundException("Product ID does not exist.");
            }
        }

        promoRequest.setId(promoId.getId());
        promoFirebaseRepository.update(promoRequest);
        return promoRequest;
    }
    public void removePromo(String id) throws ApiException {
        try {
            promoFirebaseRepository.delete(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    public List<PromoRequest> getAllPromos() throws ApiException {
        try {
            return promoFirebaseRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    public List<PromoRequest> getAllValidPromos() throws ApiException {
        try {
            Instant now = Instant.now();
            Date currentDate = Date.from(now);

            return promoFirebaseRepository.findAll().stream()
                    .filter(promo -> currentDate.after(promo.getStarting()) && currentDate.before(promo.getExpiration()))
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }
    private PromoRequest retrievePromo(String id) throws ApiException {
        try {
            return promoFirebaseRepository.findById(id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.PRODUCT_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

}
