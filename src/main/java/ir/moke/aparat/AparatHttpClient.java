package ir.moke.aparat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.moke.aparat.model.Link;
import ir.moke.aparat.model.Video;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AparatHttpClient {
    private static final String VIDEO_API_URI = "/api/fa/v1/video/video/show/videohash/";
    private static final String PLAYLIST_API_URI = "/api/fa/v1/video/playlist/one/playlist_id/";
    private static final String APARAT_URL = "https://www.aparat.com";

    public static String getURILastIndex(URI uri) {
        String[] split = uri.getPath().split("/");
        return split[split.length - 1];
    }

    public static List<String> extractPlaylist(String url) {
        URI uri = URI.create(url);
        String playListId = getURILastIndex(uri);
        JsonObject jsonObject = callApi(PLAYLIST_API_URI, playListId);
        List<String> videoHashList = new ArrayList<>();

        for (JsonElement included : jsonObject.get("included").getAsJsonArray()) {
            JsonElement attributes = included.getAsJsonObject().get("attributes");
            if (attributes != null && !attributes.isJsonNull()) {
                JsonElement uid = attributes.getAsJsonObject().get("uid");
                if (uid != null && !uid.isJsonNull()) {
                    videoHashList.add(uid.getAsString());
                }
            }
        }
        return videoHashList;
    }

    public static Video extractVideoInformation(String videoHash) {
        JsonObject jsonObject = callApi(VIDEO_API_URI, videoHash);
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        JsonObject attributes = data.get("attributes").getAsJsonObject();
        String title = attributes.get("title").getAsString();
        JsonArray file_link_all = attributes.get("file_link_all").getAsJsonArray();
        List<Link> linkList = new ArrayList<>();
        for (JsonElement jsonElement : file_link_all) {
            String profile = jsonElement.getAsJsonObject().get("profile").getAsString();
            String urlStr = jsonElement.getAsJsonObject().get("urls").getAsJsonArray().get(0).getAsString();
            try {
                URL url = new URL(urlStr);
                Link link = new Link(url, profile.substring(0, profile.length() - 1));
                linkList.add(link);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        }
        return new Video(title, linkList);
    }

    private static JsonObject callApi(String api, String key) {
        URI uri = URI.create(APARAT_URL + api + key);
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(SSLTrustManager.instance.getSslContext())
                    .build();
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            return JsonUtils.fromJson(body);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
