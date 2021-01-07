package ir.moke.aparat;

import ir.moke.aparat.download.Downloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainClass {

    private static final String APARAT_URL = "https://www.aparat.com";
    private static final Map<String, String> LINKS = new LinkedHashMap<>();
    private static final Map<QUALITY, URL> QUALITIES = new LinkedHashMap<>();
    private static final String CURRENT_WORKING_DIR = System.getProperty("user.dir");
    private static String SELECTED_QUALITY = "BEST";

    public static void main(String[] args) throws Exception {
        String urlStr = args[0];
        URL url = new URL(urlStr);
        String PLAY_LIST_PATH = url.getPath();

        if (args.length > 1) {
            SELECTED_QUALITY = args[1];
        }

        System.out.println("Download playlist url : " + APARAT_URL + PLAY_LIST_PATH);
        extractPlaylistLinks(APARAT_URL + PLAY_LIST_PATH);

        int i = 0;
        for (Map.Entry<String, String> linkMap : LINKS.entrySet()) {
            String title = linkMap.getKey();
            String href = linkMap.getValue();
            String videPage = APARAT_URL + href;
            extractVideoQualities(videPage);

            if (SELECTED_QUALITY.equalsIgnoreCase("BEST")) {
                url = QUALITIES.get(QUALITIES.keySet().toArray()[QUALITIES.size() - 1]);
            } else {
                url = QUALITIES.get(QUALITY.getQuality(SELECTED_QUALITY));
            }

            String finalFileName = i + "_" + title + ".mp4";
            File targetFile = new File(CURRENT_WORKING_DIR + "/" + finalFileName);
            System.out.println(finalFileName);
            Downloader.instance.download(url, targetFile);
            System.out.println("\n");

            QUALITIES.clear();
            i++;
        }
        System.out.println("\n");
    }


    public static void extractVideoQualities(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements videoDownloadableLinks = doc.select("a[href*=aparat-video]");
            for (Element downloadableLink : videoDownloadableLinks) {
                String href = downloadableLink.attr("href");
                String title = downloadableLink.text();
                String quality = title.replaceAll("[^A-Za-z0-9]", "");
                QUALITIES.put(QUALITY.getQuality(quality.replaceAll("p", "")), new URL(href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractPlaylistLinks(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("a[href*=?playlist]");
            for (Element element : elements) {
                if (element.hasAttr("title")) {
                    String href = element.attr("href");
                    String title = element.attr("title");
                    LINKS.put(title, href);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
