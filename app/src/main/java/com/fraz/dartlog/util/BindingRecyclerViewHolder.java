package com.fraz.dartlog.util;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingRecyclerViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final T mBinding;

    public BindingRecyclerViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding()
    {
        return mBinding;
    }
}
