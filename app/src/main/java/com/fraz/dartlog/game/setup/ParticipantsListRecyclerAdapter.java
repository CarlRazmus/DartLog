package com.fraz.dartlog.game.setup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by CarlR on 03/07/2016.
 */
public class ParticipantsListRecyclerAdapter extends RecyclerView.Adapter<ParticipantsListRecyclerAdapter.ViewHolder> {
    private ArrayList<String> participants;


    public ParticipantsListRecyclerAdapter(ArrayList<String> participants) {
        this.participants = participants;
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public void onItemDismiss(int position) {
        participants.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(participants, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(participants, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View listItemView;

        public ViewHolder(View participantListItemView) {
            super(participantListItemView);
            listItemView = participantListItemView;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list_item, parent, false);

        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView participantNameTextView = (TextView)holder.listItemView.findViewById(R.id.participant_name);
        participantNameTextView.setText(participants.get(position));
    }

}
