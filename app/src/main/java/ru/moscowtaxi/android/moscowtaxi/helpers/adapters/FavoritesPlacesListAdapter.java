package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by alex-pers on 12/15/14.
 */
public class FavoritesPlacesListAdapter extends BaseAdapter {

    List<FavoritePlaceORM> items;
    LayoutInflater inflater;

    public FavoritesPlacesListAdapter(LayoutInflater inflater, List<FavoritePlaceORM> data) {
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
                    .inflate(R.layout.list_item_favorite_place, null);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.linStateNormal = (LinearLayout) convertView.findViewById(R.id.lay_state_normal);
        holder.linStateEdited = (LinearLayout) convertView.findViewById(R.id.lay_state_edited);
        holder.txtName = (TextView) convertView.findViewById(R.id.name);
        holder.txtAdress = (TextView) convertView.findViewById(R.id.address);
        holder.edtName = (EditText) convertView.findViewById(R.id.nameEdit);
        holder.edtAdress = (EditText) convertView.findViewById(R.id.addressEdit);
        holder.butChange = (LinearLayout) convertView.findViewById(R.id.editLayout);
        holder.butDelete = (LinearLayout) convertView.findViewById(R.id.removeLayout);
        holder.butDetectAdress = (LinearLayout) convertView.findViewById(R.id.detectLayout);
        holder.butSave = (LinearLayout) convertView.findViewById(R.id.saveLayout);

        holder.setData(items.get(i));

        return convertView;
    }

    public class Holder {
        LinearLayout linStateNormal;
        LinearLayout linStateEdited;
        TextView txtName;
        TextView txtAdress;
        EditText edtName;
        EditText edtAdress;
        LinearLayout butChange;
        LinearLayout butDelete;
        LinearLayout butDetectAdress;
        LinearLayout butSave;


        public void setData(final FavoritePlaceORM data) {

            if(data.is_edited_now){
                linStateEdited.setVisibility(View.VISIBLE);
                linStateNormal.setVisibility(View.GONE);
            }else{
                linStateEdited.setVisibility(View.GONE);
                linStateNormal.setVisibility(View.VISIBLE);
            }

            txtName.setText(data.name);
            txtAdress.setText(data.address);
            edtName.setText(data.name);
            edtAdress.setText(data.address);
            butChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"CHANGE",Toast.LENGTH_SHORT).show();

                    for ( FavoritePlaceORM item : items){
                        item.is_edited_now = false;
                    }
                    data.is_edited_now = true;
                    FavoritesPlacesListAdapter.this.notifyDataSetChanged();
                }
            });

            butSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"SAVE",Toast.LENGTH_SHORT).show();
                    data.is_edited_now = false;
                    FavoritesPlacesListAdapter.this.notifyDataSetChanged();
                }
            });

        }
    }
}
