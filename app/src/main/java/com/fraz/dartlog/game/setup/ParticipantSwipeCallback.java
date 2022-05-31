package com.fraz.dartlog.game.setup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;


class ParticipantSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ParticipantsListRecyclerAdapter participantsListAdapter;


    public ParticipantSwipeCallback(int dragDirs, int swipeDirs,
                                    ParticipantsListRecyclerAdapter participantsListAdapter) {
        super(dragDirs, swipeDirs);
        this.participantsListAdapter = participantsListAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        final int fromPos = viewHolder.getAdapterPosition();
        final int toPos = target.getAdapterPosition();

        participantsListAdapter.onItemMove(fromPos, toPos);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        participantsListAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
