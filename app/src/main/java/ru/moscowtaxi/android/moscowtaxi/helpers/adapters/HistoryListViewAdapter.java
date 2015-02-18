package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;
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
    public OrderHistoryORM getItem(int i) {
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

        holder.txtIdTaxi = (TextView) convertView.findViewById(R.id.txt_number_order);
        holder.txtFrom = (TextView) convertView.findViewById(R.id.txt_from_here);
        holder.txtWhere = (TextView) convertView.findViewById(R.id.txt_where);

        holder.setData(items.get(i));

        return convertView;
    }

    public class Holder {
        TextView txtIdTaxi;
        TextView txtFrom;
        TextView txtWhere;

        public void setData(OrderHistoryORM history) {
            if (!"".equals(history.number))
                txtIdTaxi.setText("" + history.number);

            if (!"".equals(history.addressFrom))
                txtFrom.setText(history.addressFrom);
            if (!"".equals(history.addressTo))
                txtWhere.setText(history.addressTo);

        }
    }
}
