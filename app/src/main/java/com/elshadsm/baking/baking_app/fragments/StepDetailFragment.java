package com.elshadsm.baking.baking_app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.models.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.elshadsm.baking.baking_app.models.Constants.*;

public class StepDetailFragment extends Fragment {

    private static final String STEP_INSTANCE_KEY = "step-instance-key";
    private static final String PLAYER_POSITION_KEY = "player-position-key";
    private static final String PLAYER_STATUS_KEY = "player-status-key";

    @BindView(R.id.step_video_player)
    PlayerView playerView;
    @Nullable
    @BindView(R.id.step_description)
    TextView stepDescription;
    @Nullable
    @BindView(R.id.step_exo_player_thumbnail)
    ImageView stepImageThumbnail;

    private SimpleExoPlayer exoPlayer;
    private Step step;
    private boolean fullScreenMode;
    private long videoLastPosition;
    private boolean videoPlayed;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(STEP_DETAILS_FRAGMENT_ARGUMENT);
            fullScreenMode = getArguments().getBoolean(STEP_DETAILS_FRAGMENT_FULLSCREEN_ARGUMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = fullScreenMode ? R.layout.fragment_step_detail_fullscreen_mode : R.layout.fragment_step_detail;
        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        applyConfiguration(savedInstanceState);
        return view;
    }

    private void applyConfiguration(Bundle savedInstanceState) {
        videoLastPosition = C.TIME_UNSET;
        videoPlayed = true;
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP_INSTANCE_KEY);
            videoLastPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY, C.TIME_UNSET);
            videoPlayed = savedInstanceState.getBoolean(PLAYER_STATUS_KEY, true);
        }
        if (stepDescription != null && step != null) {
            stepDescription.setText(step.getDescription());
        }
        if (stepImageThumbnail != null && step != null && !TextUtils.isEmpty(step.getThumbnailUrl())) {
            stepImageThumbnail.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(step.getThumbnailUrl()).into(stepImageThumbnail);
        }
        if (step != null && !TextUtils.isEmpty(step.getVideoUrl())) {
            playerView.setVisibility(View.VISIBLE);
            initializePlayer(getContext(), Uri.parse(step.getVideoUrl()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STEP_INSTANCE_KEY, step);
        outState.putLong(PLAYER_POSITION_KEY, videoLastPosition);
        outState.putBoolean(PLAYER_STATUS_KEY, videoPlayed);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void initializePlayer(Context context, Uri mediaUri) {
        if (exoPlayer != null) {
            return;
        }
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        playerView.setPlayer(exoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getString(R.string.app_name)));
        MediaSource mediaSource =
                new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaUri);
        exoPlayer.prepare(mediaSource);
        exoPlayer.seekTo(videoLastPosition);
        exoPlayer.setPlayWhenReady(videoPlayed);
    }

    public boolean isFullScreen() {
        return this.fullScreenMode;
    }

    public void setFullScreen(boolean fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            videoLastPosition = exoPlayer.getCurrentPosition();
            videoPlayed = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
