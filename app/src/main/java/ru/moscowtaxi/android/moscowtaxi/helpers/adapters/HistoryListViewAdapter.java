package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 12/10/14.
 */
public class HistoryListViewAdapter extends BaseAdapter {

    List<OrderHistoryORM> items;
    LayoutInflater inflater;

    public HistoryListViewAdapter(LayoutInflater inflater, List<OrderHistoryORM> data) {
        this.inflater = inflater;
        items = data;
    }


    @Override
    public int getCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        if (items == null)
            return null;
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (items == null)
            return -1;
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = inflater
                    .inflate(R.layout.list_item_history, null);
            convertView.setTag(holder);

            final Context context = convertView.getContext();
            View viewById = convertView.findViewById(R.id.lay_my_adresses_to_favorites);

            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentUser = PreferenceUtils.getCurrentUser(context);
                    boolean placeExist = FavoritePlaceORM.isPlaceExist(context, currentUser, "TEST");
                    if (!placeExist) {
                        FavoritePlaceORM.insertFavoritePlace(context, new FavoritePlaceORM(currentUser, "TEST", "TEST address"));
                    } else {
                        Log.v(null, "PLACE EXIST");
                    }
                }
            });

        } else {
            holder = (Holder) convertView.getTag();
        }

//        holder.setData(items.get(i));

        return convertView;
    }

    public class Holder {


        public void setData(OrderORM data) {

        }
    }
}
