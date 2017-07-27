package com.sap.mango.jiraintegration.core.httpclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.data.function.Function1Throws;
import com.sap.mango.jiraintegration.core.data.function.Function3Throws;
import com.sap.mango.jiraintegration.core.data.function.Tuple2;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.AttachmentTypeEnum;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.AuthenticationTypeEnum;
import com.sap.mango.jiraintegration.utils.JsonEncoder;
import com.sap.mango.jiraintegration.utils.UrlUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Solman Client : executes calls to Solution Manager
 */
public class SolmanClient {

    public static final int BUFFER_SIZE = 4096;
    private static final String PATH_FOR_TOKEN = "/sap/opu/odata/SALM/EXT_INTEGRATION_SRV/";
    private static final String PATH_FOR_STATUS_CHANGE = "/sap/opu/odata/SALM/EXT_INTEGRATION_SRV/StatusSet";

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SolmanClient.class);

    public static Either<RestClient.ServiceError, String> solmanSendStatus(SolmanParams sp, String guid, String status, ProxySettings proxySettings) {
        CloseableHttpClient httpClient = null;
        HttpHost httpHost = null;

        try {
            URL url = new URL(sp.solmanUrl);

            LOG.debug("----Starting Solman Status Change Process----");

            if (proxySettings != null) {
                httpHost = new HttpHost(proxySettings.getProxyHost(), proxySettings.getPort());
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(httpHost);
                httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
            } else {
                SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(null);
                httpClient = HttpClients.custom().build();
            }

            LOG.debug("Solman Url: " + url.toString());
            LOG.debug("Proxy Settings: " + (proxySettings != null ? proxySettings.toString() : "-"));
            LOG.debug("Authentication Type: " + sp.authenticationType);


            Either<RestClient.ServiceError, String> resp = null;
            Either<RestClient.ServiceError, String> oauth = null;
            if (AuthenticationTypeEnum.valueOf(sp.authenticationType).equals(AuthenticationTypeEnum.Basic)) {
                resp = getCSRF(url, sp.userName, sp.password, sp.sapClient, null, httpClient);
            } else {
                oauth = getOauthToken(new URL(sp.tokenHcpAccountUrl), sp.userName, sp.password, sp.sapClient, httpClient);
                if (oauth.isRight()) {
                    resp = getCSRF(url, sp.userName, sp.password, sp.sapClient, oauth.toRight().right_value, httpClient);
                } else {
                    return new Either.Left<>(oauth.toLeft().left_value);
                }
            }
            if (resp.isRight()) {
                return sendStatus(url, sp.userName, sp.password, resp.toRight().right_value, guid, status, sp.sapClient, oauth != null ? oauth.toRight().right_value : null, httpClient);
            } else {
                return new Either.Left<>(resp.toLeft().left_value);
            }

        } catch (MalformedURLException e) {
            return new Either.Left<>(new RestClient.ServiceError(0, "Error in server info, please check: " + sp.solmanUrl, Optional.<Throwable>absent()));
        }
        finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                }
                catch(IOException e) {
                    LOG.error(e.getMessage());
                }
            }

        }
    }

    public static Either<RestClient.ServiceError, String> getOauthToken(URL url, final String user, final String pass, String sapClient, CloseableHttpClient httpClient) {
        Map<String, String> params = new HashMap<>();
        params.put(RestClient.PARAMNAME_BASICAUTH_USER, user);
        params.put(RestClient.PARAMNAME_BASICAUTH_PASSWORD, pass);

        LOG.debug("----Getting Oauth Token----");

        return RestClient.executeRequest(url.toString(), "", params, new Function3Throws<String, String, Map<String, String>, Request, IOException>() {
            @Override
            public Request apply(String server, String path, Map<String, String> params) throws IOException {
                final ObjectMapper mapper = JsonEncoder.mapper;

                final Request request = RestClient.createPost.apply(server, path, params)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(String.valueOf(user + ":" + pass).getBytes())).
                                bodyString("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED);
                return request;
            }
        }, new Function1Throws<Tuple2<String, Header[]>, Either<RestClient.ServiceError, String>, IOException>() {
            @Override
            public Either<RestClient.ServiceError, String> apply(Tuple2<String, Header[]> respTuple) throws IOException {
                final ObjectMapper mapper = JsonEncoder.mapper;
                JsonNode root = mapper.readTree(respTuple.a);

                String accessToken = root.get("access_token").textValue();
                LOG.debug("Oauth token: " + accessToken);
                return new Either.Right<>(accessToken);
            }
        }, httpClient);
    }

    private static Either<RestClient.ServiceError, String> getCSRF(URL url, String user, String pass, String sapClient, String bearer, CloseableHttpClient httpClient) {
        Map<String, String> params = new HashMap<>();
        final Map<String, String> specificHeaders = new HashMap<>();

        LOG.debug("----Getting CSRF Token----");

        if (bearer == null) {
            params.put(RestClient.PARAMNAME_BASICAUTH_USER, user);
            params.put(RestClient.PARAMNAME_BASICAUTH_PASSWORD, pass);
            specificHeaders.put("sap-client", sapClient);
        }

        if (bearer != null) {
            specificHeaders.put("Authorization", "Bearer " + bearer);
        }
        return RestClient.executeRequest(url.toString(), PATH_FOR_TOKEN, params, new Function3Throws<String, String, Map<String, String>, Request, IOException>() {
            @Override
            public Request apply(String server, String path, Map<String, String> params) throws IOException {
                final Request request = RestClient.createGet.apply(server, path, params)
                            .addHeader("X-CSRF-Token", "Fetch");
                addSpecificHeaders(request, specificHeaders);
                return request;
            }
        }, new Function1Throws<Tuple2<String, Header[]>, Either<RestClient.ServiceError, String>, IOException>() {
            @Override
            public Either<RestClient.ServiceError, String> apply(Tuple2<String, Header[]> respTuple) throws IOException {
                for (Header header : respTuple.b) {
                    if (header.getName().equals("x-csrf-token")) {
                        LOG.debug("x-csrf token: " + header.getValue());
                        return new Either.Right<>(header.getValue());
                    }
                }
                final RestClient.ServiceError serviceError = new RestClient.ServiceError(0, "x-crsf-token is missing.", Optional.<Throwable>absent());
                return new Either.Left<>(serviceError);
            }
        }, httpClient);
    }

    private static Either<RestClient.ServiceError, String> sendStatus(URL url, String user, String pass, final String token, final String guid, final String status, final String sapClient, final String bearer, CloseableHttpClient httpClient) {
        Map<String, String> params = new HashMap<>();

        LOG.debug("----Calling Solution Manager Status Change URL----");
        final Map<String, String> specificHeaders = new HashMap<>();
        specificHeaders.put("X-CSRF-Token", token);
        if (bearer != null) {
            specificHeaders.put("Authorization", "Bearer " + bearer);
        } else {
            specificHeaders.put("sap-client", sapClient);
            params.put(RestClient.PARAMNAME_BASICAUTH_USER, user);
            params.put(RestClient.PARAMNAME_BASICAUTH_PASSWORD, pass);
        }

        String path = PATH_FOR_STATUS_CHANGE + "(Guid='" + guid + "')";
        return RestClient.executeRequest(url.toString(), path, params, new Function3Throws<String, String, Map<String, String>, Request, IOException>() {
            @Override
            public Request apply(String server, String path, Map<String, String> params) throws IOException {

                final ObjectMapper mapper = JsonEncoder.mapper;
                final ObjectNode payload = mapper.createObjectNode().put("Guid", guid).put("ExtStatus", status);
                final byte[] bytes = mapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8);

                Request request = Request.Put(server + path)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-type", "application/json; charset=utf-8")
                        .body(new ByteArrayEntity(bytes));
                addSpecificHeaders(request, specificHeaders);

                return request;
            }
        }, new Function1Throws<Tuple2<String, Header[]>, Either<RestClient.ServiceError, String>, IOException>() {
            @Override
            public Either<RestClient.ServiceError, String> apply(Tuple2<String, Header[]> respTuple) throws IOException {
                return new Either.Right<>("Success");
            }
        }, httpClient);
    }

    private static void addSpecificHeaders(Request request, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            if (headers.get(key) != null) {
                request.addHeader(key, headers.get(key));
            }
        }
    }

    public static File downloadFile(ProxySettings proxySettings, SolmanParams solmanParams, File saveDir, String fileUrl, String fileName, Integer attachmentType) throws IOException {
        URL url = new URL(fileUrl);
        CloseableHttpClient httpClient = null;
        HttpHost httpHost = null;

        if (proxySettings != null) {
            httpHost = new HttpHost(proxySettings.getProxyHost(), proxySettings.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(httpHost);
            httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
        } else {
            SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(null);
            httpClient = HttpClients.custom().build();
        }
        if (AuthenticationTypeEnum.valueOf(solmanParams.authenticationType).equals(AuthenticationTypeEnum.Basic)) {
            try {
                HttpGet httpGet = new HttpGet(fileUrl);
                File downloadFileDirectory = new File(saveDir + File.separator + fileName);
                Files.createDirectories(downloadFileDirectory.toPath().getParent());

                File downloaded = httpClient.execute(httpGet, new FileDownloadResponseHandler(downloadFileDirectory, attachmentType));
                return downloaded;
            } catch(ClientProtocolException e) {
                throw new IOException(e.getMessage(), e.getCause());
            }
            catch (IOException e) {
                throw new IOException(e.getMessage(), e.getCause());
            }
            finally {
                try {
                    if (httpClient != null) {
                        httpClient.close();
                    }
                }
                catch(IOException e) {
                    LOG.error("error while closing resource", e);
                }
            }
        } else {
            try {
                Either<RestClient.ServiceError, String> oauth = null;
                oauth = getOauthToken(new URL(solmanParams.tokenHcpAccountUrl), solmanParams.userName, solmanParams.password, solmanParams.sapClient, httpClient);
                if (oauth.isRight()) {
                    HttpGet httpGet = new HttpGet(fileUrl);
                    httpGet.addHeader("Authorization", "Bearer " + oauth.toRight().right_value);
                    File downloadFileDirectory = new File(saveDir + File.separator + fileName);
                    Files.createDirectories(downloadFileDirectory.toPath().getParent());

                    File downloaded = httpClient.execute(httpGet, new FileDownloadResponseHandler(downloadFileDirectory, attachmentType));
                    return downloaded;
                } else {
                    throw new IOException(oauth.toLeft().toString());
                }
            } catch(ClientProtocolException e) {
                throw new IOException(e.getMessage(), e.getCause());
            }
            catch (IOException e) {
                throw new IOException(e.getMessage(), e.getCause());
            }
            finally {
                try {
                    if (httpClient != null) {
                        httpClient.close();
                    }
                }catch(IOException e) {
                    LOG.error("error while closing resource", e);
                }
            }
        }
    }

    static class FileDownloadResponseHandler implements ResponseHandler<File> {
        private File target;
        private Integer attachmentType;

        public FileDownloadResponseHandler(File target, Integer attachmentType) {
            this.target = target;
            this.attachmentType = attachmentType;
        }

        @Override
        public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = response.getEntity().getContent();
                if (attachmentType.equals(AttachmentTypeEnum.DOCUMENT.getValue())) {
                    Header header = response.getFirstHeader("Content-Disposition");
                    if (header != null) {
                        for (HeaderElement headerElement : header.getElements()) {
                            if (headerElement.getParameterByName("filename") != null) {
                                String fileName = headerElement.getParameterByName("filename").getValue();
                                target = new File(target.getParentFile(), UrlUtils.formatFileName(fileName));
                                break;
                            }
                        }
                    }
                }
                outputStream = new FileOutputStream(target);
                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                return target;
            }finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    LOG.error("error while closing resource", e);
                }
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    LOG.error("error while closing resource", e);
                }
            }
        }
    }
}
