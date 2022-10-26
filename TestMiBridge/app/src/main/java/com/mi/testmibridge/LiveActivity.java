package com.mi.testmibridge;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.mi.mibridge.MiBridge;

public class LiveActivity extends AppCompatActivity {

    private static final String TAG = "LiveActivity";

    private static final int SEND_GIFT_BARRAGE = 20201;

    private static final int CLOSE_GIFT_BARRAGE = 20202;

    private Button sendGiftBtn;

    private Button closeGiftBtn;

    private PlayerView playerView;

    private @Nullable ExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        playerView = findViewById(R.id.player_view);
        sendGiftBtn = findViewById(R.id.send_gift);
        closeGiftBtn = findViewById(R.id.close_gift);


        String packageName = getApplicationContext().getPackageName();
        int bridgeUid = android.os.Process.myUid();
        sendGiftBtn.setOnClickListener(v -> {
            MiBridge.setScene(bridgeUid, packageName, SEND_GIFT_BARRAGE);
            sendGiftBtn.setEnabled(false);
            closeGiftBtn.setEnabled(true);
            changeRate(120);


        });
        closeGiftBtn.setOnClickListener(v -> {
            MiBridge.setScene(bridgeUid, packageName, CLOSE_GIFT_BARRAGE);
            sendGiftBtn.setEnabled(true);
            closeGiftBtn.setEnabled(false);
            changeRate(24);
        });
    }

    @SuppressLint("WrongConstant")
    private void changeRate(int rate) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            return;
        }
        View view = playerView.getVideoSurfaceView();
        if (view instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) view;
            Surface surface = surfaceView.getHolder().getSurface();
            surface.setFrameRate(rate, Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE, 1);
            Log.e(TAG, "changeRate to " + rate + "Hz");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        if (playerView != null) {
            playerView.onResume();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            ExoPlayer.Builder playerBuilder = new ExoPlayer.Builder(/* context= */ this);
            player = playerBuilder.build();
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny);
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);
            playerView.setPlayer(player);
        }
        player.prepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (playerView != null) {
            playerView.onPause();
        }
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
   }

    protected void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            playerView.setPlayer(/* player= */ null);
        }
        playerView.getAdViewGroup().removeAllViews();
    }
}