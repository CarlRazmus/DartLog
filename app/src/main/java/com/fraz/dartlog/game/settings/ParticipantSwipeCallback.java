package com.fraz.dartlog.game.settings;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

/**
 * Created by CarlR on 04/07/2016.
 */
public class ParticipantSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ArrayList<String> participantsNames;
    private RecyclerView.Adapter participantsListAdapter;


    public ParticipantSwipeCallback(int dragDirs, int swipeDirs, ArrayList<String> participantsNames,
                                    RecyclerView.Adapter participantsListAdapter) {
        super(dragDirs, swipeDirs);

        this.participantsNames = participantsNames;
        this.participantsListAdapter = participantsListAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        participantsNames.remove(viewHolder.getAdapterPosition());
        participantsListAdapter.notifyDataSetChanged();
    }
}
