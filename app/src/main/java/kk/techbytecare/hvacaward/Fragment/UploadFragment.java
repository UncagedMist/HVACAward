package kk.techbytecare.hvacaward.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.Model.CategoryItem;
import kk.techbytecare.hvacaward.Model.Project;
import kk.techbytecare.hvacaward.Model.ProjectItem;
import kk.techbytecare.hvacaward.R;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {

    View myFragment;

    Button btn_browse,btn_upload;
    ImageView image_preview;
    MaterialSpinner spinner;
    EditText edt_name,edt_description,edt_address,edt_area,edt_city;

    String categoryIdSelected = "",nameOfFile = "";
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    Map<String,String> spinnerData = new HashMap<>();


    public UploadFragment() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static UploadFragment newInstance()    {
        UploadFragment uploadFragment = new UploadFragment();
        return uploadFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_upload, container, false);

        btn_browse = myFragment.findViewById(R.id.btn_browse);
        btn_upload = myFragment.findViewById(R.id.btn_upload);
        image_preview = myFragment.findViewById(R.id.image_preview);
        spinner = myFragment.findViewById(R.id.spinner);

        edt_name = myFragment.findViewById(R.id.edt_name);
        edt_description = myFragment.findViewById(R.id.edt_description);
        edt_address = myFragment.findViewById(R.id.edt_address);
        edt_city = myFragment.findViewById(R.id.edt_city);
        edt_area = myFragment.findViewById(R.id.edt_area);

        loadCategorySpinner();

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedIndex() == 0)    {
                    Toast.makeText(getActivity(), "Please choose category first..", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPicture();
                }
            }
        });

        return myFragment;
    }

    private void uploadPicture() {
        if (filePath != null)   {

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            nameOfFile = UUID.randomUUID().toString();
            final StorageReference ref = storageReference.child(new StringBuilder("project_images/").append(nameOfFile).toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            String name = edt_name.getText().toString();
                            String description = edt_description.getText().toString();
                            String address = edt_address.getText().toString();
                            String city = edt_city.getText().toString();
                            String area = edt_area.getText().toString();

                            saveUriToCategory(name,description,address,city,area,categoryIdSelected,taskSnapshot.getDownloadUrl().toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading.."+(int)progress+"%");
                        }
                    });
        }
    }

    private void saveUriToCategory(String name, String description, String address, String city, String area, String categoryIdSelected, String imageLink) {
        FirebaseDatabase.getInstance()
                .getReference("Projects")
                .push()
                .setValue(new Project(name,imageLink,description,categoryIdSelected,address,city,area))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Uploaded..", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)    {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                image_preview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCategorySpinner() {
        FirebaseDatabase.getInstance()
                .getReference("Categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())    {
                            CategoryItem item = postSnapshot.getValue(CategoryItem.class);
                            String key = postSnapshot.getKey();

                            spinnerData.put(key,item.getName());
                        }

                        Object[] valueArray = spinnerData.values().toArray();
                        List<Object> valueList = new ArrayList<>();
                        valueList.add(0,"Category");
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList);

                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                Object[] keyArray = spinnerData.keySet().toArray();
                                List<Object> keyList = new ArrayList<>();
                                keyList.add(0,"Category_Key");
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelected = keyList.get(position).toString();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
