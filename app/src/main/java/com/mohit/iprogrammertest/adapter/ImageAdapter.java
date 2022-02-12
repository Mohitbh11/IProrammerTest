package com.mohit.iprogrammertest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mohit.iprogrammertest.R;
import com.mohit.iprogrammertest.activity.MainActivity;
import com.mohit.iprogrammertest.manager.DatabaseInstance;
import com.mohit.iprogrammertest.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    Context context;
    List<ImageModel> imageModels;
    List<ImageModel> imageInDatabase;
    DatabaseInstance databaseInstance;
    ListView listView;

    public ImageAdapter(Context context, List<ImageModel> imageModels, List<ImageModel> imageInDatabase, ListView listView) {
        this.context = context;
        this.imageModels = imageModels;
        this.imageInDatabase = imageInDatabase;
        this.listView = listView;
    }

    @NonNull
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_image_details_row, parent, false);
        databaseInstance = new DatabaseInstance(context);
        return new ImageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.MyViewHolder holder, int position) {

        ImageModel model = imageModels.get(position);
        holder.title.setText(model.getTitle());
        holder.id.setText("ID: " + model.getId());
        holder.url.setText(model.getUrl());
        try{
            Picasso.get().load(model.getThumbnailUrl()+"/").into(holder.imageView);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        boolean inDatabase = false;
        for(int i = 0; i < imageInDatabase.size(); i++){
            if(imageInDatabase.get(i).getId().equals(model.getId())){
                inDatabase = true;
                updateComparisonTable(imageInDatabase);
            }
        }
        if(inDatabase){
            holder.button.setText("Remove");
            holder.button.setBackgroundColor(context.getResources().getColor(R.color.red_alert));

        }
        else{
            holder.button.setText("Compare");
            holder.button.setBackgroundColor(context.getResources().getColor(R.color.app_color));
        }
        boolean finalInDatabase = inDatabase;
        holder.button.setOnClickListener(v -> {
            if(holder.button.getText().equals("Compare")){
                databaseInstance.insertImages(model.getId(), model.getAlbumId(), model.getTitle(), model.getUrl(), model.getThumbnailUrl());
                holder.button.setText("Remove");
                if(!finalInDatabase){
                    imageInDatabase.add(model);
                }
                holder.button.setBackgroundColor(context.getResources().getColor(R.color.red_alert));
                updateComparisonTable(imageInDatabase);
            }
            else{
                databaseInstance.deleteImageData(model.getId());
                holder.button.setText("Compare");
                imageInDatabase.remove(model);

                holder.button.setBackgroundColor(context.getResources().getColor(R.color.app_color));
                updateComparisonTable(imageInDatabase);
            }

        });

    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, id, url;
        MaterialButton button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            id = itemView.findViewById(R.id.text_view_id);
            url = itemView.findViewById(R.id.text_view_url);
            imageView = itemView.findViewById(R.id.image);
            button = itemView.findViewById(R.id.btn_comapre_remove);

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    void updateComparisonTable(List<ImageModel> imageInDB){
        CompareTableAdapter compareTableAdapter = new CompareTableAdapter(context, imageInDB);
        listView.setAdapter(compareTableAdapter);
        compareTableAdapter.notifyDataSetChanged();
    }

}
