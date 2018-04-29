package com.example.younearme.younearme;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, ArrayList<String> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_adapter, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.logo = (ImageView) convertView.findViewById(R.id.logo);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int imgResourceId = 0;
        switch (position) {
            case 1:
                imgResourceId = R.drawable.america;
                break;
            case 2:
                imgResourceId = R.drawable.argenitna;
                break;
            case 3:
                imgResourceId = R.drawable.australia;
                break;
            case 4:
                imgResourceId = R.drawable.brazil;
                break;
            case 5:
                imgResourceId = R.drawable.canada;
                break;
            case 6:
                imgResourceId = R.drawable.egypt;
                break;
            case 7:
                imgResourceId = R.drawable.france;
                break;
            case 8:
                imgResourceId = R.drawable.german;
                break;
            case 9:
                imgResourceId = R.drawable.hongkong;
                break;
            case 10:
                imgResourceId = R.drawable.italy;
                break;
            case 11:
                imgResourceId = R.drawable.japan;
                break;
            case 12:
                imgResourceId = R.drawable.mexico;
                break;
            case 13:
                imgResourceId = R.drawable.newzealand;
                break;
            case 14:
                imgResourceId = R.drawable.southafrica;
                break;
            case 15:
                imgResourceId = R.drawable.southkorea;
                break;
            case 16:
                imgResourceId = R.drawable.uk;
                break;

            case 17:
                imgResourceId = R.drawable.thailand;
                break;
        }

        holder.name.setText(mData.get(position));
        holder.logo.setImageResource(imgResourceId);

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageView logo;
    }
}