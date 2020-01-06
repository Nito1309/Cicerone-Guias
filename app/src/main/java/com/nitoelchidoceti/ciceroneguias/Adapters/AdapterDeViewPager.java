package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterDeViewPager extends PagerAdapter {
    private Context context;
    private ArrayList<String> imagenes;

    public AdapterDeViewPager(Context context, ArrayList<String> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        Glide.with(context).load(imagenes.get(position)).into(imageView);
        container.addView(imageView,0);//agrega la imagen al view pager
        return imageView;//lo regresa al metodo de arriba de isViewFormObject
    }
}
