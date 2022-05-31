package com.fraz.dartlog.view;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.statistics.ProfileDetailFragmentPagerAdapter;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProfileViewModel profileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        ViewPager view = (ViewPager) getView();
        if(view != null)
        {
            view.setAdapter(new ProfileDetailFragmentPagerAdapter(getChildFragmentManager(),
                profileViewModel.getProfileName()));
        }
    }
}
