package kk.techbytecare.hvacaward;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.Model.Rating;
import kk.techbytecare.hvacaward.ViewHolder.RatingViewHolder;

public class FeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratings;

    FirebaseRecyclerAdapter<Rating,RatingViewHolder> adapter;

    String projectId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        database = FirebaseDatabase.getInstance();
        ratings = database.getReference("Ratings");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Project Feedback");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_feedback);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)    {
            projectId = getIntent().getStringExtra(Common.INTENT_PROJECT_ID);
        }
        if (!projectId.isEmpty() && projectId != null) {
            Query query = ratings.orderByChild("projectId").equalTo(projectId);

            FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                    .setQuery(query, Rating.class).build();

            adapter = new FirebaseRecyclerAdapter<Rating, RatingViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RatingViewHolder holder, int position, @NonNull Rating model) {
                    holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                    holder.txtComment.setText(model.getComment());
                    holder.txtUserPhone.setText(Common.currentUser.getName());
                }

                @NonNull
                @Override
                public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.show_comment_layout,parent,false);
                    return new RatingViewHolder(view);
                }
            };
            loadComment(projectId);
        }
    }

    private void loadComment(String projectId) {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
