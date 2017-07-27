package com.sap.mango.jiraintegration.core.httpclient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.data.function.Function1Throws;
import com.sap.mango.jiraintegration.core.data.function.Function3Throws;
import com.sap.mango.jiraintegration.core.data.function.Tuple2;
import com.sap.mango.jiraintegration.utils.JsonEncoder;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

public class RestClient
{

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RestClient.class);

    public static final String PARAMNAME_BASICAUTH_USER = "RestClient.BasicAuth.User";
    public static final String PARAMNAME_BASICAUTH_PASSWORD = "RestClient.BasicAuth.Password";

    public static final Set<String> INTERNAL_PARAM_NAMES = set(PARAMNAME_BASICAUTH_USER, PARAMNAME_BASICAUTH_PASSWORD);

    public static final TimeoutConfig defaultTimeoutConfig = new TimeoutConfig(30 * 1000, 30 * 1000);

    private static final ResponseHandler<Either<ServiceError, Tuple2<String, Header[]>>> handleHttpResponse = new ResponseHandler<Either<ServiceError, Tuple2<String, Header[]>>>()
    {
        @Override
        public Either<ServiceError, Tuple2<String, Header[]>> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException
        {
            final StatusLine statusLine = response.getStatusLine();

            final HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300)
            {
                String explanatation = statusLine.getReasonPhrase();
                if (entity != null) {
                    try {
                        explanatation += "\n *** entity: " + EntityUtils.toString(entity);
                    } catch (ZipException e) {
                        return new Either.Left<>(
                                new ServiceError(statusLine.getStatusCode(), statusLine.getStatusCode() + ", " + explanatation, Optional.<Throwable>absent()));
                    }
                }
                return new Either.Left<>(
                        new ServiceError(statusLine.getStatusCode(), explanatation, Optional.<Throwable>absent()));
            }
            else
            {
                if (entity != null) {
                    return new Either.Right<>(new Tuple2<>(EntityUtils.toString(entity, "UTF-8"), response.getAllHeaders()));
                }
               else
                {
                    return new Either.Right<>(new Tuple2<String, Header[]>(null, response.getAllHeaders()));
                }
            }
        }
    };

    public static final Function3Throws<String, String, Map<String, String>, Request, IOException> createGet = new Function3Throws<String, String, Map<String, String>, Request, IOException>()
    {
        @Override
        public Request apply(String rootUri, String path, Map<String, String> params) throws IOException
        {
            String uri = rootUri + path + getQuery(params);
            return Request.Get(uri);
        }
    };

    public static final Function3Throws<String, String, Map<String, String>, Request, IOException> createPost = new Function3Throws<String, String, Map<String, String>, Request, IOException>()
    {
        @Override
        public Request apply(String rootUri, String path, Map<String, String> params) throws IOException
        {
            String uri = rootUri + path;
            //HttpHost httpHost = new HttpHost("localhost", 8080, null);
            return Request.Post(uri).bodyForm(form(params));
        }
    };

    public static final Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, String>, IOException> createRawResult = new Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, String>, IOException>()
    {
        @Override
        public Either<ServiceError, String> apply(Tuple2<String, Header[]> respTuple) throws IOException
        {
            return new Either.Right<>(respTuple.a);
        }
    };

    public static <A> Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException> createTypedResult(final Class<A> type,
                                                                                                                        final ObjectMapper mapper)
    {

        return new Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException>()
        {
            @Override
            public Either<ServiceError, A> apply(Tuple2<String, Header[]> respTuple) throws IOException
            {
                return new Either.Right<>(mapper.readValue(respTuple.a, type));
            }
        };

    }

    public static <A> Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException> createTypedResult(final JavaType type,
                                                                                                                        final ObjectMapper mapper)
    {
        return new Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException>()
        {
            @Override
            public Either<ServiceError, A> apply(Tuple2<String, Header[]> respTuple) throws IOException
            {
                A a = mapper.readValue(respTuple.a, type);
                return new Either.Right<>(a);
            }
        };
    }

    /**
     * Executes a get request with the passed parameters, and returns either the error or the raw string of the body
     *
     * @param serverPath
     * @param path
     * @param params
     * @return
     */
    public static Either<ServiceError, String> executeGetRaw(String serverPath, String path, Map<String, String> params, CloseableHttpClient httpClient)
    {
        return executeRequest(serverPath, path, params, createGet, createRawResult, null, httpClient);
    }

    /**
     * Executes a get request with the passed parameters, and returns either the error or the raw string of the body
     *
     * @param serverPath
     * @param path
     * @param params
     * @return
     */
    public static Either<ServiceError, String> executePostRaw(String serverPath, String path, Map<String, String> params, CloseableHttpClient httpClient)
    {
        return executeRequest(serverPath, path, params, createPost, createRawResult, null, httpClient);
    }

    public static <A> Either<ServiceError, A> executeGetTyped(String serverPath, String path, Map<String, String> params,
                                                              Class<A> type, CloseableHttpClient httpClient)
    {
        return executeRequest(serverPath, path, params, createGet, createTypedResult(type, JsonEncoder.mapper), httpClient);
    }

    public static <A> Either<ServiceError, A> executePostTyped(String serverPath, String path, Map<String, String> params,
                                                               Class<A> type, CloseableHttpClient httpClient)
    {
        return executeRequest(serverPath, path, params, createPost, createTypedResult(type, JsonEncoder.mapper), null, httpClient);
    }

    public static <A> Either<ServiceError, A> executeRequest(String serverPath, String path, Map<String, String> params,
                                                             Function3Throws<String, String, Map<String, String>, Request, IOException> createRequest,
                                                             Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException> createResult,
                                                             CloseableHttpClient httpClient)
    {
        return executeRequest(serverPath, path, params, createRequest, createResult, defaultTimeoutConfig, httpClient);
    }

    /**
     * Executes a request using Apache HTTP Client
     *
     * @param serverPath    - protocol, host and port without trailing '/'
     * @param path          the path of the resource on the server, with leading '/'
     * @param params        the parameters of the call
     * @param createRequest Function which will be applied to serverPath,path and params to yield the desired Request object
     * @param createResult  Function which will be applied to the raw response body, in case the status code is 200, to yield the desired result type
     * @param timeoutConfig Configuration for the request and socket timeout
     * @param <A>           The type of the successful result
     * @return the result
     */
    public static <A> Either<ServiceError, A> executeRequest(String serverPath, String path, Map<String, String> params,
                                                             Function3Throws<String, String, Map<String, String>, Request, IOException> createRequest,
                                                             Function1Throws<Tuple2<String, Header[]>, Either<ServiceError, A>, IOException> createResult,
                                                             TimeoutConfig timeoutConfig, CloseableHttpClient httpClient)
    {

        long start = System.currentTimeMillis();

        try
        {

            Map<String, String> actualParams = getActualParams(params);

            Request request = createRequest.apply(serverPath, path, actualParams);
            request = request.connectTimeout(timeoutConfig.connectTimeout).socketTimeout(timeoutConfig.socketTimeout);

            boolean withBasicAuth = params != null && params.get(PARAMNAME_BASICAUTH_USER) != null
                    && params.get(PARAMNAME_BASICAUTH_PASSWORD) != null;
            Response apacheResp = null;

            logRequestInputParameters(request, httpClient);
            if (withBasicAuth)
            {
                Executor executor = Executor.newInstance(httpClient)
                        .auth(params.get(PARAMNAME_BASICAUTH_USER), params.get(PARAMNAME_BASICAUTH_PASSWORD));

                apacheResp = executor.execute(request);
            }
            else
            {
                apacheResp = Executor.newInstance(httpClient).execute(request);
            }

            Either<ServiceError, Tuple2<String, Header[]>> resp = apacheResp.handleResponse(handleHttpResponse);
            logHttpResponse(resp);
            if (resp.isLeft())
            {
                return new Either.Left<>(resp.toLeft().left_value);
            }
            else
            {
                Tuple2<String, Header[]> val = resp.toRight().right_value;
                try
                {
                    return createResult.apply(val);
                }
                catch (IOException e)
                {
                    return new Either.Left<>(new ServiceError(-2, MessageFormat
                            .format("IOException in createResult function. Raw response: \n {0} \n Exception: \n {1}", val,
                                    Throwables.getStackTraceAsString(e)), Optional.<Throwable>of(e)));
                }
            }
        }
        catch (IOException e)
        {
            boolean timeout = e instanceof InterruptedIOException;

            if (timeout)
            {
                long duration = System.currentTimeMillis() - start;
                return new Either.Left<>(new ServiceError(-1,
                        "Timeout occurred, duration in millis " + duration + "\n" + Throwables.getStackTraceAsString(e),
                        Optional.<Throwable>of(e)));

            }

            return new Either.Left<>(new ServiceError(-1, Throwables.getStackTraceAsString(e), Optional.<Throwable>of(e)));

        }

    }

    private static Map<String, String> getActualParams(Map<String, String> params)
    {
        if (params == null)
        {
            return null;
        }
        Map<String, String> result = new HashMap<>(params);
        for (String key : INTERNAL_PARAM_NAMES)
        {
            result.remove(key);
        }
        return result;
    }

    /**
     * Url params to be used for a get request
     */
    public static String getQuery(Map<String, String> params)
    {
        if (params == null || params.isEmpty())
        {
            return "";
        }

        StringBuilder b = new StringBuilder();
        b.append("?");

        for (Map.Entry<String, String> param : params.entrySet())
        {
            try
            {
                b.append(param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8") + "&");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }

        String builtString = b.toString();
        return builtString.substring(0, builtString.length() - 1);
    }

    /**
     * Params to be used for a post request
     */
    public static Iterable<? extends NameValuePair> form(Map<String, String> params)
    {
        Form result = Form.form();
        for (Map.Entry<String, String> param : params.entrySet())
        {
            result.add(param.getKey(), param.getValue());
        }
        return result.build();
    }

    public static final class ServiceError
    {
        public final int code;
        public final String detail;
        @JsonIgnore
        public final Optional<Throwable> cause;

        public ServiceError(int code, String detail, Optional<Throwable> cause)
        {
            this.code = code;
            this.detail = detail;
            this.cause = cause;
        }

        @Override
        public String toString()
        {
            return "ServiceError{" +
                    "code=" + code +
                    ", detail='" + detail + '\'' +
                    '}';
        }
    }

    public static final class TimeoutConfig
    {
        public final int connectTimeout;
        public final int socketTimeout;

        public TimeoutConfig(int connectTimeout, int socketTimeout)
        {
            this.connectTimeout = connectTimeout;
            this.socketTimeout = socketTimeout;
        }
    }

    private static <A> Set<A> set(A... as)
    {
        Set<A> result = new HashSet<>();
        for (A a : as)
        {
            result.add(a);
        }
        return result;
    }

    private static void logRequestInputParameters(Request request, CloseableHttpClient closeableHttpClient) {
        LOG.debug("URL: " + request.toString());
    }

    private static void logHttpResponse(Either<ServiceError, Tuple2<String, Header[]>> resp) {
        LOG.debug("Http Response: " + (resp.isLeft() ?  resp.toLeft().left_value.toString() : resp.toRight().right_value.toString()));
    }

}