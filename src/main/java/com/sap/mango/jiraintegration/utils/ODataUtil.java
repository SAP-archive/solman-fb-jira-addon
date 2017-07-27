package com.sap.mango.jiraintegration.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

import java.util.List;
import java.util.Locale;

/**
 * Utility class, that supports the OData issue process
 */
public class ODataUtil {

    public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfoResource) throws ODataApplicationException {
        List<UriResource> resourcePaths = uriInfoResource.getUriResourceParts();

        //to get the entity set we have to interpet all uri segments
        if(!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Invalid resource type for first segment", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
                    Locale.ENGLISH);
        }
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        return uriResourceEntitySet.getEntitySet();
    }

    /**
     * Returns the primary key from the uri parameter
     * @param edmEntityType
     * @param keyParams
     * @return
     * @throws ODataApplicationException
     */
    public static String getKeyValue(EdmEntityType edmEntityType, List<UriParameter> keyParams)
            throws ODataApplicationException {
        for (final UriParameter key : keyParams) {
            String keyText = key.getText();
            return keyText;
        }
        return null;
    }

    /**
     * Returns true is property value is null or ''
     * @param property
     * @return
     */
    public static Boolean isEmpty(Property property) {
        return StringUtils.isEmpty(property.getValue().toString());
    }
 }
