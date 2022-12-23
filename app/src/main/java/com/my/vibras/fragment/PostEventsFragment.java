package com.my.vibras.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.R;
import com.my.vibras.act.PaymentsAct;
import com.my.vibras.adapter.MultipleImagesAdapter;
import com.my.vibras.databinding.FragmentPostEventsBinding;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.model.SuccessResAddEvent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class PostEventsFragment extends Fragment {

    private Fragment fragment;

    private FragmentPostEventsBinding binding;

    private static int AUTOCOMPLETE_REQUEST_CODE = 9;

    private String eventName="",eventDate="",eventTime="",eventCategory="",eventLocation="",etAmount="",eventDetails="",eventType="";

    private String myLatitude = "",myLongitude="";

    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetCategory.Result> categoryResult = new ArrayList<>();

    private ArrayList<String> eventsCategories = new ArrayList<>();

    private ArrayList<String> eventsType = new ArrayList<>();

    String str_image_path="";

    final Calendar myCalendar= Calendar.getInstance();

    private ArrayList<String> imagesList = new ArrayList<>();
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private MultipleImagesAdapter multipleImagesAdapter;
    private String whichSelected="";
    private static final int MY_PERMISSION_CONSTANT = 5;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_events,container, false);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.api_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());

        eventsType.add("Public");
        eventsType.add("Private");
        binding.etLocation.setOnClickListener(v ->
                {
//                        Navigation.findNavController(v).navigate(R.id.action_addAddressFragment_to_currentLocationFragment);

                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(getActivity());
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }
        );

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),   android.R.layout.simple_spinner_item, eventsType);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        binding.spinnerType.setAdapter(spinnerArrayAdapter);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        binding.etEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.ivCamera.setOnClickListener(v ->
                {
                    whichSelected = "event";
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );

        binding.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.etTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        binding.ivMultiple.setOnClickListener(v ->
                {
                    whichSelected = "multiple";
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );

        binding.rlAdd.setOnClickListener(v ->
                {

                    eventName = binding.etEventName.getText().toString().trim();
                    eventDate = binding.etEventDate.getText().toString().trim();
                    eventTime = binding.etTime.getText().toString().trim();
                    eventCategory = binding.spinnerCategory.getSelectedItem().toString();
                    eventLocation = binding.etLocation.getText().toString().trim();
                    etAmount = binding.etBookingAmount.getText().toString().trim();
                    eventDetails = binding.etEventDetails.getText().toString().trim();
                    eventType = binding.spinnerType.getSelectedItem().toString();
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                        isValid();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        getEventCategory();

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

    private void updateLabel(){
        String myFormat="MM/dd/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.etEventDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void isValid() {

        if (eventName.equalsIgnoreCase("")) {
            binding.etEventName.setError("Enter Event Name.");
        }else if (str_image_path.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select Event Image");
        }else if (eventDate.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select Date");
        }else if (eventTime.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select time");
        }else if (eventCategory.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select Event Category");
        }else if (eventLocation.equalsIgnoreCase("")) {
            binding.etLocation.setError("Please Enter Event Location");
        }else if (etAmount.equalsIgnoreCase("")) {
            binding.etBookingAmount.setError("Please Enter Event Amount");
        }else if (eventDetails.equalsIgnoreCase("")) {
            binding.etEventDetails.setError("Please Enter Event Details");
        }else if (eventType.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please select Event Type");
        }else if (imagesList.size()==0) {
            showToast(getActivity(),"Please select Event Images.");
        }else
        {

//            addEvent();

            startActivity(new Intent(getActivity(), PaymentsAct.class)
                    .putExtra("from","event")
                    .putExtra("eventName",eventName)
                    .putExtra("str_image_path",str_image_path)
                    .putExtra("eventDate",eventDate)
                    .putExtra("eventTime",eventTime)
                    .putExtra("eventCategory",eventCategory)
                    .putExtra("eventLocation",eventLocation)
                    .putExtra("etAmount",etAmount)
                    .putExtra("eventDetails",eventDetails)
                    .putExtra("eventType",eventType)
                    .putExtra("lat",myLatitude)
                    .putExtra("lon",myLongitude)
                    .putExtra("imagesList",imagesList)
            );
        }
    }

    private void getEventCategory() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        Call<SuccessResGetCategory> call = apiInterface.getEventsCategory(map);
        call.enqueue(new Callback<SuccessResGetCategory>() {
            @Override
            public void onResponse(Call<SuccessResGetCategory> call, Response<SuccessResGetCategory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCategory data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        setProfileDetails();
                        categoryResult.clear();
                        categoryResult.addAll(data.getResult());
                        setSpinner();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetCategory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setSpinner()
    {

        eventsCategories.clear();

        for(SuccessResGetCategory.Result result:categoryResult)
        {
            eventsCategories.add(result.getName());
        }

//      Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),   R.layout.simple_spinner, eventsCategories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        binding.spinnerCategory.setAdapter(spinnerArrayAdapter);
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
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                eventLocation = place.getAddress();
                LatLng latLng = place.getLatLng();
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                myLatitude = Double.toString(latitude);
                myLongitude = Double.toString(longitude);
                String address = place.getAddress();
                eventLocation = address;
                binding.etLocation.setText(address);
                binding.etLocation.post(new Runnable(){
                    @Override
                    public void run() {
                        binding.etLocation.setText(address);
                    }
                });

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
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

     /*   if (resultCode == RESULT_OK) {
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

            } else
            if (requestCode == REQUEST_CAMERA) {
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
        }*/
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

    public void addEvent()
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
        RequestBody eventNm = RequestBody.create(MediaType.parse("text/plain"), eventName);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), eventLocation);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody eventDat = RequestBody.create(MediaType.parse("text/plain"), eventDate);
        RequestBody startTime = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody endTIme = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), eventDetails);
        RequestBody bookingAmount = RequestBody.create(MediaType.parse("text/plain"), etAmount);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), eventType);
        RequestBody eventCat = RequestBody.create(MediaType.parse("text/plain"), eventCategory);

        Call<SuccessResAddEvent> loginCall = apiInterface.addEvent(userId,eventNm,address,lat,lon,eventDat,startTime,endTIme,description,bookingAmount,type,eventCat,filePart,filePartList);
        loginCall.enqueue(new Callback<SuccessResAddEvent>() {
            @Override
            public void onResponse(Call<SuccessResAddEvent> call, Response<SuccessResAddEvent> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddEvent data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);

                    startActivity(new Intent(getActivity(), HomeComapnyAct.class));


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddEvent> call, Throwable t) {
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