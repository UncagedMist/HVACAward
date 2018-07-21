package kk.techbytecare.hvacaward;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.Model.Project;
import kk.techbytecare.hvacaward.Model.Rating;

public class DetailActivity extends AppCompatActivity implements RatingDialogListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnRating;
    RatingBar ratingBar;
    Button btnFeedback;

    TextView project_name,project_description,txt_uploader,txt_area,txt_city,txt_address;
    ImageView img_project;

    String projectId = "";

    Project currentProject;

    FirebaseDatabase database;
    DatabaseReference ratings,projects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        database = FirebaseDatabase.getInstance();
        ratings = database.getReference("Ratings");
        projects = database.getReference("Projects");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnRating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar);

        project_name = findViewById(R.id.project_name);
        project_description = findViewById(R.id.project_description);

        txt_address = findViewById(R.id.txt_address);
        txt_area = findViewById(R.id.txt_area);
        txt_city = findViewById(R.id.txt_city);
        txt_uploader = findViewById(R.id.project_uploader);

        img_project = findViewById(R.id.img_project);

        collapsingToolbarLayout = findViewById(R.id.collapsing);

        btnFeedback = findViewById(R.id.btnShowFeedback);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,FeedbackActivity.class);
                intent.putExtra(Common.INTENT_PROJECT_ID,projectId);
                startActivity(intent);
            }
        });

        if (getIntent() != null)    {
            projectId = getIntent().getStringExtra("ProjectId");
        }

        if (!projectId.isEmpty())  {
            getDetailProject(projectId);
            getRatingProject(projectId);
        }

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });
    }

    private void getDetailProject(final String projectId) {
        projects.child(projectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProject = dataSnapshot.getValue(Project.class);

                //set image
                Picasso.with(getBaseContext())
                        .load(currentProject.getImage())
                        .into(img_project);

                collapsingToolbarLayout.setTitle(currentProject.getName());

                project_name.setText(currentProject.getName());
                txt_uploader.setText(Common.currentUser.getName());
                txt_address.setText(currentProject.getAddress());
                txt_area.setText(currentProject.getArea());
                txt_city.setText(currentProject.getCity());
                project_description.setText(currentProject.getDescription());

                project_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailActivity.this,WebActivity.class);
                        intent.putExtra(Common.INTENT_PROJECT_ID,projectId);
                        Common.currentProject = currentProject;
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite OK","Very Good","Awesome Project"))
                .setDefaultRating(1)
                .setTitle("Rate this Project")
                .setDescription("Please Select Some Stars and Give Your FeedBack")
                .setTitleTextColor(R.color.colorComment)
                .setDescriptionTextColor(R.color.colorComment)
                .setHint("Please Write Your FeedBack Here")
                .setHintTextColor(R.color.colorCommentAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorCommentDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(DetailActivity.this)
                .show();
    }

    private void getRatingProject(String projectId) {

        Query projectRating = ratings.orderByChild("projectId").equalTo(projectId);

        projectRating.addValueEventListener(new ValueEventListener() {
            int count = 0,sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())    {

                    Rating item= postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        Rating rating = new Rating(
                Common.currentUser.getPhone(),
                projectId,
                String.valueOf(value),
                comments);

        ratings.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailActivity.this, "Thank You For Your FeedBack !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
