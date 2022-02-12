package com.mohit.iprogrammertest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohit.iprogrammertest.R;
import com.mohit.iprogrammertest.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompareTableAdapter extends BaseAdapter {
    Context context;
    List<ImageModel> imageModelList;

    public CompareTableAdapter(Context context, List<ImageModel> imageModelList) {
        this.context = context;
        this.imageModelList = imageModelList;
    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CompareTableAdapter.CompareTableHolder compareTableHolder ;
        if(view == null){
            LayoutInflater recordInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.comparison_table_row_layout, null);
            compareTableHolder = new CompareTableAdapter.CompareTableHolder();
            compareTableHolder.photo = view.findViewById(R.id.iv_photo);
            compareTableHolder.id = (TextView) view.findViewById(R.id.tv_id);
            compareTableHolder.url = (TextView) view.findViewById(R.id.tv_url);
            compareTableHolder.title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(compareTableHolder);
        }else {
            compareTableHolder = (CompareTableAdapter.CompareTableHolder) view.getTag();
        }

        ImageModel model = imageModelList.get(i);
        compareTableHolder.title.setText(model.getTitle());
        compareTableHolder.id.setText(model.getId());
        compareTableHolder.url.setText(model.getUrl());
        try{
            Picasso.get().load(model.getThumbnailUrl()+"/").into(compareTableHolder.photo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private static class CompareTableHolder {

        public ImageView photo;
        public TextView id;
        public TextView url;
        public TextView title;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
