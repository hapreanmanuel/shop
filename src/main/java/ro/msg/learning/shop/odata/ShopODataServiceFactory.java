package ro.msg.learning.shop.odata;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

import javax.persistence.EntityManagerFactory;

public class ShopODataServiceFactory extends ODataJPAServiceFactory {

    private final EntityManagerFactory entityManagerFactory;
    private final String namespace;

    public ShopODataServiceFactory(final EntityManagerFactory entityManagerFactory, final String namespace) {
        this.entityManagerFactory = entityManagerFactory;
        this.namespace = namespace;
    }

    @Override
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
        ODataJPAContext context = this.getODataJPAContext();
        context.setEntityManagerFactory(entityManagerFactory);
        context.setPersistenceUnitName(namespace);
        return context;
    }
}

