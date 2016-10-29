package com.fraz.dartlog.game.x01;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameListAdapter;

import java.util.LinkedList;

public class X01GameListAdapter extends GameListAdapter {

    private X01 game;

    public X01GameListAdapter(X01 game) {
        super(game);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final X01PlayerData player = (X01PlayerData) this.game.getPlayer(position);
        updateCheckoutView(player, holder);
    }

    private void updateCheckoutView(X01PlayerData player, ViewHolder holder) {
        if (player.getScore() == 0) {
            holder.checkout_view.setVisibility(View.VISIBLE);
            holder.checkout.setText("");
            holder.checkoutLabel.setText(R.string.result_win);
        }
        else if (holder.getAdapterPosition() == game.getCurrentPlayerIdx()) {
            holder.checkout.setText(player.getCheckoutText());
            holder.checkout_view.setVisibility(View.VISIBLE);
        } else {
            holder.checkout_view.setVisibility(View.GONE);
        }
    }
}