package ir.moke.aparat;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLTrustManager implements X509TrustManager {

    public static SSLTrustManager instance = new SSLTrustManager();

    private SSLTrustManager() {

    }


    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    private TrustManager[] getTrustManagers() {
        return new TrustManager[]{
                new SSLTrustManager()
        };
    }

    public SSLContext getSslContext() {
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("ssl");
            context.init(null, getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
}
