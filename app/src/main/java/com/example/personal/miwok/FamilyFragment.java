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


public class FamilyFragment extends Fragment {

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


    public FamilyFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> family_members = new ArrayList<>();
        family_members.add(new Word("әpә", "father", R.drawable.family_father, R.raw.family_father));
        family_members.add(new Word("әṭa", "mother", R.drawable.family_mother, R.raw.family_mother));
        family_members.add(new Word("angsi", "son", R.drawable.family_son, R.raw.family_son));
        family_members.add(new Word("tune", "daughter", R.drawable.family_daughter, R.raw.family_daughter));
        family_members.add(new Word("taachi", "older brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        family_members.add(new Word("chalitti", "younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        family_members.add(new Word("teṭe", "older sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        family_members.add(new Word("kolliti", "younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        family_members.add(new Word("ama", "grand mother", R.drawable.family_grandmother, R.raw.family_grandmother));
        family_members.add(new Word("paapa", "grand father", R.drawable.family_grandfather, R.raw.family_grandfather));

        ListView number_listView = rootView.findViewById(R.id.list);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter adapter = new WordAdapter(getActivity(), family_members, R.color.family_members_color);

        number_listView.setAdapter(adapter);

        number_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Word word = family_members.get(position);

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
