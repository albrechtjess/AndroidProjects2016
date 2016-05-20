package net.albrechtjess.albrechtjesslab3;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jess on 4/15/2016.
 */
public class MyListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private Activity act;
    private ArrayList<Manufacturer> manufacturers;
    public MyListAdapter(Activity act, ArrayList<Manufacturer> manufacturers){
        this.act = act;
        this.manufacturers = manufacturers;
    }

    @Override
    public int getGroupCount() {
        return this.manufacturers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.manufacturers.get(groupPosition).models.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.manufacturers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.manufacturers.get(groupPosition).getModelNameatPosition(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(R.layout.group_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.groupTextView);
        textView.setText(this.manufacturers.get(groupPosition).getName());
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView =  act.getLayoutInflater().inflate(R.layout.child_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.childTextView);
        textView.setText(this.manufacturers.get(groupPosition).getModelNameatPosition(childPosition));
        ImageView deleteIcon = (ImageView) convertView.findViewById(R.id.childImageView);
        deleteIcon.setTag(R.id.group_pos, groupPosition);
        deleteIcon.setTag(R.id.child_pos, childPosition);
        deleteIcon.setOnClickListener(this);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
        final int groupPos = (int)v.getTag(R.id.group_pos);
        final int childPos = (int)v.getTag(R.id.child_pos);
        Snackbar.make(v,
                "Confirm delete of " + manufacturers.get(groupPos).getModelNameatPosition(childPos),
                Snackbar.LENGTH_LONG).setAction("CONFIRM", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manufacturers.get(groupPos).deleteModel(childPos);
                        notifyDataSetChanged();
                    }
        }).show();
    }
}
