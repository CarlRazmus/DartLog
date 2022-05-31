package com.fraz.dartlog.util;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

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
