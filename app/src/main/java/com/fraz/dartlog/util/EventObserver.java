package com.fraz.dartlog.util;

import androidx.lifecycle.Observer;

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandled] is *only* called if the [Event]'s contents has not been handled.
 */
public abstract class EventObserver<T> implements Observer<Event<T>>
{
    public void onChanged(Event<T> event)
    {
        if (event != null)
        {
            T value = event.getContentIfNotHandled();
            if (value != null)
                onEventUnhandled(value);
        }
    }

    public abstract void onEventUnhandled(T content);
}