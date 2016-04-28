package com.fraz.dartlog.game.settings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.List;


public class ParticipantsListAdapter extends ArrayAdapter<String> {

    private List<String> participants;
    private Activity context;

    public ParticipantsListAdapter(Activity context, List<String> participants) {
        super(context, R.layout.participant_list_item, participants);

        this.context = context;
        this.participants = participants;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = convertView;

        if (convertView == null) {
            listItem = inflater.inflate(R.layout.participant_list_item, parent, false);
        }

        TextView participantNameView = (TextView) listItem.findViewById(R.id.participant_name);
        participantNameView.setText(participants.get(position));

        Button removeButton = (Button) listItem.findViewById(R.id.remove_participant_button);
        removeButton.setText("X");

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return listItem;
    }
}
