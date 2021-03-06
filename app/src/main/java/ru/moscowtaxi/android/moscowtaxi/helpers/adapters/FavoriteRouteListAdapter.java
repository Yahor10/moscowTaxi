package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoriteRouteORM;

/**
 * Created by alex-pers on 12/13/14.
 */
public class FavoriteRouteListAdapter extends BaseAdapter {

    List<FavoriteRouteORM> items;
    LayoutInflater inflater;

    public FavoriteRouteListAdapter(LayoutInflater inflater, List<FavoriteRouteORM> data) {
        this.inflater = inflater;
        items = data;
    }

    public List<FavoriteRouteORM> getItems(){
        return items;
    }

    public  void setItems(List<FavoriteRouteORM> items){
        this.items = items;
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
                    .inflate(R.layout.list_item_favorite_route, null);
            convertView.setTag(holder);



        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.view_but_delete = convertView.findViewById(R.id.view_but_delete);

        holder.setData(items.get(i));


        return convertView;
    }

    public class Holder {

        public View view_but_delete;


        public void setData(final FavoriteRouteORM data) {
            view_but_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    getItems().remove(data);
                    notifyDataSetChanged();
                    FavoriteRouteORM.deleteFavoriteRouteByID(context, data.getId());
                }
            });

        }
    }
}

