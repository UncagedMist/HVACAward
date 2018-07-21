package kk.techbytecare.hvacaward.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.HomeActivity;
import kk.techbytecare.hvacaward.Model.UserDetails;
import kk.techbytecare.hvacaward.R;
import kk.techbytecare.hvacaward.RegisterActivity;

public class ProfileFragment extends Fragment {

    View myFragment;

    CheckBox ckb_window,ckb_split,ckb_duet,ckb_chiller,ckb_vrv,ckb_install,ckb_service;;
    ElegantNumberButton txt_count;
    EditText edt_holder,edt_account,edt_ifsc,edt_bank_name,edt_bank_branch;
    Button btn_update_profile;
    TextView txt_name;
    TextView txt_refer;
    CircleImageView img_profile;

    FirebaseDatabase database;
    DatabaseReference userDetail,users;
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri selectedFileUri;

    String refer = "HVAC1234";

    private int PICK_FILE_REQUEST_CODE = 5152;

    public ProfileFragment() {

        database = FirebaseDatabase.getInstance();
        userDetail = database.getReference("UserDetails");
        users = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public static ProfileFragment newInstance()    {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        ckb_window = myFragment.findViewById(R.id.ckb_window);
        ckb_split = myFragment.findViewById(R.id.ckb_split);
        ckb_duet = myFragment.findViewById(R.id.ckb_duet);
        ckb_chiller = myFragment.findViewById(R.id.ckb_chiller);
        ckb_vrv = myFragment.findViewById(R.id.ckb_vrv);
        ckb_install = myFragment.findViewById(R.id.ckb_install);
        ckb_service = myFragment.findViewById(R.id.ckb_service);

        txt_count = myFragment.findViewById(R.id.txt_count);
        txt_refer = myFragment.findViewById(R.id.txt_refer);

        img_profile = myFragment.findViewById(R.id.img_profile);

        edt_holder = myFragment.findViewById(R.id.edt_holder);
        edt_account = myFragment.findViewById(R.id.edt_account);
        edt_ifsc = myFragment.findViewById(R.id.edt_ifsc);
        edt_bank_name = myFragment.findViewById(R.id.edt_bank_name);
        edt_bank_branch = myFragment.findViewById(R.id.edt_bank_branch);

        txt_name = myFragment.findViewById(R.id.txt_name);

        btn_update_profile = myFragment.findViewById(R.id.btn_update_profile);

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailsToServer();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        loadAllDetails();

        return myFragment;
    }

    private void chooseImage() {
        Intent intent = Intent.createChooser(FileUtils.createGetContentIntent(),"Select a file");
        startActivityForResult(intent,PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)   {

            if (requestCode == PICK_FILE_REQUEST_CODE)  {

                if (data != null)   {

                    selectedFileUri = data.getData();

                    if (selectedFileUri != null && !selectedFileUri.getPath().isEmpty())    {
                        img_profile.setImageURI(selectedFileUri);
                        uploadFile();
                    }
                    else {
                        Toast.makeText(getActivity(), "Can't Upload image to server", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    private void uploadFile() {

        //final String referCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

        if (selectedFileUri != null)    {
            final ProgressDialog mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("profile_images/"+imageName);
            imageFolder.putFile(selectedFileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded!!!", Toast.LENGTH_SHORT).show();

                            Map<String,Object> imgData = new HashMap<>();
                            imgData.put("profileImage",taskSnapshot.getDownloadUrl().toString());
                            imgData.put("referCode",refer);

                            users.child(Common.currentUser.getPhone()).updateChildren(imgData);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+" %");
                        }
                    });
        }
    }

    private void loadAllDetails() {
        txt_name.setText(Common.currentUser.getName());

        userDetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {

                    UserDetails userDetails = dataSnapshot.child(Common.currentUser.getPhone()).getValue(UserDetails.class);

                    ckb_window.setChecked(userDetails.isWindow());
                    ckb_split.setChecked(userDetails.isSplit());
                    ckb_duet.setChecked(userDetails.isDuet());
                    ckb_chiller.setChecked(userDetails.isChiller());
                    ckb_vrv.setChecked(userDetails.isVRV());
                    ckb_install.setChecked(userDetails.isInstall());
                    ckb_service.setChecked(userDetails.isService());
                    txt_count.setNumber(userDetails.getCount());
                    edt_holder.setText(userDetails.getAccountHolder());
                    edt_account.setText(userDetails.getAccountNo());
                    edt_ifsc.setText(userDetails.getIFSC());
                    edt_bank_name.setText(userDetails.getBankName());
                    edt_bank_branch.setText(userDetails.getBankBranch());

                    if (!TextUtils.isEmpty(Common.currentUser.getProfileImage())) {

                        Picasso.with(getContext())
                                .load(Common.currentUser.getProfileImage())
                                .into(img_profile);

                        if (!TextUtils.isEmpty(Common.currentUser.getReferCode()))   {
                            txt_refer.setText(new StringBuilder("Refer Code : ")+refer);
                        }
                        else    {
                            //
                        }
                    }
                    else    {
                        try {
                            img_profile.setImageDrawable(getContext().getResources().getDrawable(R.drawable.default_avatar));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                else    {
                    //Toast.makeText(getActivity(), "Plz Update all the details..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendDetailsToServer() {

        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please wait! while we Update Your Details");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        userDetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDialog.dismiss();

                UserDetails userDetails = new UserDetails(
                        Common.currentUser.getPhone(),
                        ckb_window.isChecked(),
                        ckb_split.isChecked(),
                        ckb_duet.isChecked(),
                        ckb_chiller.isChecked(),
                        ckb_vrv.isChecked(),
                        ckb_install.isChecked(),
                        ckb_service.isChecked(),
                        txt_count.getNumber(),
                        edt_holder.getText().toString(),
                        edt_account.getText().toString(),
                        edt_ifsc.getText().toString(),
                        edt_bank_name.getText().toString(),
                        edt_bank_branch.getText().toString());

                try {
                    userDetail.child(Common.currentUser.getPhone()).setValue(userDetails);
                    Toast.makeText(getActivity(), "Updated Successfully...", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }

}
