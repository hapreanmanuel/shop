package ro.msg.learning.shop.odata.order;

import lombok.extern.slf4j.Slf4j;

import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.core.ep.feed.ODataDeltaFeedImpl;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.service.ShopService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OProcessor extends ODataSingleProcessor {

    private final ShopService shopService;

    OProcessor(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException {
        //No support for creating and linking a new entry
        if (!uriInfo.getNavigationSegments().isEmpty()) {
            throw new ODataNotImplementedException();
        }
        //No support for media resources
        if (uriInfo.getStartEntitySet().getEntityType().hasStream()) {
            throw new ODataNotImplementedException();
        }

        EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();

        ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getStartEntitySet(), content, properties);

        Map<String, Object> data = entry.getProperties();

        //This method checks data validity and throws exceptions
        OrderSpecifications specifications = shopService.createOrderSpecifications(getRequest(data), getUsername(data));

        Order ord = shopService.createNewOrder(specifications);

        data.put("Id",ord.getOrderId());

        return EntityProvider.writeEntry(contentType,
                uriInfo.getStartEntitySet(),
                data,
                EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
    }

    private String getUsername(Map<String, Object> data){
        return (String)data.get("Username");
    }

    private Address getAddress(Map<String, Object> data){
        HashMap addressMap = (HashMap)data.get("Address");

        return Address.builder()
                .city(addressMap.get("City").toString())
                .country(addressMap.get("Country").toString())
                .region(addressMap.get("Region").toString())
                .fullAddress(addressMap.get("FullAddress").toString()).build();
    }

    private List<ShoppingCartEntry> getWishList(Map<String, Object> data){
        ODataDeltaFeedImpl deltaFeed = (ODataDeltaFeedImpl)data.get("ShoppingCart");

        List<ODataEntry> deltaFeedEntries = deltaFeed.getEntries();

        return deltaFeedEntries.stream().map(element-> {
            Map<String,Object>  dEntryMap = element.getProperties();
            return new ShoppingCartEntry((int) dEntryMap.get("ProductId"), (int) dEntryMap.get("Quantity"));
        }).collect(Collectors.toList());
    }

    private OrderCreationDto getRequest(Map<String, Object> data){

        OrderCreationDto request = new OrderCreationDto();
        request.setAddress(getAddress(data));
        request.setShoppingCart(getWishList(data));

        return request;
    }

}

