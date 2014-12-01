package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityAdapter<E> extends ArrayAdapter<E> {

    protected final LayoutInflater inflater;
    public EntityAdapter(Context context) {
        super(context, -1, new ArrayList<E>());
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<E> loaded = loadData();
        if (loaded != null) {
            addAll(loaded);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public abstract List<E> loadData();

    public abstract void refreshData();
}