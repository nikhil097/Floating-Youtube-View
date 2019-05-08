package com.example.nikhil.youtubeplayback.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.nikhil.youtubeplayback.ApiResponseBody.LinkResponseBody;
import com.example.nikhil.youtubeplayback.Rest.ApiService;
import com.example.nikhil.youtubeplayback.Rest.Config;
import com.example.nikhil.youtubeplayback.R;
import com.example.nikhil.youtubeplayback.Rest.ResponseCallback;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private static final int RECOVERY_DIALOG_REQUEST = 1;

    RelativeLayout mainLayout;
    YouTubePlayerView youTubeView;
    CustomViewYoutube customViewYoutube;

    ApiService apiService;

    int pos=0;

    boolean isplayPauseVisible=false;

    Button fullScreenMode;

    String link=null;

    View view;

    YouTubePlayer player;
    String codeUrl;

    Button closeView,playBtn,pauseBtn;

    float dX,dY;
    private String rawUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout=findViewById(R.id.mainView);



        apiService=new ApiService();


                if (player!=null)
                {
                    player.release();
                    player=null;
                }
                if(view!=null)
                {
                    mainLayout.removeView(view);
                }

                view=getLayoutInflater().inflate(R.layout.window_mode,null);
                mainLayout.addView(view);

                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) view.getLayoutParams();
                int px = Math.round(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 200
                        ,getResources().getDisplayMetrics()));
                params.height=px;
         //       params.width=400;
                params.setMargins(16,16,16,16);

                view.setBackground(getResources().getDrawable(R.drawable.custom_view_background,null));

                view.setLayoutParams(params);



                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.v("pos", event.getX() + "");
                        Log.v("position", event.getX() + "");

                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:

                                if(!isplayPauseVisible)
                                {
                                    if(player.isPlaying())
                                    {
                                        pauseBtn.setVisibility(View.VISIBLE);
                                        playBtn.setVisibility(View.GONE);
                                    }
                                    else{
                                        pauseBtn.setVisibility(View.GONE);
                                        playBtn.setVisibility(View.VISIBLE);
                                    }
                                    closeView.setVisibility(View.VISIBLE);
                                    fullScreenMode.setVisibility(View.VISIBLE);
                                }
                                else{
                                    playBtn.setVisibility(View.GONE);
                                    pauseBtn.setVisibility(View.GONE);
                                    closeView.setVisibility(View.GONE);
                                    fullScreenMode.setVisibility(View.GONE);
                                }

                                isplayPauseVisible=!isplayPauseVisible;

                                hideUnhideOptions();

                                dX = view.getX() - event.getRawX();
                                dY = view.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:

                                view.animate()
                                        .x(event.getRawX() + dX)
                                        .y(event.getRawY() + dY)
                                        .setDuration(0)
                                        .start();
                                break;

                        }


                        view.invalidate();


                        return true;

                    }
                });





                customViewYoutube=view.findViewById(R.id.customYoutubeView);

                playBtn=view.findViewById(R.id.playButton);
                pauseBtn=view.findViewById(R.id.pauseButton);

                fullScreenMode=view.findViewById(R.id.fullScreenMode_Btn);

                fullScreenMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivityForResult(new Intent(MainActivity.this,FullScreenYoutubeActivity.class).putExtra("videoCode",codeUrl).putExtra("playTime",player.getCurrentTimeMillis()+""),2);

                        player.release();
                        player=null;
                    }
                });

                playBtn.setVisibility(View.GONE);

                pauseBtn.setVisibility(View.GONE);


                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!player.isPlaying())
                        {
                            player.play();
                            playBtn.setVisibility(View.GONE);
                            pauseBtn.setVisibility(View.VISIBLE);
                            hideUnhideOptions();
                        }
                    }
                });

                pauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(player.isPlaying())
                        {
                            player.pause();
                            pauseBtn.setVisibility(View.GONE);
                            playBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });


//                youTubeView=customViewYoutube.findViewById(R.id.youtubeView);


                youTubeView=customViewYoutube.getView().findViewById(R.id.youtubeView);


                customViewYoutube.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });


                closeView=view.findViewById(R.id.closeView);

                closeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(player!=null) {
                            player.release();
                            player=null;
                        }
                        mainLayout.removeView(view);
                    }
                });




         /*       youTubeView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return false;

                    }

                });




                youTubeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("position1","hi");
                    }
                });
*/

         fetchUrl();





    }


    public void hideUnhideOptions()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {



                if (player!=null) {
                    if (player.isPlaying()) {
                        pauseBtn.setVisibility(View.GONE);
                        closeView.setVisibility(View.GONE);
                        fullScreenMode.setVisibility(View.GONE);
                    }
                }
            }
        }, 5000);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        player=null;

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (true) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically


            codeUrl=rawUrl.trim().substring(rawUrl.trim().lastIndexOf("/")+1);

            youTubePlayer.loadVideo(codeUrl);

            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);


            player=youTubePlayer;

            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
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

                    player.seekToMillis(pos);
                    pos=0;
                    playBtn.setVisibility(View.GONE);
                    pauseBtn.setVisibility(View.GONE);
                    closeView.setVisibility(View.GONE);
                    fullScreenMode.setVisibility(View.GONE);
                    isplayPauseVisible=false;

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });

        }

    }

    private String fetchUrl() {


        apiService.getLink(new ResponseCallback<LinkResponseBody>() {
            @Override
            public void success(LinkResponseBody linkResponseBody) {
                rawUrl=linkResponseBody.getLink();
                youTubeView.initialize(Config.DEVELOPER_KEY, MainActivity.this);
            }

            @Override
            public void failure(LinkResponseBody linkResponseBody) {

            }
        });

        return null;
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
        else if(requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                pos = Integer.parseInt(data.getStringExtra("playTime"));
                getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY,this);
            }
        }

    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

}
