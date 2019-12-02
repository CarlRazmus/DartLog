package com.fraz.dartlog.game.random;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fraz.dartlog.databinding.GamePlayerListItemBinding;
import com.fraz.dartlog.game.GameListAdapter;
import com.fraz.dartlog.util.BindingRecyclerViewHolder;
import com.fraz.dartlog.viewmodel.RandomGameViewModel;

/**
 * Created by CarlR on 09/10/2016.
 */
public class RandomGameListAdapter extends GameListAdapter {

    public RandomGameListAdapter(RandomGameViewModel viewModel) {
        super(viewModel);
    }

    @NonNull
    @Override
    public BindingRecyclerViewHolder<GamePlayerListItemBinding> onCreateViewHolder(
            ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GamePlayerListItemBinding binding = GamePlayerListItemBinding.inflate(layoutInflater, parent, false);
        return new BindingRecyclerViewHolder<>(binding);
    }
}
