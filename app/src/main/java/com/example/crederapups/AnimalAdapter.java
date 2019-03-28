package com.example.crederapups;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class AnimalAdapter extends BaseAdapter {
    Context mContext;
    public static Animal [] animals;

    public AnimalAdapter(Context c, Animal [] a){
        mContext = c;
        animals = a;
    }

    @Override
    public int getCount() {
        return animals.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        }
        else {
            imageView = (ImageView) convertView;
        }

        if(animals[position].photos.length > 0){
            System.out.println(animals[position].name + " : " + animals[position].photos[0].full);

            Picasso.get().load(animals[position].photos[0].full).placeholder(R.drawable.no_image).resize(300, 300).centerCrop().error(R.drawable.no_image).into(imageView);
        }
        else {
            imageView = new ImageView(mContext);
        }
        return imageView;
    }
}
