package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Database.Articles.ArticleObj;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/13/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ArticleObj> texts;
    private RecyclerViewClickListener listener;
    private Context context;

    public ArticleAdapter(Context context, ArrayList<ArticleObj> texts, RecyclerViewClickListener listener) {
        this.context = context;

        this.texts = texts;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(context).inflate(R.layout.article_view, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.title.setText(texts.get(position).getTitle());
        vh.summary.setText(texts.get(position).getSummary());
        vh.image.setImageBitmap(texts.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public void setTexts(ArrayList<ArticleObj> texts) {
        this.texts = texts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, summary;
        public ImageView image;
        public Button read;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
            summary = (TextView) itemView.findViewById(R.id.article_summary);
            image = (ImageView) itemView.findViewById(R.id.article_image);
            read = (Button) itemView.findViewById(R.id.article_read_button);
            read.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v, this.getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {

        void recyclerViewListClicked(View v, int position);
    }

}
