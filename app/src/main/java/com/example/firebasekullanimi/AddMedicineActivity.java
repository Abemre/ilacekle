
package com.example.firebasekullanimi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddMedicineActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etMedicineName, etMedicineDose;
    private ImageView ivMedicineImage;
    private Button btnUploadImage, btnSave;

    private Uri imageUri;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        etMedicineName = findViewById(R.id.etMedicineName);
        etMedicineDose = findViewById(R.id.etMedicineDose);
        ivMedicineImage = findViewById(R.id.ivMedicineImage);
        btnUploadImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSave);

        progressDialog = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("medicines");
        storageReference = FirebaseStorage.getInstance().getReference("medicine_images");

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivMedicineImage.setImageURI(imageUri);
        }
    }

    private void uploadData() {
        final String name = etMedicineName.getText().toString().trim();
        final String dose = etMedicineDose.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dose) || imageUri == null) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun ve bir resim seçin", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Veri yükleniyor...");
        progressDialog.show();

        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();

                        // İlaç adı, dozajı ve resim URL'si Firebase Realtime Database'e kaydedilir
                        DatabaseReference medicineRef = databaseReference.push();
                        medicineRef.child("name").setValue(name);
                        medicineRef.child("dose").setValue(dose);
                        medicineRef.child("imageUrl").setValue(imageUrl);

                        progressDialog.dismiss();
                        Toast.makeText(AddMedicineActivity.this, "İlaç başarıyla eklendi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddMedicineActivity.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddMedicineActivity.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }


