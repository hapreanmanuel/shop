package ro.msg.learning.shop.odata;

import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.core.processor.ODataSingleProcessorService;


public class ShopODataService extends ODataSingleProcessorService{

    public ShopODataService(EdmProvider provider, ODataSingleProcessor processor) {
        super(provider, processor);
    }

    class ShopODataSingleProcessor extends ODataSingleProcessor{

    }



}

