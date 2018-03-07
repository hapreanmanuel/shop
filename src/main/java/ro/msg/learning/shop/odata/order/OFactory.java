package ro.msg.learning.shop.odata.order;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.processor.ODataContext;
import ro.msg.learning.shop.service.ShopService;

public class OFactory extends ODataServiceFactory {

    private final ShopService shopService;

    OFactory(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public ODataService createService(ODataContext ctx) {
        return createODataSingleProcessorService(
                new OEdmProvider(),
                new OProcessor(shopService));
    }
}