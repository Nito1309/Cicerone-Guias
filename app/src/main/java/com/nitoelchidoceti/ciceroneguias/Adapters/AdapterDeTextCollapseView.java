package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nitoelchidoceti.ciceroneguias.R;

import java.util.ArrayList;

public class AdapterDeTextCollapseView extends BaseExpandableListAdapter {

    ArrayList<String> titulos, descripciones;
    Context context;

    public AdapterDeTextCollapseView(ArrayList<String> titulos, ArrayList<String> descripciones, Context context) {
        this.titulos = titulos;
        this.descripciones = descripciones;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return titulos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return descripciones.size() - 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titulos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return descripciones.get(groupPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String titleFaq = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faqs_title, null);
        }
        TextView txtTitleFaq = convertView.findViewById(R.id.txtFaqsTitle);
        txtTitleFaq.setTypeface(null, Typeface.BOLD);
        txtTitleFaq.setText(titleFaq);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String descriptionFaq = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faqs_description, null);
        }
        TextView txtDescriptionFaq = convertView.findViewById(R.id.txtDescriptionFaq);
        txtDescriptionFaq.setText(descriptionFaq);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
