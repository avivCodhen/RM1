package com.strongest.savingdata.Handlers;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.YoutubeAPI.VideoItem;
import com.strongest.savingdata.YoutubeAPI.YoutubeConfig;
import com.strongest.savingdata.YoutubeAPI.YoutubeConnector;

import java.util.List;

public class YoutubeHandler {

    private static Context context;
    YouTubePlayer YPlayer;
    List<VideoItem> searchResults;
    Handler handler;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();



    private YoutubeHandler() {
        handler = new Handler();
    }

    public static YoutubeHandler getHandler(Context context) {
        YoutubeHandler.context = context;
        return new YoutubeHandler();
    }

    public void searchOnYoutube(final String keywords) {
        new Thread() {
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(context);
                searchResults = yc.search(keywords);
                handler.post(() -> {
                    if (YPlayer != null) {
                        YPlayer.cueVideo(searchResults.get(0).getId());
                    } else {
                        /**
                         *
                         * */
                        if(youTubePlayerFragment != null)
                        youTubePlayerFragment.initialize(YoutubeConfig.getYoutubeApiKey(), onInitializedListener);
                    }

                });
            }
        }.start();

    }


    public YoutubeHandler init(int layout, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(layout, youTubePlayerFragment).commit();

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                YPlayer = youTubePlayer;
                YPlayer.setShowFullscreenButton(false);
                YPlayer.cueVideo(searchResults.get(0).getId());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        return this;
    }

    public void stop(){
        onInitializedListener = null;
        youTubePlayerFragment = null;
    }

}
