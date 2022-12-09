package com.my.vibras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.my.vibras.act.PaymentsAct;
import com.my.vibras.act.SearchConversationUserAct;
import com.my.vibras.adapter.AddFriendAdapter;
import com.my.vibras.adapter.ConversationSearchAdapter;
import com.my.vibras.adapter.SelectedUserAdapter;
import com.my.vibras.databinding.ActivityCreateGroupBinding;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;
import com.my.vibras.utility.RealPathUtil;
import com.my.vibras.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.USER_ID;
import static com.my.vibras.retrofit.Constant.showToast;

public class CreateGroupAct extends AppCompatActivity implements AddFriendAdapter.OnItemClickListener {
    private static final int SELECT_FILE = 2;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    ActivityCreateGroupBinding binding;
    private VibrasInterface apiInterface;

    private ArrayList<SuccessResGetUsers.Result> conversation = new ArrayList<>();
    private ArrayList<SuccessResGetUsers.Result> searhList = new ArrayList<>();

    private ArrayList<SuccessResGetUsers.Result> selectedUserList = new ArrayList<>();
    private AddFriendAdapter mAdapter;
    private SelectedUserAdapter selectedUserAdapter;
    String usersIds="";
    private static final int MY_PERMISSION_CONSTANT = 5;
    boolean cameraClicked = true;
    CircleImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_create_group);
       apiInterface = ApiClient.getClient().create(VibrasInterface.class);
       binding.RRFrnd.setOnClickListener(v -> finish());
       binding.ivReferesh.setOnClickListener(v ->
               {
                 getAllUsers();
                 binding.etSearch.setText("");
               }
               );

        mAdapter = new AddFriendAdapter(CreateGroupAct.this,searhList,CreateGroupAct.this);
        binding.rvFrnd.setHasFixedSize(true);
        binding.rvFrnd.setLayoutManager(new LinearLayoutManager(CreateGroupAct.this));
        binding.rvFrnd.setAdapter(mAdapter);
        getAllUsers();

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searhList.clear();

                for (SuccessResGetUsers.Result result:conversation)
                {
                    if(result.getFirstName().contains(s.toString()))
                    {
                        searhList.add(result);
                    }
                }

                mAdapter.notifyDataSetChanged();

            }
        });

        selectedUserAdapter = new SelectedUserAdapter(CreateGroupAct.this,
                selectedUserList, (view, position, model) -> {
                    selectedUserList.remove(position);
                    selectedUserAdapter.notifyDataSetChanged();
                });
       binding.btnCreate.setOnClickListener(v ->
               {
                   if(selectedUserList.size()>0)
                   {
                       createGroup();
                   }
               }
               );
       binding.rvSelectedUser.setHasFixedSize(true);
       binding.rvSelectedUser.setLayoutManager(new LinearLayoutManager(CreateGroupAct.this,LinearLayoutManager.HORIZONTAL,false));
       binding.rvSelectedUser.setAdapter(selectedUserAdapter);
    }

    private void createGroup()
    {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
         usersIds = userId+",";
        for (SuccessResGetUsers.Result result:selectedUserList)
        {
            usersIds = usersIds + result.getId()+",";
        }
        usersIds = usersIds.substring(0, usersIds.length() -1);
        final Dialog dialog = new Dialog(CreateGroupAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_group_name);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        AppCompatButton btnCreate = dialog.findViewById(R.id.btnCreate);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
         ivProfile = dialog.findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v ->
            {
                final CharSequence[] options = {"Take Video", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupAct.this);
                builder.setTitle("Add Video!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            cameraClicked = true;
                            if (checkPermisssionForReadStorage()) {
                                
                                openCamera();
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            cameraClicked = false;
                            if (checkPermisssionForReadStorage()) {
                               
                                    getPhotoFromGallary();
                                
                            }
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        );
        btnCreate.setOnClickListener(v ->
                {
                    if(edtEmail.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(CreateGroupAct.this, "Please enter group name.", Toast.LENGTH_SHORT).show();
                    }else if(str_image_path.equalsIgnoreCase(""))
                    {
                        Toast.makeText(CreateGroupAct.this, "Please Pick group Image.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        dialog.dismiss();
                        startActivity(new Intent(CreateGroupAct.this,PaymentsAct.class)
                                .putExtra("memberIds",usersIds)
                                .putExtra("groupName",edtEmail.getText().toString())
                                .putExtra("str_image_path",str_image_path)
                                .putExtra("from","group")
                        );
//                        createGroupApi(usersIds,edtEmail.getText().toString());
                    }
                }
                );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

   /* private void createGroupApi(String userIds,String groupName)
    {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreateGroupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("group_name",groupName);
        map.put("members_id",userIds);
        map.put("group_image","");

        Call<ResponseBody> call = apiInterface.createGroup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        finish();

                    } else if (data.equals("0")) {
                        showToast(CreateGroupAct.this,message);
                    }
                } catch (Exception e) {

                    Log.d("TAG", "onResponse: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }*/

    private void getAllUsers() {
        String userId = SharedPreferenceUtility.getInstance(CreateGroupAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(CreateGroupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat","22.7196");
        map.put("lon","75.8577");
        Call<SuccessResGetUsers> call = apiInterface.getFriendsList(map);
        call.enqueue(new Callback<SuccessResGetUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetUsers> call, Response<SuccessResGetUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUsers data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        searhList.clear();
                        searhList.addAll(data.getResult());
                        conversation.clear();
                        conversation.addAll(data.getResult());
mAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(CreateGroupAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, SuccessResGetUsers.Result model) {
        boolean found = false;
        if(selectedUserList.size()==0)
        {
            selectedUserList.add(searhList.get(position));
            selectedUserAdapter.notifyDataSetChanged();
        } else
        {
            for (SuccessResGetUsers.Result result:selectedUserList)
            {
                String id1 = result.getId();
                String id2 = searhList.get(position).getId();
                if(id1.equalsIgnoreCase(id2))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                selectedUserList.add(searhList.get(position));
                selectedUserAdapter.notifyDataSetChanged();
            } else
            {
                Toast.makeText(CreateGroupAct.this, "User already added.", Toast.LENGTH_SHORT).show();
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
        if (cameraIntent.resolveActivity(CreateGroupAct.this.getPackageManager()) != null)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }
    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(CreateGroupAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(CreateGroupAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(CreateGroupAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateGroupAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(CreateGroupAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(CreateGroupAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {

                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.
                                READ_EXTERNAL_STORAGE, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission
                                .READ_EXTERNAL_STORAGE, 
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
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
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateGroupAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(CreateGroupAct.this, "Permission denied", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreateGroupAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateGroupAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");

             if (requestCode == SELECT_FILE) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(CreateGroupAct.this)
                            .load(selectedImage)
                            .centerCrop()
                            .into(ivProfile);
                    Uri tempUri = getImageUri(CreateGroupAct.this, bitmap);
                    String image = RealPathUtil.getRealPath(CreateGroupAct.this, tempUri);
                    str_image_path = image;

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            } else if (requestCode == REQUEST_CAMERA) {

                try {
                    if (data != null) {
                        // TODO
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(CreateGroupAct.this, imageBitmap);
                        String image = RealPathUtil.getRealPath(CreateGroupAct.this, tempUri);
                        str_image_path = image;
                        Glide.with(CreateGroupAct.this)
                                .load(imageBitmap)
                                .centerCrop()
                                .into(ivProfile);
//                        updateCoverPhoto();
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
}