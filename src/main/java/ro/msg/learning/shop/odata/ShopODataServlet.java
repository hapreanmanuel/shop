package ro.msg.learning.shop.odata;

import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.servlet.ODataServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShopODataServlet extends ODataServlet{

    private final ODataServiceFactory serviceFactory;

    public ShopODataServlet(ODataServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException{
        req.setAttribute(ODataServiceFactory.FACTORY_INSTANCE_LABEL, serviceFactory);

        super.service(req, res);
    }

}
