package com.example.travis.fantasycalculator;

/**
 * Created by Travis on 2/10/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Travis on 2/10/2016.
 */
public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private Activity activity;



    public MyCustomAdapter(ArrayList<String> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity= activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.team_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.teamName);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.deleteButton);


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PlayerArray.getInstance().deleteTeam(list.get(position));
                list.remove(position);
                notifyDataSetChanged();
                ((TestDrawer) activity).updateDrawer();


            }
        });


        return view;
    }
}
