package com.example.version5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static com.example.version5.MainActivity.EXTRA_MESSAGE;

public class ListViewAdapter extends ArrayAdapter<ListItem> {
    // record xml id
    private int resourceId;
    // record adapter activity
    private Activity activity;
    final WriteTxtofPosition writeData = new WriteTxtofPosition();

    public ListViewAdapter(Context context, int resourceId1, List<ListItem> objects, Activity activity1) {
        super(context,resourceId1,objects);
        resourceId=resourceId1;
        activity = activity1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ListItem listitem =getItem(position);
        final View view;
        final ViewHolder viewHolder;
        // for first read
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)view.findViewById(R.id.image);
            viewHolder.itemTitle=(TextView)view.findViewById(R.id.itemTitle);
            viewHolder.itemText=(TextView)view.findViewById(R.id.itemText);
            viewHolder.button=(Button)view.findViewById(R.id.call);
            viewHolder.button2=(Button)view.findViewById(R.id.rate);
            //set data in cache
            view.setTag(viewHolder);
        }else{
            //read the cache for the second read
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.imageView.setImageResource(listitem.getImageId());
        viewHolder.itemTitle.setText(listitem.getItemTitle());
        viewHolder.itemText.setText(listitem.getItemText());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String action = "android.intent.action.DIAL";
                action = Intent.ACTION_DIAL;
                Intent intent = new Intent(action);
                // set data 
                intent.setData(Uri.parse("tel:" + listitem.getItemText())); //<data android:scheme="tel" />
                // startActivity(intent)
                activity.startActivity(intent);
            }
        });

        //create a new thread in onClicklistener because cannot write and read file simultaneously.
        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        //ListItem listItem = lists.get(position);
                        writeData.writeData("" + position, activity);

                        //new thread communicate with UI thread through view.post method
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(activity, DisplayActivity.class);
                                String message = position + "+" + listitem.getItemTitle() + "+" + listitem.getItemText();
                                //add value to the Intent
                                intent.putExtra(EXTRA_MESSAGE, message);
                                activity.startActivity(intent);
                            }
                        });
                        //update adapter then jump to the selected row.
                        //listViewAdapter.notifyDataSetChanged();
                        //listView.setSelection(1);
                    }
                }).start();
            }
        });
        return view;
    }
    class ViewHolder{
        private ImageView imageView;
        private TextView itemTitle;
        private TextView itemText;
        private Button button;
        private Button button2;
    }
}
