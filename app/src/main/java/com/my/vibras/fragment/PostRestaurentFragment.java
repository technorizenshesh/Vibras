package com.my.vibras.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.R;
import com.my.vibras.adapter.MultipleImagesAdapter;
import com.my.vibras.databinding.FragmentPostEventsBinding;
import com.my.vibras.databinding.FragmentPostRestaurentBinding;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.ImageCancelClick;
import com.my.vibras.utility.RealPathUtil;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class PostRestaurentFragment extends Fragment {

    private Fragment fragment;

    private FragmentPostRestaurentBinding binding;

    private String restrantName="",strLocation="",strDetails="";

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetCategory.Result> categoryResult = new ArrayList<>();

    private ArrayList<String> eventsCategories = new ArrayList<>();

    private ArrayList<String> eventsType = new ArrayList<>();

    private static final int MY_PERMISSION_CONSTANT = 5;

    String str_image_path="";

    final Calendar myCalendar= Calendar.getInstance();

    private ArrayList<String> imagesList = new ArrayList<>();

    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_FILE = 2;

    private MultipleImagesAdapter multipleImagesAdapter;

    private String whichSelected="";

    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_restaurent,container, false);

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        binding.ivMultiple.setOnClickListener(v ->
                {
                    whichSelected = "multiple";
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );

        binding.ivCamera.setOnClickListener(v ->
                {
                    whichSelected = "event";
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );

        binding.rlAdd.setOnClickListener(v ->
                {
                    restrantName = binding.etRestaurantName.getText().toString().trim();
                    strDetails = binding.etDetails.getText().toString().trim();
                    strLocation = binding.etLocation.getText().toString().trim();
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                        isValid();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        multipleImagesAdapter = new MultipleImagesAdapter(getActivity(), imagesList, new ImageCancelClick() {
            @Override
            public void imageCancel(int position) {
                imagesList.remove(position);
                multipleImagesAdapter.notifyDataSetChanged();
            }
        });
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        binding.rvImages.setAdapter(multipleImagesAdapter);


        return binding.getRoot();
    }

    private void isValid() {

        if (restrantName.equalsIgnoreCase("")) {
            binding.etRestaurantName.setError(getString(R.string.enter_restaurant_name));
        }else if (str_image_path.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select Event Image");
        }else if (strLocation.equalsIgnoreCase("")) {
            binding.etLocation.setError(getString(R.string.enter_restaurant_location));
        }else if (strDetails.equalsIgnoreCase("")) {
            binding.etDetails.setError(getString(R.string.enter_details));
        }else if (imagesList.size()==0) {
            showToast(getActivity(),"Please select Event Images.");
        }else
        {

            addRestaurant();

        }

    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
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
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
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
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                try {

                    if(whichSelected.equalsIgnoreCase("event"))
                    {

                        Uri selectedImage = data.getData();
                        Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Glide.with(getActivity())
                                .load(selectedImage)
                                .centerCrop()
                                .into(binding.ivProfile);
                        Uri tempUri = getImageUri(getActivity(), bitmap);
                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        str_image_path = image;

                    }
                    else
                    {
                        Uri selectedImage = data.getData();
                        Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(getActivity(), bitmap);
                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        imagesList.add(image);
                        multipleImagesAdapter.notifyDataSetChanged();

                    }

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            } else if (requestCode == REQUEST_CAMERA) {

                try {
                    if (data != null) {


                        if(whichSelected.equalsIgnoreCase("event"))
                        {
                            Bundle extras = data.getExtras();
                            Bitmap bitmapNew = (Bitmap) extras.get("data");
                            Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                            Uri tempUri = getImageUri(getActivity(), imageBitmap);
                            String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                            str_image_path = image;
                            Glide.with(getActivity())
                                    .load(imageBitmap)
                                    .centerCrop()
                                    .into(binding.ivProfile);

                        }
                        else
                        {
                            Bundle extras = data.getExtras();
                            Bitmap bitmapNew = (Bitmap) extras.get("data");
                            Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                            Uri tempUri = getImageUri(getActivity(), imageBitmap);
                            String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                            imagesList.add(image);
                            multipleImagesAdapter.notifyDataSetChanged();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
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
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void addRestaurant()
    {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesList.size(); i++) {

            String image = imagesList.get(i);

            if(!imagesList.get(i).contains("https://nobu.es/tasknobu/uploads"))
            {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image_file[]", file.getName(), RequestBody.create(MediaType.parse("image_file[]/*"), file)));
            }
        }

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

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody eventName = RequestBody.create(MediaType.parse("text/plain"), restrantName);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), strLocation);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), strDetails);

        Call<SuccessResAddRestaurant> loginCall = apiInterface.addRestaurants(userId,eventName,address,lat,lon,description,filePart,filePartList);
        loginCall.enqueue(new Callback<SuccessResAddRestaurant>() {
            @Override
            public void onResponse(Call<SuccessResAddRestaurant> call, Response<SuccessResAddRestaurant> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddRestaurant data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);

                    startActivity(new Intent(getActivity(), HomeComapnyAct.class));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddRestaurant> call, Throwable t) {
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



}