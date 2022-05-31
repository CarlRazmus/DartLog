package com.fraz.dartlog.game.random;

import androidx.lifecycle.LifecycleOwner;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fraz.dartlog.databinding.GamePlayerListItemBinding;
import com.fraz.dartlog.game.GameListAdapter;
import com.fraz.dartlog.viewmodel.RandomGameViewModel;

/**
 * Created by CarlR on 09/10/2016.
 */
public class RandomGameListAdapter extends GameListAdapter {

    public RandomGameListAdapter(RandomGameViewModel viewModel, LifecycleOwner lifecycleOwner) {
        super(viewModel, lifecycleOwner);
    }

    @Override
    public GamePlayerListItemBinding onCreateBinding(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return GamePlayerListItemBinding.inflate(layoutInflater, parent, false);
    }
}
