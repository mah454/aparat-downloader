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
        if (args.length == 0) {
            System.out.println("Please add aparat playlist link as first parameter");
            System.out.println("Usage: aparat-dl [APARAT_PLAYLIST_LINK] [QUALITY-ID : 144|240|360|480|720|BEST]");
            System.out.println("Example : aparat-dl https://www.aparat.com/playlist/108222");
            System.out.println("Download with custom quality: aparat-dl [APARAT_PLAYLIST_LINK] 720");
            System.exit(1);
        }
        String urlStr = args[0];
        URL url = new URL(urlStr);
        String PLAY_LIST_PATH = url.getPath();

        if (args.length > 1) {
            SELECTED_QUALITY = args[1];
        }

        System.out.println("Download playlist url : " + APARAT_URL + PLAY_LIST_PATH);
        extractPlaylistLinks(APARAT_URL + PLAY_LIST_PATH);

        int i = 1;
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

            String id = String.format("%02d", i);
            String finalFileName = id + "_" + title + ".mp4";
            File targetFile = new File(CURRENT_WORKING_DIR + "/" + finalFileName);
            System.out.println(finalFileName);
            Downloader.instance.download(url, targetFile);
            System.out.println("\n");

            QUALITIES.clear();
            i++;
        }
        System.out.println("--------------------------------");
        System.out.println(i + " File Downloaded");
        System.out.println("Download Playlist Completed");
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
