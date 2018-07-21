package kk.techbytecare.hvacaward.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import kk.techbytecare.hvacaward.R;

public class RatingViewHolder extends RecyclerView.ViewHolder {

    public TextView txtUserPhone,txtComment;
    public RatingBar ratingBar;

    public RatingViewHolder(View itemView) {
        super(itemView);

        txtUserPhone = itemView.findViewById(R.id.txtUserPhone);
        txtComment = itemView.findViewById(R.id.txtComment);

        ratingBar = itemView.findViewById(R.id.ratingBar);
    }
}
