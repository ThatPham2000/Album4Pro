package com.example.album4pro.searching;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.album4pro.R;

import java.util.ArrayList;

public class SearchLVAdapter extends RecyclerView.Adapter<SearchLVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SearchLVModel> searchLVModels;

    public SearchLVAdapter(Context context, ArrayList<SearchLVModel> searchLVModels) {
        this.context = context;
        this.searchLVModels = searchLVModels;
    }

    @NonNull
    @Override
    public SearchLVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLVAdapter.ViewHolder holder, int position) {
        SearchLVModel searchLVModel = searchLVModels.get(position);
        holder.titleTv.setText(searchLVModel.getTitle());
        holder.snippetTV.setText(searchLVModel.getLink());
        holder.descriptionTV.setText(searchLVModel.getSnippet());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(searchLVModel.getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchLVModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv, snippetTV, descriptionTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.idTVTitle);
            snippetTV = (TextView) itemView.findViewById(R.id.idTVSnippet);
            descriptionTV = (TextView) itemView.findViewById(R.id.idTVDescription);
        }
    }
}

