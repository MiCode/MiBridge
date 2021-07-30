package com.mi.testmibridge;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MiBridgeAdapter extends RecyclerView.Adapter<MiBridgeAdapter.ViewHolder> {

    private List<MiBridgeModel> mMiBridgeModels = new ArrayList<>();

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            MiBridgeModel model = mMiBridgeModels.get(index);
            model.getRunnable().run();
        }
    };

    public MiBridgeAdapter(List<MiBridgeModel> models) {
        super();
        if (null != models) {
            mMiBridgeModels.clear();
            mMiBridgeModels.addAll(models);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bridge_item_layout, (ViewGroup) viewGroup.getParent(), false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MiBridgeModel miBridgeModel = mMiBridgeModels.get(i);
        viewHolder.title.setText(miBridgeModel.getName());
        viewHolder.title.setOnClickListener(listener);
        viewHolder.title.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mMiBridgeModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Button title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

}
