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


public class PhrasesFragment extends Fragment {

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


    public PhrasesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> phrases = new ArrayList<>();
        phrases.add(new Word("minto wuksus", "Where are you going?", R.raw.phrase_where_are_you_going));
        phrases.add(new Word("tinnә oyaase'nә", "What is your name?", R.raw.phrase_what_is_your_name));
        phrases.add(new Word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        phrases.add(new Word("michәksәs?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("kuchi achit", "I’m feeling good.", R.raw.phrase_im_feeling_good));
        phrases.add(new Word("әәnәs'aa?", "Are you coming?", R.raw.phrase_are_you_coming));
        phrases.add(new Word("hәә’ әәnәm", "Yes, I’m coming.", R.raw.phrase_im_coming));
        phrases.add(new Word("әәnәm", "I’m coming.", R.raw.phrase_yes_im_coming));
        phrases.add(new Word("yoowutis", "Let’s go.", R.raw.phrase_lets_go));
        phrases.add(new Word("әnni'nem", "Come here.", R.raw.phrase_come_here));

        ListView number_listView = rootView.findViewById(R.id.list);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter adapter = new WordAdapter(getActivity(), phrases, R.color.phrases_color);

        number_listView.setAdapter(adapter);

        number_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Word word = phrases.get(position);

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
