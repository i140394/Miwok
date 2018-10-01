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


public class NumbersFragment extends Fragment {

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

    public NumbersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> numbers = new ArrayList<>();
        numbers.add(new Word("lutti", "one", R.drawable.number_one, R.raw.number_one));
        numbers.add(new Word("otiiko", "two", R.drawable.number_two, R.raw.number_two));
        numbers.add(new Word("tolookosu", "three", R.drawable.number_three, R.raw.number_three));
        numbers.add(new Word("oyyisa", "four", R.drawable.number_four, R.raw.number_four));
        numbers.add(new Word("massokka", "five", R.drawable.number_five, R.raw.number_five));
        numbers.add(new Word("temmokka", "six", R.drawable.number_six, R.raw.number_six));
        numbers.add(new Word("kenekaku", "seven", R.drawable.number_seven, R.raw.number_seven));
        numbers.add(new Word("kawinta", "eight", R.drawable.number_eight, R.raw.number_eight));
        numbers.add(new Word("wo’e", "nine", R.drawable.number_nine, R.raw.number_nine));
        numbers.add(new Word("na’aacha", "ten", R.drawable.number_ten, R.raw.number_ten));

        ListView number_listView = rootView.findViewById(R.id.list);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter adapter = new WordAdapter(getActivity(), numbers, R.color.numbers_color);
        number_listView.setAdapter(adapter);

        number_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Word word = numbers.get(position);

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
