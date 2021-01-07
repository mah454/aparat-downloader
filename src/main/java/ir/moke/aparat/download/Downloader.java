package ir.moke.aparat.download;

import ir.moke.aparat.Calculator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Downloader implements ProgressCallBack {
    private long downloadedSize;
    private long downloadableSize;

    public static Downloader instance = new Downloader();

    private Downloader() {
    }

    public void download(URL url, File targetFile) throws Exception {
        downloadedSize = targetFile.length();
        downloadableSize = getDownloadableSize(url);

        if (downloadableSize == 0) {
            System.out.println("Can not get downloadable file size");
            System.exit(1);
        } else if (downloadedSize >= downloadableSize) {
            System.out.println("Download Completed");
        } else {
            HttpURLConnection downloadConnection = getHttpURLConnection(url);
            if (downloadConnection != null) {
                if (downloadedSize > 0) {
                    downloadConnection.setRequestProperty("Range", "bytes=" + targetFile.length() + "-");
                }
                downloadOperation(targetFile, downloadConnection);
            }
        }
    }

    private void downloadOperation(File outputFile, HttpURLConnection downloadConnection) throws IOException {
        InputStream inputStream = downloadConnection.getInputStream();
        CallBackByteChannel callBackByteChannel = new CallBackByteChannel(Channels.newChannel(inputStream), downloadableSize, this);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile, true);
        FileChannel channel = fileOutputStream.getChannel();
        channel.transferFrom(callBackByteChannel, outputFile.length(), Long.MAX_VALUE);
    }

    private long getDownloadableSize(URL url) {
        long size = 0;
        try {
            HttpURLConnection httpConnection = getHttpURLConnection(url);
            if (httpConnection != null) {
                httpConnection.setRequestMethod("HEAD");
                size = httpConnection.getContentLengthLong();
                httpConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private HttpURLConnection getHttpURLConnection(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void callBack(ReadableByteChannel readableByteChannel, long sizeRead, long progress) {
        long currentPercentage = Calculator.percentage(downloadableSize, downloadedSize);
        progress += currentPercentage;
        sizeRead += downloadedSize;
        if (downloadedSize >= downloadableSize) {
            progress = 100;
        }
        System.out.print("\r" + sizeRead + "/" + downloadableSize + "          " + progress + "/100 %");
    }
}
