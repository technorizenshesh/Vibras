package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityEditProfileBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.model.SuccessResUploadSelfie;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.RealPathUtil;
import com.my.vibras.utility.SharedPreferenceUtility;


import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;
 import static com.my.vibras.utility.RandomString.IncodeIntoBase64;

public class EditProfileAct extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    String str_image_path="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri uriSavedImage;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private VibrasInterface apiInterface;
    private SuccessResSignup.Result userDetail;
    private String fName= "" , lName = "", email = "",bio="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        if (NetworkAvailablity.checkNetworkStatus(EditProfileAct.this)) {
            getProfile();
            getProfileImage();
        } else {
            Toast.makeText(EditProfileAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.RLogin.setOnClickListener(v ->
                {
                    fName = binding.edtFirstName.getText().toString();
                    lName = binding.edtLastName.getText().toString();
                    email = binding.edtEmail.getText().toString();
                    bio = binding.edtBio.getText().toString();
                    if (isValid()) {
                        if (NetworkAvailablity.checkNetworkStatus(EditProfileAct.this)) {
                            updateProfile();
                        } else {
                            Toast.makeText(EditProfileAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfileAct.this, "ON error", Toast.LENGTH_SHORT).show();
                    }
                }
                );

        binding.ivEdit.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );
        
       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });
    }

    private boolean isValid() {
        if (fName.equalsIgnoreCase("")) {
            binding.edtFirstName.setError(getString(R.string.enter_first));
            return false;
        } else if (lName.equalsIgnoreCase("")) {
            binding.edtLastName.setError(getString(R.string.enter_last));
            return false;
        }else  if (email.equalsIgnoreCase("")) {
            binding.edtEmail.setError(getString(R.string.enter_email));
        }
        return true;
    }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(EditProfileAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResSignup> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    userDetail = data.getResult();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();
                    } else if (data.status.equals("0")) {
                        showToast(EditProfileAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getProfileImage() {
        String userId = SharedPreferenceUtility.getInstance(EditProfileAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResUploadSelfie> call = apiInterface.getProfileImage(map);
        call.enqueue(new Callback<SuccessResUploadSelfie>() {
            @Override
            public void onResponse(Call<SuccessResUploadSelfie> call, Response<SuccessResUploadSelfie> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUploadSelfie data = response.body();
                    SuccessResUploadSelfie.Result userData = data.getResult();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);



                    } else if (data.status.equals("0")) {
                        showToast(EditProfileAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResUploadSelfie> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setProfileDetails()
    {

        binding.edtFirstName.setText(StringEscapeUtils.unescapeJava(userDetail.getFirstName()));
        binding.edtLastName.setText(StringEscapeUtils.unescapeJava(userDetail.getLastName()));
        binding.edtEmail.setText((userDetail.getEmail()));
        binding.etPhone.setText(userDetail.getMobile());
        binding.edtBio.setText(  StringEscapeUtils.unescapeJava(userDetail.getBio()));

        Glide
                .with(EditProfileAct.this)
                .load(userDetail.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);

    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(EditProfileAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(view -> {
            dialog.dismiss();
            dialog.cancel();
            getPhotoFromGallary();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(EditProfileAct.this.getPackageManager()) != null)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {

                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(EditProfileAct.this.getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(EditProfileAct.this)
                            .load(selectedImage)
                            .centerCrop()
                            .into(binding.ivProfile);
                    Uri tempUri = getImageUri(EditProfileAct.this, bitmap);
                    String image = RealPathUtil.getRealPath(EditProfileAct.this, tempUri);
                    str_image_path = image;

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }



            } else if (requestCode == REQUEST_CAMERA) {

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(EditProfileAct.this, imageBitmap);
                        String image = RealPathUtil.getRealPath(EditProfileAct.this, tempUri);
                        str_image_path = image;
                        Glide.with(EditProfileAct.this)
                                .load(str_image_path)
                                .centerCrop()
                                .into(binding.ivProfile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap BITMAP_RE_SIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(EditProfileAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            } else {
                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {
            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
/*
                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
*/

//
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditProfileAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
                } else {
                    Toast.makeText(EditProfileAct.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(EditProfileAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    public void updateProfile()
    {
        String strUserId = SharedPreferenceUtility.getInstance(EditProfileAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody userId  = RequestBody.create(MediaType.parse("text/plain"),   strUserId);
        RequestBody fname   = RequestBody.create(MediaType.parse("text/plain"),   IncodeIntoBase64( fName));
        RequestBody lname   = RequestBody.create(MediaType.parse("text/plain"),    IncodeIntoBase64(lName));
     //   RequestBody myemail = RequestBody.create(MediaType.parse("text/plain"),   email);
        RequestBody mybio   = RequestBody.create(MediaType.parse("text/plain"),    IncodeIntoBase64((bio)));
         Call<SuccessResSignup> loginCall = apiInterface.updateProfile(userId,fname,lname,mybio,filePart);
        loginCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);
                    EditProfileAct.this.getWindow().getDecorView().clearFocus();
                    getProfile();
                    getProfileImage();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}