package kk.techbytecare.hvacaward;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.Model.User;

public class LogInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnLogin;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("LOGIN");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtPassword = findViewById(R.id.edt_password);
        edtPhone = findViewById(R.id.edt_phone);

        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });
    }

    private void logInUser() {

        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setTitle("USER LOG-IN");
        mDialog.setMessage("Please wait! while we check your credential!!");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        final String phone = edtPhone.getText().toString();
        final String password = edtPassword.getText().toString();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(phone).exists()) {

                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);

                    if (Boolean.parseBoolean(user.getApproved()))  {

                        if (user.getPassword().equals(password))    {
                            mDialog.dismiss();
                            Common.currentUser = user;
                            //Toast.makeText(LogInActivity.this, "Welcome....", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogInActivity.this,HomeActivity.class));
                        }
                        else    {
                            mDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Wrong Password...", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else    {
                        mDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "Application not approved!! Plz wait while it gets approved..", Toast.LENGTH_SHORT).show();
                    }

                }
                else    {
                    mDialog.dismiss();
                    Toast.makeText(LogInActivity.this, "You are not registered with us!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
