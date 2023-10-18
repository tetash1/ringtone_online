package com.islamialib.islamic_ringtone.serverside;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.islamialib.islamic_ringtone.ListRingTone;
import com.islamialib.islamic_ringtone.R;

import java.util.List;

public class VideoAdapterServer extends RecyclerView.Adapter<VideoAdapterServer.ViewHolder> {
    private List<ConstructorsGeterSeter> dataList;
    private Context context;

    // Constructor
    public VideoAdapterServer(List<ConstructorsGeterSeter> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistNameTextView;
        public TextView playlistLinkTextView;
        public TextView usernameTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            playlistNameTextView = itemView.findViewById(R.id.playlistNameTextView);
            playlistLinkTextView = itemView.findViewById(R.id.playlistLinkTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ConstructorsGeterSeter data = dataList.get(position);

        holder.playlistNameTextView.setText(data.getPlaylistName());
        holder.playlistLinkTextView.setText(data.getPlaylistLink());
        holder.usernameTextView.setText(data.getUsername());

        // Load the image using an image loading library like Picasso or Glide
        // Example using Picasso:
/*
        Picasso.get().load(data.getImageUrl()).into(holder.imageView);
*/

        // Set an item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here (e.g., open details activity)
                // You can use dataList.get(position) to get the clicked item's data


                Intent intent = new Intent(context, ListRingTone.class);
                intent.putExtra("ringtoneName", data.getPlaylistName());
/*
                intent.putExtra("playlistLink", data.getPlaylistLink());
*/
                intent.putExtra("ringtoneLink", data.getPlaylistLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
