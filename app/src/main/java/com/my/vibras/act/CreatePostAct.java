package com.my.vibras.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityCreatePostBinding;
import com.my.vibras.model.SuccessResGetMyStories;
import com.my.vibras.model.SuccessResUploadPost;
import com.my.vibras.model.SuccessResUploadStory;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.RealPathUtil;
import com.my.vibras.utility.Session;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class CreatePostAct extends AppCompatActivity {
    private static final int LOAD_TESTING_VIDEO = 108;
    private static final int ACTION_TAKE_VIDEO = 109;
    ActivityCreatePostBinding binding;
    private static final int SELECT_FILE = 2;
    String str_image_path = "";
    String str_video_path = "";
    private static final int REQUEST_CAMERA = 1;
    private Uri uriSavedImage;
    private static final int MY_PERMISSION_CONSTANT = 5;
    boolean cameraClicked = true;
    private String type = "POST";
    private boolean haveStory = false;
    private String strSuperLikes;
    private String storyID = "";
    private VibrasInterface apiInterface;
    private String description = "";
    private String postType = "";
    Session session;
    Integer integer = 0;

    File videoone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);
        session = new Session(this);
        session.setPublishfile("");
        session.setPublishTxt("");
        session.setPublishType("POST");
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.tvPost.setOnClickListener(v ->
                {
                    type = "POST";
                    session.setPublishType("POST");
                    binding.tvPost.setBackground(ContextCompat.getDrawable(CreatePostAct.this, R.drawable.black_cornors_30));
                    binding.tvPost.setTextColor(getResources().getColor(R.color.white));
                    binding.tvStory.setBackground(ContextCompat.getDrawable(CreatePostAct.this, R.drawable.white_cornors_30));
                    binding.tvStory.setTextColor(getResources().getColor(R.color.black));
                }
        );

        binding.tvPublish.setOnClickListener(v ->
                {
                    if (!str_image_path.equalsIgnoreCase("")) {
                        description = binding.etDescription.getText().toString().trim();
                        if (session.getPublishType().equalsIgnoreCase("STORY")) {
                            ArrayList<String> imagesVideosPathList = new ArrayList<>();
                            imagesVideosPathList.add(str_image_path);
                            Log.e(TAG, "tvPublish: " + str_image_path);
                            Log.e(TAG, "tvPublish: " + imagesVideosPathList.size());
                            Log.e(TAG, "tvPublish: " + postType);
                            Log.e(TAG, "tvPublish: " + type);

                            boolean image = false;
                            if (session.getPublishfile().equalsIgnoreCase("image")) {
                                image = true;
                            } else {
                                image = false;
                            }
                            if (haveStory) {

                                updateStory(imagesVideosPathList, image, session.getPublishfile());
                            } else {
                                uploadStory(imagesVideosPathList, image, session.getPublishfile());
                            }
                        } else {
                            uploadPost();
                        }
                    } else {
                        Toast.makeText(CreatePostAct.this, "Please select a media file.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.tvStory.setOnClickListener(v ->
                {
                    type = "STORY";
                    session.setPublishType("STORY");
                    binding.tvPost.setBackground(ContextCompat.getDrawable(CreatePostAct.this, R.drawable.white_cornors_30));
                    binding.tvPost.setTextColor(getResources().getColor(R.color.black));
                    binding.tvStory.setBackground(ContextCompat.getDrawable(CreatePostAct.this, R.drawable.black_cornors_30));
                    binding.tvStory.setTextColor(getResources().getColor(R.color.white));
                }
        );

        binding.ivCamera.setOnClickListener(v ->
                {
                    postType = "image";
                    session.setPublishfile("image");
                    cameraClicked = true;
                    if (checkPermisssionForReadStorage()) {
                        openCamera();
                    }
                }
        );

        binding.ivGalary.setOnClickListener(v ->
                {
                    postType = "image";
                    session.setPublishfile("image");
                    cameraClicked = false;
                    if (checkPermisssionForReadStorage()) {
                        getPhotoFromGallary();
                    }
                }
        );
        binding.ivVideo.setOnClickListener(v -> {
            postType = "video";
            session.setPublishfile("video");
            final CharSequence[] options = {"Take Video", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostAct.this);
            builder.setTitle("Add Video!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Video")) {
                        integer = 1;
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                   //     takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath() + Calendar.getInstance().getTimeInMillis() + "story.mp4");
                        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
                    } else if (options[item].equals("Choose from Gallery")) {
                        if (checkPermisssionForReadStorage()) {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), LOAD_TESTING_VIDEO);
                            integer = 1;
                        }
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        });
        if (NetworkAvailablity.checkNetworkStatus(CreatePostAct.this)) {

            getStories();

        } else {
            Toast.makeText(CreatePostAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    public void uploadStory(ArrayList<String> imagesVideosPathList, boolean image, String type1) {

        String strUserId = SharedPreferenceUtility.getInstance(CreatePostAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(CreatePostAct.this, getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();
        if (image) {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        } else {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                //       File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                File file = new File(imagesVideosPathList.get(i));
               /* File getPathFile = new File(RealPathUtil.getVideoPath2(CreatePostAct.this,selectedVideo));
                videoone = getPathFile;
                System.out.println("getPathFile------   " + getPathFile);*/
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);
        Call<SuccessResUploadStory> loginCall = apiInterface.uploadStory(userId, caption, type, filePartList);

        loginCall.enqueue(new Callback<SuccessResUploadStory>() {
            @Override
            public void onResponse(Call<SuccessResUploadStory> call, Response<SuccessResUploadStory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUploadStory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(CreatePostAct.this, data.message);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(CreatePostAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadStory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == LOAD_TESTING_VIDEO) {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedVideo = data.getData();
                        MediaPlayer mp = MediaPlayer.create(CreatePostAct.this,
                                Uri.parse(String.valueOf(selectedVideo)));
                        int duration = mp.getDuration() / 1000;
                        mp.release();
                        if (duration <= 30) {
                            Glide.with(CreatePostAct.this)
                                    .load(selectedVideo.toString())
                                    .centerCrop()
                                    .into(binding.ivProfile);
                            str_image_path = RealPathUtil.getVideoPath2(CreatePostAct.this, selectedVideo);
                        } else {
                            Toast.makeText(CreatePostAct.this, "Please Select Video Duration 30  Sec Only", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } else if (requestCode == SELECT_FILE) { try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(CreatePostAct.this)
                            .load(selectedImage)
                            .centerCrop()
                            .into(binding.ivProfile);
                    Uri tempUri = getImageUri(CreatePostAct.this, bitmap);
                    String image = RealPathUtil.getRealPath(CreatePostAct.this, tempUri);
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
                        Uri tempUri = getImageUri(CreatePostAct.this, imageBitmap);
                        String image = RealPathUtil.getRealPath(CreatePostAct.this, tempUri);
                        str_image_path = image;
                        Glide.with(CreatePostAct.this)
                                .load(imageBitmap)
                                .centerCrop()
                                .into(binding.ivProfile);
//                        updateCoverPhoto();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (requestCode==ACTION_TAKE_VIDEO){

                try {
                    Uri vid = data.getData();
                    Glide.with(CreatePostAct.this)
                            .load(vid.toString())
                            .centerCrop()
                            .into(binding.ivProfile);
                    str_image_path = getRealPathFromURI(vid);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(CreatePostAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(CreatePostAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(CreatePostAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePostAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(CreatePostAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(CreatePostAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {

                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

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
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreatePostAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    }
                } else {
                    Toast.makeText(CreatePostAct.this, "Permission denied", Toast.LENGTH_SHORT).show();
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
                        if (cameraClicked) {
                            openCamera();
                        } else {
                            getPhotoFromGallary();
                        }
                    } else {
                        Toast.makeText(CreatePostAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreatePostAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(CreatePostAct.this.getPackageManager()) != null)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    public void uploadPost() {
        String strUserId = SharedPreferenceUtility.getInstance(CreatePostAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreatePostAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if (file != null) {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            } else {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody myDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody myType = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody pType = RequestBody.create(MediaType.parse("text/plain"), postType);

        Call<SuccessResUploadPost> loginCall = apiInterface.uploadPost(userId, myDescription, myType, pType, filePart);
        loginCall.enqueue(new Callback<SuccessResUploadPost>() {
            @Override
            public void onResponse(Call<SuccessResUploadPost> call, Response<SuccessResUploadPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadPost data = response.body();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(CreatePostAct.this, data.message);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(CreatePostAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());

                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadPost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getStories() {
        String userId = SharedPreferenceUtility.getInstance(CreatePostAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreatePostAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetMyStories> call = apiInterface.getMyStory(map);
        call.enqueue(new Callback<SuccessResGetMyStories>() {
            @Override
            public void onResponse(Call<SuccessResGetMyStories> call, Response<SuccessResGetMyStories> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMyStories data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        haveStory = true;
                        storyID = data.getResult().get(0).getId();
                    } else if (data.status.equals("0")) {
                        //    showToast(getActivity(), data.message);
                        haveStory = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetMyStories> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void updateStory(ArrayList<String> imagesVideosPathList, boolean image, String type1) {
        DataManager.getInstance().showProgressMessage(CreatePostAct.this, getString(R.string.please_wait));
        List<MultipartBody.Part> filePartList = new LinkedList<>();
        if (image) {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        } else {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                //       File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                File file = new File(imagesVideosPathList.get(i));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        }
        String strUserId = SharedPreferenceUtility.getInstance(CreatePostAct.this).getString(USER_ID);
        RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody storyId = RequestBody.create(MediaType.parse("text/plain"), storyID);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);
        Call<SuccessResUploadStory> loginCall = apiInterface.updateStory(userID, storyId, type, filePartList);
        loginCall.enqueue(new Callback<SuccessResUploadStory>() {
            @Override
            public void onResponse(Call<SuccessResUploadStory> call, Response<SuccessResUploadStory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUploadStory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(CreatePostAct.this, data.message);
                        finish();

                    } else if (data.status.equals("0")) {
                        showToast(CreatePostAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadStory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}