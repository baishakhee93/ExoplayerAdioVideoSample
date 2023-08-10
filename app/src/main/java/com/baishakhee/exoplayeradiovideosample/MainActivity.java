package com.baishakhee.exoplayeradiovideosample;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ExoPlayer player = null;
    PlayerView videoView;
    private String uri = "https://www.youtube.com/watch?v=AasI-0IRXUM";
    boolean fullscreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.video_view);
        // player = new ExoPlayer.Builder(ExoplayerActivity.this).build();
        // videoView.setPlayer(player);
        HttpDataSource.Factory httpDataSourceFactory =
                new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true);

        DefaultDataSource.Factory dataSourceFactory =
                new DefaultDataSource.Factory(MainActivity.this, httpDataSourceFactory);

        player =
                new ExoPlayer.Builder(MainActivity.this)
                        .setMediaSourceFactory(new DefaultMediaSourceFactory(dataSourceFactory))
                        .build();
        videoView.setPlayer(player);

        try {
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView fullscreenButton = videoView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_fullscreen_24));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    //params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    params.height = params.WRAP_CONTENT;
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    videoView.setLayoutParams(params);
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    fullscreen = false;
                } else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_fullscreen_exit_24));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    videoView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
        }
    }
}