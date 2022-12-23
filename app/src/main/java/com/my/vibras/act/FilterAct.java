package com.my.vibras.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.slider.LabelFormatter;
import com.google.gson.Gson;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityFilterBinding;
import com.my.vibras.model.SuccessResFilterData;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class FilterAct extends AppCompatActivity {

    private VibrasInterface apiInterface;
    ActivityFilterBinding binding;
    private SuccessResSignup.Result userDetail;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
          String  myLatitude  ="";
    String        myLongitude ="";
    String[] arrrayShouldNot = new String[] {
            "Smoke", "Animals", "Children", "Alcohol"
            ,"Parties","Ego","Vegetarian","Left-wing ideology","Right-wing ideology","Centrist","ideology"
    };

    String[] arrayShould = new String[] {
            "Have a job", "Good spelling", "Studies", "Ambition",
            "Own company","Independence","Children" ,"Animals",
            "Vegetarian","Smoking","Family","Travel","Left-wing ideology","Right-wing ideology","Ideology of the centre"
    };

    String[] Arraygender = new String[] {
            "Male", "Female","Both"
    };

    String[] arrayAgeRange = new String[] {
            "10-25", "25-30","30-45","45-60"
    };

    String[] arrayLanaguage = new String[] {
            "English", "Japanese","Norwegian","French",
            "Italian", "Chinese","German",
    };

    ArrayAdapter<String> adapter;

    private String shouldNot="",should="",gender="",ageRangeFrom="",ageRangeTo="",preferedLang="",location="",distance="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         binding= DataBindingUtil.setContentView(this,R.layout.activity_filter);
        Places.initialize(getApplicationContext().getApplicationContext(), getString(R.string.api_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getApplicationContext());
         binding.RRback.setOnClickListener(v -> {
            onBackPressed();
         });     binding.etLocation.setOnClickListener(v -> {



//                        Navigation.findNavController(v).navigate(R.id.action_addAddressFragment_to_currentLocationFragment);

                        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                                Place.Field.ADDRESS,Place.Field.LAT_LNG);
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                .build(getApplicationContext());
                        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
         });

         apiInterface = ApiClient.getClient().create(VibrasInterface.class);

         binding.ivReset.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(FilterAct.this)
                            .setTitle("Reset Filter")
                            .setMessage("Are you sure you want to reset this filter?")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                );

        binding.ageSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                return String.format(Locale.US, "%.0f", value);
            }
        });

        binding.distanceSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                return String.format(Locale.US, "%.0f", value);
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrrayShouldNot);
        binding.spinnerShouldNot.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayShould);
        binding.spinnerShould.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Arraygender);
        binding.spinnerWantToUse.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayLanaguage);
        binding.spinnerLanguage.setAdapter(adapter);

        binding.btnAdd.setOnClickListener(v ->
                {
                   // shouldNot = binding.spinnerShouldNot.getSelectedItem().toString();
                    //should = binding.spinnerShould.getSelectedItem().toString();
                  gender = binding.spinnerWantToUse.getSelectedItem().toString();
                    List<Float> val = binding.ageSlider.getValues();
                    float f11 = val.get(0);
                    ageRangeFrom = String.valueOf(f11);
                    f11 = val.get(1);
                    ageRangeTo = String.valueOf(f11);
                    //preferedLang = binding.spinnerLanguage.getSelectedItem().toString();
                    location = binding.etLocation.getText().toString();
                    List<Float> values = binding.distanceSlider.getValues();
                    float f12 = values.get(0);
                    distance = String.valueOf(f12);
                    if(location.equalsIgnoreCase(""))
                    {
                        Toast.makeText(FilterAct.this, "Please enter location.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        filterLocation();
                    }
                }
                );

        getProfile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                //  eventLocation = place.getAddress();
                LatLng latLng = place.getLatLng();
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                myLatitude = Double.toString(latitude);
                myLongitude = Double.toString(longitude);
                String address = place.getAddress();
                //  eventLocation = address;
                binding.etLocation.setText(address);
               /* binding.etLocation.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.etLocation.setText(address);
                    }
                });*/

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
    }
 /*    @Override
    protected void onResume() {
        super.onResume();

    }*/

    public void filterLocation()
    {
        String userId = SharedPreferenceUtility.getInstance(FilterAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(FilterAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
  /*      map.put("should_not", shouldNot);
        map.put("should", should);*/
        map.put("age_range_from", ageRangeFrom);
        map.put("age_range_to", ageRangeTo);
     //   map.put("p_language", preferedLang);
        map.put("f_location", location);
        map.put("f_lat",  myLatitude );
        map.put("f_lon",  myLongitude);
        map.put("distance", distance);
        map.put("gender", gender);
       /* if (gender.equalsIgnoreCase("Man")){
            map.put("gender", "Male");

        }else  if (gender.equalsIgnoreCase("Women")){
            map.put("gender", "Female");

        }else {
            map.put("gender", "both");

        }*/

        Log.e("TAG", "filterLocation:-000000000000-------------- "+map );
        Call<SuccessResFilterData> call = apiInterface.filter(map);
        call.enqueue(new Callback<SuccessResFilterData>() {
            @Override
            public void onResponse(Call<SuccessResFilterData> call,
                                   Response<SuccessResFilterData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResFilterData data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(FilterAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResFilterData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(FilterAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(FilterAct.this, getString(R.string.please_wait));
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
                        showToast(FilterAct.this, data.message);
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

    public void setProfileDetails()
    {
        int selectedPosition =-1;

        for(int i=0;i<arrrayShouldNot.length;i++)
        {
            if(arrrayShouldNot[i].equalsIgnoreCase(userDetail.getShouldNot()))
            {
                selectedPosition = i;
                break;
            }
        }

        binding.spinnerShouldNot.setSelection(selectedPosition);

        for(int i=0;i<arrayShould.length;i++)
        {
            if(arrayShould[i].equalsIgnoreCase(userDetail.getShould()))
            {
                selectedPosition = i;
                break;
            }
        }

        binding.spinnerShould.setSelection(selectedPosition);

        for(int i=0;i<Arraygender.length;i++)
        {
            if(Arraygender[i].equalsIgnoreCase(userDetail.getWantTo()))
            {
                selectedPosition = i;
                break;
            }
        }
        binding.spinnerWantToUse.setSelection(selectedPosition);

       /* for(int i=0;i<arrayAgeRange.length;i++)
        {
            if(arrayAgeRange[i].equalsIgnoreCase(userDetail.getAgeRange()))
            {
                selectedPosition = i;
                break;
            }
        }*/

        for(int i=0;i<arrayLanaguage.length;i++)
        {
            if(arrayLanaguage[i].equalsIgnoreCase(userDetail.getpLanguage()))
            {
                selectedPosition = i;
                break;
            }
        }
        binding.spinnerLanguage.setSelection(selectedPosition);
        binding.etLocation.setText(userDetail.getfLocation());
        myLatitude=userDetail.getfLat();
                myLongitude=userDetail.getLon();
        float f = Float.parseFloat(userDetail.getDistance());
        binding.slider.setValue(f);

        List<Float> f1 = new ArrayList<>();
        float ageFrom = Float.parseFloat(userDetail.getAgeRangeFrom());
        float ageTo = Float.parseFloat(userDetail.getAgeRangeTo());
        f1.add(ageFrom);
        f1.add(ageTo);
        binding.ageSlider.setValues(f1);

        float f12 = Float.parseFloat(userDetail.getDistance());

        binding.distanceSlider.setValues(f12);


    }


}