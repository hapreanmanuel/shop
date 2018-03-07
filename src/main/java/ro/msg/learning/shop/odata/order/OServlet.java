package ro.msg.learning.shop.odata.order;

import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.servlet.ODataServlet;

import javax.servlet.http.HttpServletRequest;

public class OServlet extends ODataServlet {

    private final ODataServiceFactory serviceFactory;

    OServlet(ODataServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    protected ODataServiceFactory getServiceFactory(HttpServletRequest request){
        return serviceFactory;
    }
}
