package com.example.nikhil.youtubeplayback.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nikhil.youtubeplayback.Rest.Config;
import com.example.nikhil.youtubeplayback.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class FullScreenYoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;

    String codeUrl;

    YouTubePlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);


        youTubePlayerView=findViewById(R.id.youtubeFullView);

        youTubePlayerView.initialize(Config.DEVELOPER_KEY,this);

        codeUrl=getIntent().getStringExtra("videoCode");

    }

    @Override
    public void onBackPressed() {

        Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra("playTime", player.getCurrentTimeMillis()+"");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b){

    youTubePlayer.loadVideo(codeUrl);

    player=youTubePlayer;

        Log.v("currentmili",getIntent().getStringExtra("playTime"));

        final int pos= Integer.parseInt(getIntent().getStringExtra("playTime"));

    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

            youTubePlayer.seekToMillis(pos);

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }


}
