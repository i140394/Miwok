package com.example.personal.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ColorsFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener
            audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //Play the audio back if we regain the AUDIO FOCUS
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //release all resources when we loss the AUDIO FOCUS
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Pause the audio when we temporarily the lose the AUDIO FOCUS
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
        }
    };

    public ColorsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("weṭeṭṭi", "red", R.drawable.color_red, R.raw.color_red));
        colors.add(new Word("chokokki", "green", R.drawable.color_green, R.raw.color_green));
        colors.add(new Word("ṭakaakki", "brown", R.drawable.color_brown, R.raw.color_brown));
        colors.add(new Word("ṭopoppi", "gray", R.drawable.color_gray, R.raw.color_gray));
        colors.add(new Word("kululli", "black", R.drawable.color_black, R.raw.color_black));
        colors.add(new Word("kelelli", "white", R.drawable.color_white, R.raw.color_white));
        colors.add(new Word("ṭopiisә", "dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colors.add(new Word("chiwiiṭә", "mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        ListView number_listView = rootView.findViewById(R.id.list);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter adapter = new WordAdapter(getActivity(), colors, R.color.colors_color);

        number_listView.setAdapter(adapter);

        number_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Word word = colors.get(position);

                releaseMediaPlayer();

                // Obtaining the AUDIO FOCUS to play the audio file
                int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                // if we granted the AUDIO FOCUS then we would play audio
                // otherwise not
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceID());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(completionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            // regardless of the current state of the media player
            // release all resources that took
            mediaPlayer.release();

            // set the media player to null, indicates that
            // it is not configured to play any audio file
            mediaPlayer = null;

            // abandon the audio focus so that other component can use it
            mAudioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
}
