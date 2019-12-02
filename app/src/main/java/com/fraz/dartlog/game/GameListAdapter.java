package com.fraz.dartlog.game;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fraz.dartlog.databinding.GamePlayerListItemBinding;
import com.fraz.dartlog.util.BindingRecyclerViewHolder;
import com.fraz.dartlog.viewmodel.GameViewModel;
import com.fraz.dartlog.viewmodel.PlayerViewModel;

public abstract class GameListAdapter
        extends RecyclerView.Adapter<BindingRecyclerViewHolder<GamePlayerListItemBinding>> {

    protected GameViewModel viewModel;

    public GameListAdapter(GameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public abstract BindingRecyclerViewHolder<GamePlayerListItemBinding> onCreateViewHolder(
            ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return viewModel.getGame().getNumberOfPlayers();
    }

    @Override
    public void onBindViewHolder(BindingRecyclerViewHolder<GamePlayerListItemBinding> holder, int position) {
        PlayerViewModel playerViewModel = viewModel.getPlayerViewModel(position);
        holder.getBinding().setViewModel(playerViewModel);
        holder.getBinding().setGameViewModel(viewModel);
        holder.getBinding().setPosition(position);
    }
}