package kk.techbytecare.hvacaward;

import android.app.ProgressDialog;
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

import kk.techbytecare.hvacaward.Model.User;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName,edtAddress,edtPhone,edtEmail,edtPassword,edt_cnf_password,edtPromo;
    Button btnRegister;

    FirebaseDatabase database;
    DatabaseReference users;

    String refer = "HVAC1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("REGISTER");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edt_cnf_password = findViewById(R.id.edt_cnf_password);
        edtPromo = findViewById(R.id.edt_promo);

        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equals(edt_cnf_password.getText().toString())) {
                    registerUser();
                }
                else    {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser() {

        final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
        mDialog.setTitle("USER SIGN-UP");
        mDialog.setMessage("Please wait! while we Register Your Account!!");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDialog.dismiss();

                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                    Toast.makeText(RegisterActivity.this, "User Already Registered!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (edtPromo.getText().toString().equals(refer) || edtPromo.getText().toString().equals(""))    {
                        User user = new User(
                                edtName.getText().toString(),
                                edtAddress.getText().toString(),
                                edtEmail.getText().toString(),
                                edtPhone.getText().toString(),
                                edtPassword.getText().toString());

                        users.child(edtPhone.getText().toString()).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Application Submitted & in review", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else    {
                        Toast.makeText(RegisterActivity.this, "Promo code not exist Not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
