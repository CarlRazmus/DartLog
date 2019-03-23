package com.fraz.dartlog.game.x01;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.fraz.dartlog.R;

public class X01SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.x01_preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {

            ListView listView = getView().findViewById(android.R.id.list);
            Adapter adapter = listView.getAdapter();

            if (adapter != null) {
                int height = listView.getPaddingTop() + listView.getPaddingBottom();

                for (int i = 0; i < adapter.getCount(); i++) {
                    View item = adapter.getView(i, null, listView);

                    item.measure(0, 0);
                    height += item.getMeasuredHeight();
                }
                FrameLayout frame = getActivity().findViewById(R.id.game_preferences);

                ViewGroup.LayoutParams param = frame.getLayoutParams();
                param.height = height + (listView.getDividerHeight() * adapter.getCount());
                frame.setLayoutParams(param);
            }
        }
    }
}
