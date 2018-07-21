package kk.techbytecare.hvacaward.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kk.techbytecare.hvacaward.Interface.ItemClickListener;
import kk.techbytecare.hvacaward.R;

public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView project_name;
    public ImageView project_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProjectViewHolder(View itemView) {
        super(itemView);

        project_name = itemView.findViewById(R.id.name);
        project_image = itemView.findViewById(R.id.image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}
