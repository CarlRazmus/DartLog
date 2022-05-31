package com.fraz.dartlog.game.x01;

import androidx.lifecycle.LifecycleOwner;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.GamePlayerListItemBinding;
import com.fraz.dartlog.game.GameListAdapter;
import com.fraz.dartlog.util.BindingRecyclerViewHolder;
import com.fraz.dartlog.viewmodel.X01GameViewModel;

public class X01GameListAdapter extends GameListAdapter {


    public X01GameListAdapter(X01GameViewModel viewModel, LifecycleOwner lifecycleOwner) {
        super(viewModel, lifecycleOwner);
    }

    @Override
    public GamePlayerListItemBinding onCreateBinding(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return GamePlayerListItemBinding.inflate(layoutInflater, parent, false);
    }

    @NonNull
    @Override
    public BindingRecyclerViewHolder<GamePlayerListItemBinding> onCreateViewHolder(
            ViewGroup parent, int viewType){
        GamePlayerListItemBinding binding = onCreateBinding(parent);
        setLifecycleOwner(binding);
        return new X01ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerViewHolder<GamePlayerListItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);
        final X01PlayerData player = (X01PlayerData) this.viewModel.getGame().getPlayer(position);
        updateCheckoutView(player, (X01ViewHolder) holder);
    }

    class X01ViewHolder extends BindingRecyclerViewHolder<GamePlayerListItemBinding> {
        TextView checkout;
        TextView checkoutLabel;
        ViewGroup checkout_view;

        X01ViewHolder(GamePlayerListItemBinding binding) {
            super(binding);
            checkout = binding.getRoot().findViewById(R.id.checkout);
            checkoutLabel = binding.getRoot().findViewById(R.id.checkout_label);
            checkout_view = binding.getRoot().findViewById(R.id.checkout_view);
        }
    }

    private void updateCheckoutView(X01PlayerData player, X01ViewHolder holder) {
        if (player.getScore() == 0) {
            holder.checkout_view.setVisibility(View.VISIBLE);
            holder.checkout.setText("");
            holder.checkoutLabel.setText(R.string.result_win);
        }
        else {
            holder.checkout.setText(player.getCheckoutText());
            switch (player.getCurrentCheckoutType()) {
                case DOUBLE:
                    holder.checkoutLabel.setText(R.string.double_out);
                    break;
                case DOUBLE_ATTEMPT:
                    holder.checkoutLabel.setText(R.string.double_out_attempts);
                    String label = (String) holder.checkoutLabel.getText();
                    holder.checkoutLabel.setText(String.format(label,
                            player.getRemainingDoubleOutAttempts()));
                    break;
                case SINGLE:
                    holder.checkoutLabel.setText(R.string.single_out);
            }
        }
    }
}