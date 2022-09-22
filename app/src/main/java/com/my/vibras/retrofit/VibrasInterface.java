package com.my.vibras.retrofit;
import com.my.vibras.model.SuccessResAddEvent;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.model.SuccessResGetChat;
import com.my.vibras.model.SuccessResGetComment;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetInterest;
import com.my.vibras.model.SuccessResGetNotification;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetProfile;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetSubscription;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.model.SuccessResUploadCoverPhoto;
import com.my.vibras.model.SuccessResUploadPost;
import com.my.vibras.model.SuccessResUploadSelfie;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VibrasInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignup> signup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("check_otp")
    Call<SuccessResSignup> verifyOtp(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignup> login(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("addSelfy")
    Call<SuccessResUploadSelfie> uploadSelfie (
            @Part("user_id") RequestBody userId,
            @Part MultipartBody.Part file);

    @GET("getPassion")
    Call<SuccessResGetInterest> getPassion();

    @GET("getUserStory")
    Call<SuccessResGetStories> getStories();

    @FormUrlEncoded
    @POST("get_ProfilePic")
    Call<SuccessResSignup> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_Selfy")
    Call<SuccessResUploadSelfie> getProfileImage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<SuccessResSignup> changePassword(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("updateUserProfile")
    Call<SuccessResSignup> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("first_name") RequestBody cc,
                                                 @Part("last_name") RequestBody mobile,
                                                 @Part("email") RequestBody fullname,
                                                 @Part("bio") RequestBody lat,
                                                 @Part MultipartBody.Part file);

    @Multipart
    @POST("addUserCoverPic")
    Call<SuccessResUploadCoverPhoto> uploadCoverPhoto (
            @Part("user_id") RequestBody userId,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("getAllUsers")
    Call<SuccessResGetUsers> getAllUsers(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addProfileLikeUnlike")
    Call<SuccessResAddOtherProfileLike> addProfileLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getUserProlieData")
    Call<SuccessResGetProfile> getOtherProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_like")
    Call<SuccessResAddOtherProfileLike> addLikePost(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search")
    Call<SuccessResGetUsers> searchUser(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("addUserPost")
    Call<SuccessResUploadPost> uploadPost (
            @Part("user_id") RequestBody userId,
            @Part("description") RequestBody description,
            @Part("type") RequestBody comment,
            @Part("type_status") RequestBody type,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("getUserPOST")
    Call<SuccessResGetPosts> getPost(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getotheruserpost")
    Call<SuccessResGetPosts> getOtherUserPost(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_like")
    Call<SuccessResAddLike> addLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_notification")
    Call<SuccessResGetNotification> getNotification(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_User_post_comment")
    Call<ResponseBody> addComment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_post_comment")
    Call<SuccessResGetComment> getComments(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_neareast_friends")
    Call<SuccessResGetUsers> getFriendsList(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_post")
    Call<SuccessResAddLike> deletePost(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("insert_chat")
    Call<SuccessResInsertChat> insertImageVideoChat (
            @Part("sender_id") RequestBody senderId,
            @Part("receiver_id") RequestBody receiverId,
            @Part("chat_message") RequestBody chatMessage,
            @Part("type") RequestBody type
            );

    @FormUrlEncoded
    @POST("get_chat")
    Call<SuccessResGetChat> getChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_conversation")
    Call<SuccessResGetConversation> getConversation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("clear_chat")
    Call<SuccessResDeleteConversation> deleteConversation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("send_fire")
    Call<SuccessResDeleteConversation> addFireToOther(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_category")
    Call<SuccessResGetCategory> getEventsCategory(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_even")
    Call<SuccessResAddEvent> addEvent (@Part("user_id") RequestBody userId,
                                            @Part("event_name") RequestBody eName,
                                            @Part("address") RequestBody address,
                                            @Part("lat") RequestBody lat,
                                            @Part("lon") RequestBody lon,
                                            @Part("event_date") RequestBody date,
                                            @Part("event_start_time") RequestBody time1,
                                            @Part("event_end_time") RequestBody time2,
                                            @Part("description") RequestBody description,
                                            @Part("booking_amount") RequestBody mobile,
                                            @Part("type") RequestBody fullname,
                                            @Part("event_cat") RequestBody eventCategory,
                                            @Part MultipartBody.Part fileEvent,
                                            @Part List<MultipartBody.Part> file);

    @Multipart
    @POST("add_Restaurant")
    Call<SuccessResAddRestaurant> addRestaurants (@Part("user_id") RequestBody userId,
                                                  @Part("restaurant_name") RequestBody eName,
                                                  @Part("address") RequestBody address,
                                                  @Part("lat") RequestBody lat,
                                                  @Part("lon") RequestBody lon,
                                                  @Part("description") RequestBody description,
                                                  @Part MultipartBody.Part fileEvent,
                                                  @Part List<MultipartBody.Part> file);



    @FormUrlEncoded
    @POST("get_banner")
    Call<SuccessResGetBanner> getBanner(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_neareast_event")
    Call<SuccessResGetEvents> getEvents(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_neareast_restaurent")
    Call<SuccessResGetRestaurants> getRestaurnat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_subscription_plan")
    Call<SuccessResGetSubscription> getSubscription(@FieldMap Map<String, String> paramHashMap);


}