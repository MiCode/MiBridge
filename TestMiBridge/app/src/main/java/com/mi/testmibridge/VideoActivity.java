package com.mi.testmibridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.mi.mibridge.MiBridge;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";

    private static final int OPEN_BARRAGE = 10102;

    private static final int CLOSE_BARRAGE = 10103;

    private PlayerView playerView;

    private Button openBarBtn;

    private Button closeBarBtn;

    private TextView introText;

    private TextView openBarIntroText;

    private TextView closeBarIntroText;

    protected @Nullable ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        playerView = findViewById(R.id.player_view);
        openBarBtn = findViewById(R.id.send_gift);
        closeBarBtn = findViewById(R.id.close_gift);
        introText = findViewById(R.id.text_introduce);
        introText.setText(R.string.text_introduce);
        playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();

        String packageName = getApplicationContext().getPackageName();
        int bridgeUid = android.os.Process.myUid();
        openBarBtn.setOnClickListener(v -> {
            MiBridge.setScene(bridgeUid, packageName, OPEN_BARRAGE);
            openBarBtn.setEnabled(false);
            closeBarBtn.setEnabled(true);
            changeRate(120);


        });
        closeBarBtn.setOnClickListener(v -> {
            MiBridge.setScene(bridgeUid, packageName, CLOSE_BARRAGE);
            openBarBtn.setEnabled(true);
            closeBarBtn.setEnabled(false);
            changeRate(30);
        });

        Display display = getWindowManager().getDefaultDisplay();
        Display.Mode[] modes = display.getSupportedModes();
        for (Display.Mode mode :modes){
            Log.e(TAG,"mode is " + mode);
        }
        DisplayManager displayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(new MyListener(),new Handler(Looper.getMainLooper()));
    }

    private class MyListener implements DisplayManager.DisplayListener{

        @Override
        public void onDisplayAdded(int displayId) {
            Log.e(TAG, "onDisplayAdded: ");
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            Log.e(TAG, "onDisplayRemoved: ");
        }

        @Override
        public void onDisplayChanged(int displayId) {
            Log.e(TAG, "onDisplayChanged: ");
        }
    }

    @SuppressLint("WrongConstant")
    private void changeRate(int rate){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){
            return;
        }
        View view =  playerView.getVideoSurfaceView();
        if (view instanceof SurfaceView){
            SurfaceView surfaceView = (SurfaceView)view;
            Surface surface = surfaceView.getHolder().getSurface();
            surface.setFrameRate(rate,Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE,1);
            Log.e(TAG,"changeRate to " + rate + "Hz");
        }
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
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
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.android_screens_lavf);
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

    private class PlayerErrorMessageProvider implements ErrorMessageProvider<PlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(PlaybackException e) {
            String errorString = getString(R.string.error_generic);
            Throwable cause = e.getCause();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.codecInfo == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString =
                                getString(
                                        R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                    } else {
                        errorString =
                                getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                    }
                } else {
                    errorString =
                            getString(
                                    R.string.error_instantiating_decoder,
                                    decoderInitializationException.codecInfo.name);
                }
            }
            return Pair.create(0, errorString);
        }
    }
}