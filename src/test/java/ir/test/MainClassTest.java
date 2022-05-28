package ir.test;

import ir.moke.aparat.AparatHttpClient;
import ir.moke.aparat.model.Video;

import java.util.List;

public class MainClassTest {

    public static void main(String[] args) {
        String str = "https://www.aparat.com/playlist/172811";

        List<String> stringList = AparatHttpClient.extractPlaylist(str);

        for (String hash : stringList) {
            Video video = AparatHttpClient.extractVideoInformation(hash);
            if (video != null) System.out.println(video.title());
        }

    }
}


