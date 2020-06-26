package com.example.eldadzipori.atidaprovals;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eldadzipori on 3/8/16.
 */
public class teachersCorrectionAdapter extends ArrayAdapter<aprovedUser> {

    Context context;
    List<aprovedUser> list;
    ArrayList<aprovedUser> userListClone;
    List<aprovedUser> suggestions;
    public teachersCorrectionAdapter(Context context, int resource, ArrayList<aprovedUser> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        userListClone = (ArrayList<aprovedUser>)objects.clone();
        suggestions = new ArrayList<>();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.autocorrect_layout,parent,false);

        TextView tx = (TextView)v.findViewById(R.id.txtNames);
        tx.setText(list.get(position).getFullName());

        return v;

    }
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((aprovedUser)(resultValue)).getFullName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (aprovedUser customer : userListClone) {
                    try {
                        customer.fetch();
                    }
                    catch (ParseException e){}
                    String name= customer.getFullName();
                    if(name.toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<aprovedUser> filteredList = (ArrayList<aprovedUser>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (aprovedUser c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
