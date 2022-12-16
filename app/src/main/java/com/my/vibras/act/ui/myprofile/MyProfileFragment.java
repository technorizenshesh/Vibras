package com.my.vibras.act.ui.myprofile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.my.vibras.R;
import com.my.vibras.act.CreatePostAct;
import com.my.vibras.act.EditProfileAct;
import com.my.vibras.act.LikeReceivedActivity;
import com.my.vibras.act.LikeSentActivity;
import com.my.vibras.act.SettingAct;
import com.my.vibras.act.ViewAllEventAct;
import com.my.vibras.act.ViewAllGroupsAct;
import com.my.vibras.adapter.PostsAdapter;
import com.my.vibras.databinding.FragmentMyProfileBinding;
import com.my.vibras.fragment.AppointmentFragment;
import com.my.vibras.fragment.PostsFragment;
import com.my.vibras.fragment.PostsVideoFragment;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.model.SuccessResUploadCoverPhoto;
import com.my.vibras.model.SuccessResUploadSelfie;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.NetworkAvailablity;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.PostClickListener;
import com.my.vibras.utility.RealPathUtil;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

public class MyProfileFragment extends Fragment implements PostClickListener {

    private FragmentMyProfileBinding binding;

    private VibrasInterface apiInterface;

    private SuccessResSignup.Result userDetail;

    private ArrayList<SuccessResGetPosts.Result> postList = new ArrayList<>();
    private MyProfileFragment.Qr_DetailsAdapter adapter;

    String str_image_path = "";
    GPSTracker gpsTracker;
    private static final int REQUEST_CAMERA = 1;

    private static final int SELECT_FILE = 2;

    private Uri uriSavedImage;
    Bundle bundle = new Bundle();
    private static final int MY_PERMISSION_CONSTANT = 5;

    public class Qr_DetailsAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public Qr_DetailsAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PostsFragment recents = new PostsFragment();
                    return recents;

              /*  case 1:
                    AppointmentFragment recents1 = new AppointmentFragment();
                    return recents1;*/

                case 1:
                    PostsVideoFragment recents11 = new PostsVideoFragment();
                    return recents11;

               /* case 3:
                    EventsFragmentComapny recents12 = new EventsFragmentComapny();
                    return recents12;*/

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
         gpsTracker =new GPSTracker(getActivity());


      //  binding = FragmentMyProfileBinding.inflate(inflater,binding.getRoot(),false);
            //    DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false);
        apiInterface = ApiClient.getClient().create(VibrasInterface.class);
     //   binding.scrool.setNestedScrollingEnabled(false);
        binding.imgSetting.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingAct.class));
        });

        binding.ivAddPost.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), CreatePostAct.class));
                }
        );

        binding.llGroup.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ViewAllGroupsAct.class)
                            .putExtra("from", "my"));
                }
        );

        binding.llViewEvents.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(),
                            ViewAllEventAct.class).putExtra("from", "my"));
                }
        );
        binding.likeRecived.setOnClickListener(v -> {
            String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

            startActivity(new Intent(getActivity(),
                    LikeReceivedActivity.class)
                    .putExtra("id",userId));
        });
        binding.followers.setOnClickListener(v -> {
            String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

            startActivity(new Intent(getActivity(),
                    LikeSentActivity.class)
                    .putExtra("id",userId));
        });
      setUpUi();

        binding.ivEditConverPhoto.setOnClickListener(v ->
                {
                    chooseToEdit();
                }
        );
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getProfile();
            //  getPosts();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private void getPosts() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("type_status", "IMAGE");
        Call<SuccessResGetPosts> call = apiInterface.getPost(map);
        call.enqueue(new Callback<SuccessResGetPosts>() {
            @Override
            public void onResponse(Call<SuccessResGetPosts> call, Response<SuccessResGetPosts> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPosts data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        postList.clear();
                        postList.addAll(data.getResult());
                        binding.rvPosts.setHasFixedSize(true);
                        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPosts.setAdapter(new PostsAdapter(getActivity(), postList, MyProfileFragment.this, "Mine"));
                    } else if (data.status.equals("0")) {
                        postList.clear();
                        binding.rvPosts.setHasFixedSize(true);
                        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPosts.setAdapter(new PostsAdapter(getActivity(), postList, MyProfileFragment.this, "Mine"));
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPosts> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResSignup> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    userDetail = data.getResult();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResUploadSelfie> call = apiInterface.getProfileImage(map);
        call.enqueue(new Callback<SuccessResUploadSelfie>() {
            @Override
            public void onResponse(Call<SuccessResUploadSelfie> call, Response<SuccessResUploadSelfie> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUploadSelfie data = response.body();
                    SuccessResUploadSelfie.Result userData = data.getResult();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        setProfileDetails();

                        Glide
                                .with(getActivity())
                                .load(userData.getImage())
                                .centerCrop()
                                .placeholder(R.drawable.ic_user)
                                .into(binding.ivProfile);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    private void setProfileDetails() {

        binding.tvName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());
       //                     binding.tvName.setCompoundDrawables(null,null, requireActivity().getResources().getDrawable(R.drawable.ic_baseline_verified),null);
       // .. // binding.tvName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());

        if (!userDetail.getBio().equalsIgnoreCase("")) {
            binding.tvBio.setVisibility(View.VISIBLE);
            binding.tvBio.setText(userDetail.getBio());
        } else {
            binding.tvBio.setVisibility(View.GONE);
        }

        Glide
                .with(getActivity())
                .load(userDetail.getCoverImage())
                .into(binding.ivCoverPhoto);
        Glide
                .with(getActivity())
                .load(userDetail.getImage())
                .into(binding.ivProfile);

        binding.tvLikeGiven.setText(userDetail.getGivenLikes() + "");
        binding.tvLikeReceived.setText(userDetail.getReceviedLikes() + "");
        if (userDetail.getLat()!=null&&!
                userDetail.getLat().equalsIgnoreCase("0.0"))
        {
            String ff =CurrentCity(Double.parseDouble(userDetail.getLat()),
                    Double.parseDouble(userDetail.getLon()));
            binding.cityState.setText(ff);
        }else {
            if (gpsTracker.getLatitude()>0) {
                binding.cityState.setText(CurrentCity(
                        gpsTracker.getLatitude()
                        , gpsTracker.getLongitude()));
            }
        }
    }
    public  String CurrentCity(double latitude,double longitude) {

        Log.e(TAG, "CurrentCity: "+ latitude);
        Log.e(TAG, "CurrentCity: "+ longitude);
        if (latitude>=0.0) {
            try {
            //List<Address> addresses =geocoder.getFromLocation(latitude, longitude, 1);
            Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses!=null) {
                Log.e(TAG, "CurrentCity:  addressesaddresses "+ addresses);
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
           //     String countryName = addresses.get(0).getAddressLine(2);

                // txt_paddress.setText(address);
                return cityName+","+stateName ;
            }
                //   txt_state.setText(stateName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  "  ";

    }

    private void setUpUi() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
       // binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Appointments"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
         binding.tabLayout.setSelected(true);
        adapter = new MyProfileFragment.Qr_DetailsAdapter(getActivity(),
                getChildFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public void selectLike(int position, String status) {
        addLike(postList.get(position).getId());
    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position) {
        showDialog(postID, position);
    }

    @Override
    public void savePost(View param1, String postID, boolean isUser, int position) {

    }

    public void showDialog(String postId, int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView tvDelete = dialog.findViewById(R.id.tvDelete);
        TextView tvShare = dialog.findViewById(R.id.tvShare);
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        if (userId.equalsIgnoreCase(postList.get(position).getUserId())) {
            tvDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.GONE);
        }

        tvShare.setOnClickListener(v1 ->
                {
                    dialog.dismiss();
                    String shareBody = "User :" + postList.get(position).getFirstName() + " " + postList.get(position).getLastName() + "\n\nPosted :" + postList.get(position).getImage();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    getActivity().startActivity(Intent.createChooser(sharingIntent, getActivity().getResources().getString(R.string.share_using)));
                }
        );

        tvDelete.setOnClickListener(v1 ->
                {
                    dialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.delete_post))
                            .setMessage(getString(R.string.are_you_sure_want_to_delete_post))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePost(postId);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void deletePost(String postId) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("post_id", postId);
        Call<SuccessResAddLike> call = apiInterface.deletePost(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data", data.status + "");
                    if (data.status == 1) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        getPosts();
                    } else if (data.status == 0) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void addLike(String postId) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("post_id", postId);
        Call<SuccessResAddLike> call = apiInterface.addLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data", data.status + "");
                    if (data.status == 1) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        getPosts();
                    } else if (data.status == 0) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
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

    public void chooseToEdit() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_choose_edit);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        RelativeLayout rlchangeCoverPhoto = dialog.findViewById(R.id.rrChangeCover);
        RelativeLayout rlEditProfile = dialog.findViewById(R.id.rrEditProfile);

        rlEditProfile.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), EditProfileAct.class));
                }
        );
        rlchangeCoverPhoto.setOnClickListener(v ->
                {
                    if (checkPermisssionForReadStorage()) {
                        dialog.dismiss();
                        showImageSelection();
                    }
                }
        );

        tvCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
//                    Glide.with(getActivity())
//                            .load(selectedImage)
//                            .centerCrop()
//                            .into(binding.ivCoverPhoto);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                    str_image_path = image;
                    updateCoverPhoto();
                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            } else if (requestCode == REQUEST_CAMERA) {

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(getActivity(), imageBitmap);
                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        str_image_path = image;
//                        Glide.with(getActivity())
//                                .load(imageBitmap)
//                                .centerCrop()
//                                .into(binding.ivCoverPhoto);

                        updateCoverPhoto();

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

    public void updateCoverPhoto() {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if (file != null) {
                filePart = MultipartBody.Part.createFormData("cover_image", file.getName(), RequestBody.create(MediaType.parse("cover_image/*"), file));
            } else {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);

        Call<SuccessResUploadCoverPhoto> loginCall = apiInterface.uploadCoverPhoto(userId, filePart);
        loginCall.enqueue(new Callback<SuccessResUploadCoverPhoto>() {
            @Override
            public void onResponse(Call<SuccessResUploadCoverPhoto> call, Response<SuccessResUploadCoverPhoto> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadCoverPhoto data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Test Response :" + responseString);

                    getProfile();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadCoverPhoto> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


}