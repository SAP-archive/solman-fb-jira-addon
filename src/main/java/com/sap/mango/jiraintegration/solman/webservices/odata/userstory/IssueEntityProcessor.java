package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

import com.sap.mango.jiraintegration.utils.ODataUtil;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * IssueEntityProcessor : performs the operations (Create, Update, Read) over the Entity Issue
 */
public class IssueEntityProcessor implements EntityProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(IssueEntityProcessor.class);

    private OData odata;
    private ServiceMetadata serviceMetadata;
    private IssueEntityStorageService issueEntityStorageService;

    public IssueEntityProcessor(IssueEntityStorageService issueEntityStorageService) {
        this.issueEntityStorageService = issueEntityStorageService;
    }

    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    /**
     * Creates an issue, using the json parameters.
     * @param oDataRequest
     * @param oDataResponse
     * @param uriInfo
     * @param requestFormat
     * @param responseFormat
     * @throws ODataApplicationException
     * @throws ODataLibraryException
     */
    @Override
    public void createEntity(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

        EdmEntitySet edmEntitySet = ODataUtil.getEdmEntitySet(uriInfo);
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        //2 Create the data in backend
        //2.1 retrieve the payload from the POST request for the entity to create and deserialize it
        InputStream requestInputStream = oDataRequest.getBody();
        ODataDeserializer deserializer = this.odata.createDeserializer(requestFormat);
        DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
        Entity requestResult = result.getEntity();
        //2.2 do the creation

        LOG.debug("Request URI: " + oDataRequest.getRawRequestUri());
        LOG.debug("Protocol: " + oDataRequest.getProtocol());

        Entity resultEntity = issueEntityStorageService.createIssue(requestResult, edmEntityType, edmEntitySet);

        //3.serialize the response (we have to return the created entity)
        ContextURL contextURL = ContextURL.with().entitySet(edmEntitySet).build();
        EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextURL).build();

        ODataSerializer serializer = this.odata.createSerializer(responseFormat);
        SerializerResult serializerResult = serializer.entity(serviceMetadata, edmEntityType, resultEntity, options);

        oDataResponse.setContent(serializerResult.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.CREATED.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());

    }

    /**
     * Reads an issue, using the provided issue key
     * @param oDataRequest
     * @param oDataResponse
     * @param uriInfo
     * @param responseFormat
     * @throws ODataApplicationException
     * @throws ODataLibraryException
     */
    @Override
    public void readEntity(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        //note: only in our example we can assume that the first segment is the EntitySet
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

        //2.retrieve the data from backend
        List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();

        Entity entity = null;

        EdmEntityType entityType = edmEntitySet.getEntityType();

        ContextURL contextURL = ContextURL.with().entitySet(edmEntitySet).build();
        //expand and select currently not supported
        EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextURL).build();

        ODataSerializer serializer = odata.createSerializer(responseFormat);
        SerializerResult serializerResult = serializer.entity(serviceMetadata, entityType, entity, options);
        InputStream entityStream = serializerResult.getContent();

        oDataResponse.setContent(entityStream);
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    /**
     * Updates an issue, using the provided issue key and issue information.
     * @param oDataRequest
     * @param oDataResponse
     * @param uriInfo
     * @param requestFormat
     * @param responseFormat
     * @throws ODataApplicationException
     * @throws ODataLibraryException
     */
    @Override
    public void updateEntity(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
        //1. Retrieve the entity set which belongs to the requested entity
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        //note: only in our example we can assume that the first segment is the EntitySet

        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        //2. Update the data in the backend
        //2.1 retrieve the payload from the PUT request for the entity to be updated

        InputStream inputStream = oDataRequest.getBody();
        ODataDeserializer deserializer = this.odata.createDeserializer(requestFormat);
        DeserializerResult result = deserializer.entity(inputStream, edmEntityType);
        Entity requestEntity = result.getEntity();

        //2.2 do the modification in backend
        List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();

        LOG.debug("Request URI: " + oDataRequest.getRawRequestUri());
        LOG.debug("Protocol: " + oDataRequest.getProtocol());

        Entity resultEntity = issueEntityStorageService.updateIssue(requestEntity, edmEntityType, edmEntitySet, keyPredicates);

        HttpMethod httpMethod = oDataRequest.getMethod();

        oDataResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    }

    @Override
    public void deleteEntity(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo) throws ODataApplicationException, ODataLibraryException {

    }
}
