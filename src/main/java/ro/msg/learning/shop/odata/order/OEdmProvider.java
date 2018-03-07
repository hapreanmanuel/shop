package ro.msg.learning.shop.odata.order;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.*;
import org.apache.olingo.odata2.api.exception.ODataException;

import java.util.ArrayList;
import java.util.List;

public class OEdmProvider extends EdmProvider {

    private static final String NAMESPACE = "ro.msg.learning.shop.OData.request";
    private static final String ENTITY_CONTAINER = "ODataOrderRequestContainer";

    private static final String ENTITY_SET_NAME_ORDERREQUESTS = "OrderRequests";
    private static final String ENTITY_NAME_ORDERREQUEST = "OrderRequest";

    private static final String ENTITY_SET_NAME_SHOPPINGCARTENTRIES = "ShoppingCartEntries";
    private static final String ENTITY_NAME_SHOPPINGCARTENTRY = "ShoppingCartEntry";

    private static final FullQualifiedName ENTITY_TYPE_ORDERREQUEST = new FullQualifiedName(NAMESPACE, ENTITY_NAME_ORDERREQUEST);

    private static final FullQualifiedName ENTITY_TYPE_SHOPPINGCARTENTRY = new FullQualifiedName(NAMESPACE, ENTITY_NAME_SHOPPINGCARTENTRY);

    private static final FullQualifiedName COMPLEX_TYPE_ADDRESS = new FullQualifiedName(NAMESPACE, "Address");

    private static final String ROLE_REQ_CART = "OrderRequest_ShoppingCartEntries";

    private static final String ROLE_CART_REQ = "ShoppingCartEntry_OrderRequest";

    private static final FullQualifiedName ASSOCIATION_REQUEST_CART = new FullQualifiedName(NAMESPACE, ROLE_REQ_CART+"_"+ROLE_CART_REQ);

    private static final String ASSOCIATION_SET = "OrderRequests_ShoppingCartEntries";

    @Override
    public List<Schema> getSchemas() throws ODataException {
        List<Schema> schemas = new ArrayList<>();

        Schema schema = new Schema();
        schema.setNamespace(NAMESPACE);

        List<EntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ENTITY_TYPE_ORDERREQUEST));
        entityTypes.add(getEntityType(ENTITY_TYPE_SHOPPINGCARTENTRY));
        schema.setEntityTypes(entityTypes);

        List<ComplexType> complexTypes = new ArrayList<>();
        complexTypes.add(getComplexType(COMPLEX_TYPE_ADDRESS));
        schema.setComplexTypes(complexTypes);

        List<Association> associations = new ArrayList<>();
        associations.add(getAssociation(ASSOCIATION_REQUEST_CART));
        schema.setAssociations(associations);

        List<EntityContainer> entityContainers = new ArrayList<>();
        EntityContainer entityContainer = new EntityContainer();
        entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);

        List<EntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_ORDERREQUESTS));
        entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_SHOPPINGCARTENTRIES));
        entityContainer.setEntitySets(entitySets);

        List<AssociationSet> associationSets = new ArrayList<>();
        associationSets.add(getAssociationSet(ENTITY_CONTAINER,ASSOCIATION_REQUEST_CART, ENTITY_SET_NAME_SHOPPINGCARTENTRIES, ROLE_CART_REQ));
        entityContainer.setAssociationSets(associationSets);

        entityContainers.add(entityContainer);
        schema.setEntityContainers(entityContainers);

        schemas.add(schema);

        return schemas;
    }

    @Override
    public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
        if(name==null || ENTITY_CONTAINER.equals(name)){
            return new EntityContainerInfo().setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);
        }
        throw new ODataException();
    }

    @Override
    public EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException {
        if(edmFQName.equals(ENTITY_TYPE_ORDERREQUEST)){
            List<Property> properties = new ArrayList<>();

            properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32));
            properties.add(new SimpleProperty().setName("Username").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false).setMaxLength(255)));
            properties.add(new ComplexProperty().setName("Address").setType(COMPLEX_TYPE_ADDRESS));

            List<NavigationProperty> navigationProperties = new ArrayList<>();
            navigationProperties.add(new NavigationProperty().setName("ShoppingCart")
                    .setRelationship(ASSOCIATION_REQUEST_CART).setFromRole(ROLE_REQ_CART).setToRole(ROLE_CART_REQ));

            List<PropertyRef> keyProperties = new ArrayList<>();
            keyProperties.add(new PropertyRef().setName("Id"));
            Key key = new Key().setKeys(keyProperties);

            return new EntityType().setName(ENTITY_TYPE_ORDERREQUEST.getName())
                    .setProperties(properties)
                    .setKey(key)
                    .setNavigationProperties(navigationProperties);

        }else if(edmFQName.equals(ENTITY_TYPE_SHOPPINGCARTENTRY)){
            List<Property> properties = new ArrayList<>();

            properties.add(new SimpleProperty().setName("ProductId").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
            properties.add(new SimpleProperty().setName("Quantity").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));

            return new EntityType().setName(ENTITY_TYPE_SHOPPINGCARTENTRY.getName())
                    .setProperties(properties);
        }
        throw new ODataException();
    }

    @Override
    public ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException {
        List<Property> properties = new ArrayList<>();
        if(edmFQName.equals(COMPLEX_TYPE_ADDRESS)){
            properties.add(new SimpleProperty().setName("City").setType(EdmSimpleTypeKind.String));
            properties.add(new SimpleProperty().setName("Country").setType(EdmSimpleTypeKind.String));
            properties.add(new SimpleProperty().setName("FullAddress").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true).setMaxLength(255)));
            properties.add(new SimpleProperty().setName("Region").setType(EdmSimpleTypeKind.String));
            return new ComplexType().setName(COMPLEX_TYPE_ADDRESS.getName()).setProperties(properties);
        }
        throw new ODataException();
    }


    @Override
    public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {
        if(entityContainer.equals(ENTITY_CONTAINER)){
            if(ENTITY_SET_NAME_ORDERREQUESTS.equals(name)){
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_ORDERREQUEST);
            } else if(ENTITY_SET_NAME_SHOPPINGCARTENTRIES.equals(name)){
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_SHOPPINGCARTENTRY);
            }
        }
        throw new ODataException();
    }

    @Override
    public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
        if (edmFQName.equals(ASSOCIATION_REQUEST_CART)) {
                return new Association().setName(ASSOCIATION_REQUEST_CART.getName())
                        .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_ORDERREQUEST).setRole(ROLE_REQ_CART).setMultiplicity(EdmMultiplicity.ONE))
                        .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_SHOPPINGCARTENTRY).setRole(ROLE_CART_REQ).setMultiplicity(EdmMultiplicity.MANY));

        }
        throw new ODataException();
    }

    @Override
    public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
        if (association.equals(ASSOCIATION_REQUEST_CART)){
                return new AssociationSet().setName(ASSOCIATION_SET)
                        .setAssociation(ASSOCIATION_REQUEST_CART)
                        .setEnd1(new AssociationSetEnd().setRole(ROLE_REQ_CART).setEntitySet(ENTITY_SET_NAME_ORDERREQUESTS))
                        .setEnd2(new AssociationSetEnd().setRole(ROLE_CART_REQ).setEntitySet(ENTITY_SET_NAME_SHOPPINGCARTENTRIES));

        }
        throw new ODataException();
    }
}



