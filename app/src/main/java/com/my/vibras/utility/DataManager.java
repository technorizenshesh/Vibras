package com.my.vibras.utility;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.loader.content.CursorLoader;

import com.my.vibras.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataManager {

    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
            return ourInstance;
        }

        private DataManager() {
        }

        private Dialog mDialog;
        private boolean isProgressDialogRunning = false;
//        WP10ProgressBar progressBar;
        ProgressBar progressBar;

        public void showProgressMessage(Activity dialogActivity, String msg) {
            try {
                if (isProgressDialogRunning) {
                    hideProgressMessage();
                }
                isProgressDialogRunning = true;
                mDialog = new Dialog(dialogActivity);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.dialog_loading);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = mDialog.findViewById(R.id.textView);
                progressBar = mDialog.findViewById(R.id.progressBar);
                textView.setText(msg);
                WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
                lp.dimAmount = 0.8f;

                mDialog.getWindow().setAttributes(lp);
                mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void hideProgressMessage() {
            isProgressDialogRunning = true;
            try {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
/*

        public SignupModel getUserData(Context context) {
            SignupModel userData = new Gson().fromJson(SessionManager.readString(context, Constant.USER_INFO, ""), SignupModel.class);
            return userData;
        }
*/

        public static String getRealPathFromURI(Activity activity, Uri contentUri) {
            //TODO: get realpath from uri
            String stringPath = null;
            try {
                if (contentUri.getScheme().toString().compareTo("content") == 0) {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
                    Cursor cursor = loader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    stringPath = cursor.getString(column_index);
                    cursor.close();
                } else if (contentUri.getScheme().compareTo("file") == 0) {
                    stringPath = contentUri.getPath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stringPath;
        }

      /*  public static String resizeBase64Image(Bitmap image) {

       *//* if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }*//*

            image = Bitmap.createScaledBitmap(image, 150, 150, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            // byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);

            byte[] b = baos.toByteArray();
            //System.gc();
            return "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);

        }
*/

        public static String toBase64(String message) {
            byte[] data;
            try {
                data = message.getBytes("UTF-8");
                String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
                return base64Sms;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static String fromBase64(String message) {
            byte[] data = Base64.decode(message, Base64.DEFAULT);
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }

     public static String convertDateToString(long l) {
            String str = "";
            Date date = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
            str = dateFormat.format(date);
            return str;
        }

  /*
        public static boolean isValidEmail(CharSequence target) {
            return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
        }
        public static String getCurrent() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  hh:mm aa");
            String currentDateandTime = sdf.format(new Date());
            return currentDateandTime;
        }*/


    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public Bitmap getImageAngle(String photoPath, Bitmap rotatedBitmap) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);//ORIENTATION_UNDEFINED

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(rotatedBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(rotatedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(rotatedBitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = rotatedBitmap;
            }
            return rotatedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try{
                URL url = new URL(src);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return image;
            }
            catch (IOException e)
            {
                return null;
            }
    }


    /*public static void DatePicker(Context context, final DateSetListener listener) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                listener.SelectedDate(sdf.format(myCalendar.getTime()));
                RideOptionAct.date = sdf.format(myCalendar.getTime());
            }

        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();
    }


    public static void TimePicker(Context context, final DateSetListener listene) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, selectedHour);
                date.set(Calendar.MINUTE, selectedMinute);
                String time=new SimpleDateFormat("hh:mm aa").format(date.getTime());
                listene.SelectedDate(time);
                RideOptionAct.time = time;
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }*/



    }
