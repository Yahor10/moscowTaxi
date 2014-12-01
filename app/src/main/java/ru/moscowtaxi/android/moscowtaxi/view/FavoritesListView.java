
package ru.moscowtaxi.android.moscowtaxi.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.EntityAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoritePlacesAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by ychabatarou on 04.11.2014.
 */
public class FavoritesListView extends EntityListView<FavoritePlaceORM> {


    public FavoritesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected EntityAdapter createAdapter() {
        return new FavoritePlacesAdapter(getContext()) {

            @Override
            public List<FavoritePlaceORM> loadData() {
                Context context = getContext();
                List<FavoritePlaceORM> favoritesPlaces = FavoritePlaceORM.getFavoritesPlaces(context);
                if (favoritesPlaces == null) {
                    favoritesPlaces = new ArrayList<FavoritePlaceORM>();
                }
                favoritesPlaces.add(null);//
                return favoritesPlaces;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item_favorite_place, parent, false);
                } // TODO create list item layout
                TextView name = (TextView) convertView.findViewById(R.id.name);
                name.setText("TEST");

                final FavoritePlaceORM item = getItem(position);
                return convertView;
            }
        };
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
