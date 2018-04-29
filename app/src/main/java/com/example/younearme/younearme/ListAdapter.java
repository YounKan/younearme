package com.example.younearme.younearme;

/**
 * Created by younyuan on 29-Apr-18.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<String> {

    private final Activity Context;
    private final String[] ListItemsName;
    private final Integer[] ImageName;

    public ListAdapter(Activity context, String[] content,
                       Integer[] ImageName) {

        super(context, R.layout.list_items, content);
        // TODO Auto-generated constructor stub

        this.Context = context;
        this.ListItemsName = content;
        this.ImageName = ImageName;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Context.getLayoutInflater();
        View ListViewSingle = inflater.inflate(R.layout.list_items, null, true);

        TextView ListViewItems = (TextView) ListViewSingle.findViewById(R.id.textView1);
        ImageView ListViewImage = (ImageView) ListViewSingle.findViewById(R.id.imageView1);

        ListViewItems.setText(ListItemsName[position]);
        ListViewImage.setImageResource(ImageName[position]);
        return ListViewSingle;

    };

}