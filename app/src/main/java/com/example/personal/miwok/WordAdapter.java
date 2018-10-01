package com.example.personal.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private int color;

    WordAdapter(Context context, ArrayList<Word> data, int color) {
        super(context, 0, data);
        this.color = color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Word currentWord = getItem(position);

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                                    .inflate(R.layout.list_item, parent, false);
        }

        TextView miwokWord = listItemView.findViewById(R.id.miwokTransation);
        if (currentWord != null) {
            miwokWord.setText(currentWord.getmMiwokTranslation());
        }

        TextView defaultWord = listItemView.findViewById(R.id.defaultTranslation);
        if (currentWord != null) {
            defaultWord.setText(currentWord.getmDefaultTranslation());
        }

        ImageView image = listItemView.findViewById(R.id.image);

        if (currentWord != null) {
            if (currentWord.isImageAvailable()) {
                image.setImageResource(currentWord.getmImageResourceID());
                image.setVisibility(View.VISIBLE);
            } else {
                image.setVisibility(View.GONE);
            }
        }

        View textHolder = listItemView.findViewById(R.id.textHolder);

        int background_color = ContextCompat.getColor(getContext(), color);

        textHolder.setBackgroundColor(background_color);

        ImageView play_btn = listItemView.findViewById(R.id.play_btn);
        play_btn.setBackgroundColor(background_color);

        return listItemView;
    }
}