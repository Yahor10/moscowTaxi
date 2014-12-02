
package ru.moscowtaxi.android.moscowtaxi.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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

        Button address = new Button(context);
        LinearLayout footer = new LinearLayout(context);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        footer.setLayoutParams(linLayoutParam);
        footer.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 35, 0, 0);

        String addFavorite = context.getString(R.string.add_favorites_place);
        address.setText(addFavorite);
        address.setLayoutParams(layoutParams);

        footer.addView(address);
        addFooterView(footer);
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
                favoritesPlaces.add(new FavoritePlaceORM());
                favoritesPlaces.add(new FavoritePlaceORM());
                favoritesPlaces.add(new FavoritePlaceORM());


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
