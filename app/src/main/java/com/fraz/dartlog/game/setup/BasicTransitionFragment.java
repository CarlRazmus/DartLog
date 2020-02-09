package com.fraz.dartlog.game.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;

public class BasicTransitionFragment extends Fragment {

    private Scene mScene1;
    private Scene mScene2;

    /** Transitions take place in this ViewGroup. We retain this for the dynamic transition on scene 4. */
    private ViewGroup mSceneRoot;

    public BasicTransitionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setup, container, false);
        assert view != null;

        mSceneRoot = view.findViewById(R.id.activity_root_view);
        mScene1 = new Scene(mSceneRoot, (ViewGroup) mSceneRoot.findViewById(R.id.scene_layout));
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.second_layout, getActivity());

        return view;
    }

    public void initStuff(){
        mSceneRoot = getActivity().findViewById(R.id.activity_root_view);
        mScene1 = new Scene(mSceneRoot, (ViewGroup) mSceneRoot.findViewById(R.id.scene_layout));
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.second_layout, getActivity());
    }

    public void changeScene(){
        TransitionManager.go(mScene2);
    }
}
