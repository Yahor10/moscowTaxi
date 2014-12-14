package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;
import ru.moscowtaxi.android.moscowtaxi.orm.RewardsORM;

/**
 * Created by alex-pers on 12/14/14.
 */
public class RewardListAdapter extends BaseAdapter {

    ArrayList<RewardsORM> items;
    LayoutInflater inflater;

    public RewardListAdapter(LayoutInflater inflater, ArrayList<RewardsORM> data) {
        this.inflater = inflater;
        items = data;
    }


    @Override
    public int getCount() {
        if (items == null)
            return 0;
        return 4;
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
                    .inflate(R.layout.list_item_rewards, null);
            convertView.setTag(holder);



        } else {
            holder = (Holder) convertView.getTag();
        }

//        holder.setData(items.get(i));

        return convertView;
    }

    public class Holder {


        public void setData(RewardsORM data) {

        }
    }
}

