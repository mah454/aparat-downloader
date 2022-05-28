package ir.moke.aparat;

import com.google.gson.Gson;
import ir.moke.aparat.download.Downloader;
import ir.moke.aparat.model.Link;
import ir.moke.aparat.model.Video;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainClass {

    private static final Map<String, String> LINKS = new LinkedHashMap<>();
    private static final Map<QUALITY, URL> QUALITIES = new LinkedHashMap<>();
    private static final String CURRENT_WORKING_DIR = System.getProperty("user.dir");
    private static String SELECTED_QUALITY = "BEST";

    private static final Gson gson = new Gson();

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

        List<String> stringList = AparatHttpClient.extractPlaylist(PLAY_LIST_PATH);

        for (int i = 0; i < stringList.size(); i++) {
            String hash = stringList.get(i);
            Video video = AparatHttpClient.extractVideoInformation(hash);

            List<Link> links = video.links();
            if (SELECTED_QUALITY.equalsIgnoreCase("BEST")) {
                url = links.get(links.size() - 1).url();
            } else {
                url = links.stream()
                        .filter(item -> item.profile().equalsIgnoreCase(SELECTED_QUALITY))
                        .map(Link::url)
                        .findFirst()
                        .orElse(null);
            }

            if (url == null) {
                System.out.println("Skip : " + video.title());
                System.out.println("Video quality does not exists");
            } else {

                String id = String.format("%02d", i);
                String finalFileName = id + "_" + video.title() + ".mp4";
                File targetFile = new File(CURRENT_WORKING_DIR + "/" + finalFileName);
                System.out.println(finalFileName);
                Downloader.instance.download(url, targetFile);
                System.out.println("\n");

                QUALITIES.clear();

                System.out.println("--------------------------------");
                System.out.println(i + " File Downloaded");
                System.out.println("Download Playlist Completed");
            }
        }
    }
}
