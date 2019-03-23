package com.fraz.dartlog.game.x01;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameListAdapter;

public class X01GameListAdapter extends GameListAdapter<X01GameListAdapter.X01ViewHolder> {


    public X01GameListAdapter(X01 game) {
        super(game);
    }

    @Override
    public void onBindViewHolder(X01ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final X01PlayerData player = (X01PlayerData) this.game.getPlayer(position);
        updateCheckoutView(player, holder);
    }

    @Override
    public X01ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_player_list_item, parent, false);
        return new X01ViewHolder(listItem);
    }

    class X01ViewHolder extends GameListAdapter.ViewHolder {
        TextView checkout;
        TextView checkoutLabel;
        ViewGroup checkout_view;

        X01ViewHolder(View itemView) {
            super(itemView);
            checkout = itemView.findViewById(R.id.checkout);
            checkoutLabel = itemView.findViewById(R.id.checkout_label);
            checkout_view = itemView.findViewById(R.id.checkout_view);
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