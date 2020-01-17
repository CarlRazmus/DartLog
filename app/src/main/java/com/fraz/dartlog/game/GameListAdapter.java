package com.fraz.dartlog.game;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ViewDataBinding;
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
    private LifecycleOwner lifecycleOwner;

    public GameListAdapter(GameViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public abstract GamePlayerListItemBinding onCreateBinding(ViewGroup parent);

    @NonNull
    @Override
    public BindingRecyclerViewHolder<GamePlayerListItemBinding> onCreateViewHolder(
            ViewGroup parent, int viewType){
        GamePlayerListItemBinding binding = onCreateBinding(parent);
        setLifecycleOwner(binding);
        return new BindingRecyclerViewHolder<>(binding);
    }

    @Override
    public int getItemCount() {
        return viewModel.getGame().getNumberOfPlayers();
    }

    @Override
    public void onBindViewHolder(BindingRecyclerViewHolder<GamePlayerListItemBinding> holder, int position) {
        PlayerViewModel playerViewModel = viewModel.getPlayerViewModel(position);
        holder.getBinding().setViewModel(playerViewModel);
    }

    public void setLifecycleOwner(ViewDataBinding binding)
    {
        binding.setLifecycleOwner(lifecycleOwner);
    }
}