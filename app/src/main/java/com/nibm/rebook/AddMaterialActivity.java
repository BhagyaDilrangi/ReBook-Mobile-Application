package com.nibm.rebook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nibm.rebook.dto.Material;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddMaterialActivity extends AppCompatActivity {

    private static final String TAG = "AddMaterialActivity";
    private ProgressBar progressBar;
    private Button btnPublish;
    private EditText etTitle, etPrice;
    private Spinner spinnerType, spinnerCategory;
    private ImageView imgPreview;
    private TextView txtPdfFileName;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String encodedImageString = "";
    private String pdfFileString = "";

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    // Image Picker Launcher for Cover Image selection
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            if (bitmap != null) {
                                encodedImageString = bitmapToBase64(bitmap);
                                if (imgPreview != null) {
                                    imgPreview.setVisibility(View.VISIBLE);
                                    imgPreview.setImageBitmap(bitmap);
                                }
                                Toast.makeText(this, "Cover Image Loaded Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Selected file is not a valid image", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Image conversion error: " + e.getMessage());
                            Toast.makeText(this, "Failed to load image from storage", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    // PDF Document Picker Launcher to browse device documents and render first page preview
    private final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            pdfFileString = uri.toString();
                            String fileName = getFileName(uri);

                            if (txtPdfFileName != null) {
                                txtPdfFileName.setText("Selected PDF: " + fileName);
                            }

                            // Render PDF First Page to Bitmap Preview
                            Bitmap pdfPageBitmap = renderPdfFirstPage(uri);
                            if (pdfPageBitmap != null && imgPreview != null) {
                                imgPreview.setVisibility(View.VISIBLE);
                                imgPreview.setImageBitmap(pdfPageBitmap);
                            }

                            Toast.makeText(this, "PDF Selected: " + fileName, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, "PDF selection/preview error: " + e.getMessage());
                            Toast.makeText(this, "Failed to render PDF preview", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("materials");

        progressBar = findViewById(R.id.progressBar);
        btnPublish = findViewById(R.id.btnPublishListing);
        etTitle = findViewById(R.id.etMaterialTitle);
        etPrice = findViewById(R.id.etMaterialPrice);
        spinnerType = findViewById(R.id.spinnerMaterialType);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgPreview = findViewById(R.id.imgPreview);
        txtPdfFileName = findViewById(R.id.txtPdfFileName);

        String[] types = {"Sale", "Borrow", "Donate"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(adapterType);

        String[] categories = {"Books", "Notes", "Past Papers", "Calculators"};
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterCategory);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnUploadPreview = findViewById(R.id.btnUploadPreview);

        // Open device file storage for Cover Image
        btnUploadImage.setOnClickListener(v -> openFileStorage("image/*"));

        // Open device file storage for PDF documents
        btnUploadPreview.setOnClickListener(v -> openFileStorage("application/pdf"));

        btnPublish.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }
            String sellerId = mAuth.getCurrentUser().getUid();

            btnPublish.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            String materialId = mDatabase.push().getKey();
            Material newMaterial = new Material(materialId, title, "Available", type, category, sellerId, price);

            // Save Base64 image and PDF URI string to DTO properties so other UIs can fetch them
            newMaterial.setImageUrl(encodedImageString);
            newMaterial.setPdfFile(pdfFileString);

            if (materialId != null) {
                mDatabase.child(materialId).setValue(newMaterial)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            btnPublish.setEnabled(true);
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddMaterialActivity.this, MyListingsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown"), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void openFileStorage(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        if ("application/pdf".equals(mimeType)) {
            pdfPickerLauncher.launch(intent);
        } else {
            imagePickerLauncher.launch(intent);
        }
    }

    private Bitmap renderPdfFirstPage(Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                if (pdfRenderer.getPageCount() > 0) {
                    PdfRenderer.Page page = pdfRenderer.openPage(0);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();
                    pdfRenderer.close();
                    return bitmap;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "PDF Rendering error: " + e.getMessage());
        }
        return null;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting filename: " + e.getMessage());
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}