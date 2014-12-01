package ru.moscowtaxi.android.moscowtaxi.view;

import android.widget.ListView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ListAdapter;


import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.EntityAdapter;

/**
 * Created by ychabatarou on 04.11.2014.
 */
public abstract class EntityListView<E extends EntityORM> extends ListView implements AdapterView.OnItemClickListener {

    public EntityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setAdapter(createAdapter());
    }

     abstract protected EntityAdapter createAdapter();

    public EntityAdapter<E> getAdapter() {
        ListAdapter adapter = super.getAdapter();
        return (EntityAdapter<E>) adapter;
    }

}
