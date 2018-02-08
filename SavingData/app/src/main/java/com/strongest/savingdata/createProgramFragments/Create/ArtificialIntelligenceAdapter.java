package com.strongest.savingdata.createProgramFragments.Create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.ArtificialInteligence.AiActions;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/26/2017.
 */

public class ArtificialIntelligenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<AiActions.Analysis> aiActionses;
    public ArtificialIntelligenceAdapter(Context context, ArrayList<AiActions.Analysis> aiActionses){
        this.context = context;
        this.aiActionses = aiActionses;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.artificial_intelligence_chatbubble_first, parent, false);
        return new AiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AiViewHolder aivh = (AiViewHolder) holder;
        if (position != 0){
            aivh.text.setBackgroundResource(R.drawable.chat_bubble_second);
        }
        aivh.text.setText(((AiActions.Analysis)aiActionses.get(position)).getAnalysis());
    }

    @Override
    public int getItemCount() {
       // return strings.length;
        return aiActionses.size();
    }

    private class AiViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        public AiViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.ai_chatbubble_tv);
        }
    }
}
