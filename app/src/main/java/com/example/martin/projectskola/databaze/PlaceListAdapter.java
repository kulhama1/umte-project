package com.example.martin.projectskola.databaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.projectskola.R;

import java.io.File;
import java.util.ArrayList;

public class PlaceListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Place> placeList;

    public PlaceListAdapter(Context context, int layout, ArrayList<Place> placeList) {
        this.context = context;
        this.layout = layout;
        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtTitle, txtDescription;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtTitle = (TextView) row.findViewById(R.id.textViewTitleIcon);
            holder.txtDescription = (TextView) row.findViewById(R.id.textViewDescriptionIcon);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewIkonkaFotky);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Place place = placeList.get(position);

        holder.txtTitle.setText(place.getTitle());
        holder.txtDescription.setText(place.getDescription());
        File file = new File(place.getCesta());
        Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {   final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
             inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

         options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}

