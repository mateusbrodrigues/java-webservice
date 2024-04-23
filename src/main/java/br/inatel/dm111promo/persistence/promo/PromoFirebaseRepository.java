package br.inatel.dm111promo.persistence.promo;

import br.inatel.dm111promo.api.promo.PromoRequest;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class PromoFirebaseRepository implements PromoRepository {

    private static final String COLLECTION_NAME = "promos";
    private final Firestore firestore;

    public PromoFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public PromoRequest save(PromoRequest promoRequest) {
        firestore.collection(COLLECTION_NAME)
                .document(promoRequest.getId()) // Use UUID.toString()
                .set(promoRequest);
        return promoRequest;
    }

    @Override
    public List<PromoRequest> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.toObject(PromoRequest.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PromoRequest> findById(String promoId) throws ExecutionException, InterruptedException {
        var promo = firestore.collection(COLLECTION_NAME)
                .document(promoId) // Use UUID.toString()
                .get()
                .get()
                .toObject(PromoRequest.class);

        return Optional.ofNullable(promo);
    }

    @Override
    public void update(PromoRequest promoRequest) {
        save(promoRequest);
    }

    @Override
    public void delete(String promoId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(promoId.toString()).delete().get();
    }

}
