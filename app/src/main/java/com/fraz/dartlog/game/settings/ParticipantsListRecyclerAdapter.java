package com.fraz.dartlog.game.settings;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View participantListItemView) {
            super(participantListItemView);
            mTextView = (TextView)participantListItemView.findViewById(R.id.participant_name);
        }
    }

    public ParticipantsListRecyclerAdapter(ArrayList<String> participants) {
        this.participants = participants;
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
        Log.d("onItemMove", "onItemMove was executed");
        return true;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ParticipantsListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list_item, parent, false);

        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(participants.get(position));
    }


    @Override
    public int getItemCount() {
        return participants.size();
    }

    public void add(int position, String item) {
        participants.add(position, item);
        notifyItemInserted(position);
    }

}
