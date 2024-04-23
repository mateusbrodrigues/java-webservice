package br.inatel.dm111promo.persistence.promo;

import br.inatel.dm111promo.api.promo.PromoRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface PromoRepository {
    PromoRequest save(PromoRequest promoRequest);
    List<PromoRequest> findAll() throws ExecutionException, InterruptedException;
    Optional<PromoRequest> findById(String promoId) throws ExecutionException, InterruptedException;
    void update(PromoRequest promoRequest);
    void delete(String promoId) throws ExecutionException, InterruptedException;
}
