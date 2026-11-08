/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mdd.common.util;

import com.mdd.common.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static String sendGet(String url) {
        return HttpUtils.sendGet(url, "");
    }

    public static String sendGet(String url, String param) {
        return HttpUtils.sendGet(url, param, "UTF-8");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String line;
            String urlNameString = StringUtils.isNotBlank((CharSequence)param) ? url + "?" + param : url;
            log.info("sendGet - {}", SensitiveUtils.maskUrl(urlNameString));
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("rev - {}", SensitiveUtils.maskJson(result.toString()));
        }
        catch (ConnectException e) {
            log.error("调用HttpUtils.sendGet ConnectException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (IOException e) {
            log.error("调用HttpUtils.sendGet IOException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (Exception e) {
            log.error("调用HttpUtils.sendGet Exception, url={}", SensitiveUtils.maskUrl(url), e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception ex) {
                log.error("调用HttpUtils.in.close Exception, url={}", SensitiveUtils.maskUrl(url), ex);
            }
        }
        return result.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            String line;
            log.info("sendPost - {}", SensitiveUtils.maskUrl(url));
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("rev - {}", SensitiveUtils.maskJson(result.toString()));
        }
        catch (ConnectException e) {
            log.error("调用HttpUtils.sendPost ConnectException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (IOException e) {
            log.error("调用HttpUtils.sendPost IOException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (Exception e) {
            log.error("调用HttpUtils.sendPost Exception, url={}", SensitiveUtils.maskUrl(url), e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                log.error("调用HttpUtils.in.close Exception, url={}", SensitiveUtils.maskUrl(url), ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            String ret;
            log.info("sendSSLPost - {}", SensitiveUtils.maskUrl(urlNameString));
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection)console.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((ret = br.readLine()) != null) {
                if ("".equals(ret.trim())) continue;
                result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            log.info("rev - {}", SensitiveUtils.maskJson(result.toString()));
            conn.disconnect();
            br.close();
        }
        catch (ConnectException e) {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (IOException e) {
            log.error("调用HttpUtils.sendSSLPost IOException, url={}", SensitiveUtils.maskUrl(url), e);
        }
        catch (Exception e) {
            log.error("调用HttpUtils.sendSSLPost Exception, url={}", SensitiveUtils.maskUrl(url), e);
        }
        return result.toString();
    }

    private static class TrustAnyTrustManager
    implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAnyHostnameVerifier
    implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}

