package com.my.vibras.retrofit;
import com.my.vibras.model.SuccessResAddCard;
import com.my.vibras.model.SuccessResAddEvent;
import com.my.vibras.model.SuccessResAddLike;
import com.my.vibras.model.SuccessResAddOtherProfileLike;
import com.my.vibras.model.SuccessResAddRestaurant;
import com.my.vibras.model.SuccessResAddRestaurantComment;
import com.my.vibras.model.SuccessResCompanyNotification;
import com.my.vibras.model.SuccessResDeleteCard;
import com.my.vibras.model.SuccessResDeleteConversation;
import com.my.vibras.model.SuccessResFilterData;
import com.my.vibras.model.SuccessResGetBanner;
import com.my.vibras.model.SuccessResGetCard;
import com.my.vibras.model.SuccessResGetCategory;
import com.my.vibras.model.SuccessResGetChat;
import com.my.vibras.model.SuccessResGetComment;
import com.my.vibras.model.SuccessResGetConversation;
import com.my.vibras.model.SuccessResGetEventComment;
import com.my.vibras.model.SuccessResGetEvents;
import com.my.vibras.model.SuccessResGetGroup;
import com.my.vibras.model.SuccessResGetGroupChat;
import com.my.vibras.model.SuccessResGetGroupData;
import com.my.vibras.model.SuccessResGetGroupDetails;
import com.my.vibras.model.SuccessResGetGroupRestaurantEventAmount;
import com.my.vibras.model.SuccessResGetHelp;
import com.my.vibras.model.SuccessResGetInterest;
import com.my.vibras.model.SuccessResGetMyStories;
import com.my.vibras.model.SuccessResGetNotification;
import com.my.vibras.model.SuccessResGetPP;
import com.my.vibras.model.SuccessResGetPosts;
import com.my.vibras.model.SuccessResGetProfile;
import com.my.vibras.model.SuccessResGetRestaurantComment;
import com.my.vibras.model.SuccessResGetRestaurants;
import com.my.vibras.model.SuccessResGetStories;
import com.my.vibras.model.SuccessResGetSubscription;
import com.my.vibras.model.SuccessResGetToken;
import com.my.vibras.model.SuccessResGetTransaction;
import com.my.vibras.model.SuccessResGetUsers;
import com.my.vibras.model.SuccessResInsertChat;
import com.my.vibras.model.SuccessResInsertGroupChat;
import com.my.vibras.model.SuccessResMakeCall;
import com.my.vibras.model.SuccessResMakePayment;
import com.my.vibras.model.SuccessResMyEventRes;
import com.my.vibras.model.SuccessResMyJoinedEvents;
import com.my.vibras.model.SuccessResMyRestaurantRes;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.model.SuccessResUpdateRate;
import com.my.vibras.model.SuccessResUploadCoverPhoto;
import com.my.vibras.model.SuccessResUploadPost;
import com.my.vibras.model.SuccessResUploadSelfie;
import com.my.vibras.model.SuccessResUploadStory;

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
    @POST("get_all_story")
    Call<SuccessResGetStories> getAllStories(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_ProfilePic")
    Call<SuccessResSignup> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResUpdateRate> getNotificationStatus(@FieldMap Map<String, String> paramHashMap);

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

    @FormUrlEncoded
    @POST("delete_group")
    Call<SuccessResAddLike> deleteGroup(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("insert_chat")
    Call<SuccessResInsertChat> insertImageVideoChat (
            @Part("sender_id") RequestBody senderId,
            @Part("receiver_id") RequestBody receiverId,
            @Part("chat_message") RequestBody chatMessage,
            @Part("type") RequestBody type
            );

    @Multipart
    @POST("insert_group_chat")
    Call<SuccessResInsertGroupChat> insertGroupImageVideoChat (
            @Part("sender_id") RequestBody senderId,
            @Part("group_id") RequestBody receiverId,
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
    @POST("update_lat_lon")
    Call<SuccessResDeleteConversation> updateLocation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_category")
    Call<SuccessResGetCategory> getEventsCategory(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_event")
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
    @POST("get_neareast_restaurent")
    Call<SuccessResMyRestaurantRes> getNearbyRestaurnat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_subscription_plan")
    Call<SuccessResGetSubscription> getSubscription(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("send_fire_like_love")
    Call<SuccessResAddOtherProfileLike> addFireLikeLove(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getUserEvents")
    Call<SuccessResMyEventRes> getMyEvents(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getUserRestaurent")
    Call<SuccessResMyRestaurantRes> getMyRestaurant(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addEventlike")
    Call<SuccessResAddLike> addEventLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addRestaurentlike")
    Call<SuccessResAddLike> addRestaurantLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_event")
    Call<SuccessResAddLike> deleteEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_restaurant")
    Call<SuccessResAddLike> deleteRestaurant(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addEventSave")
    Call<SuccessResAddLike> saveEventRestaurant(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addRestaurentcomment")
    Call<ResponseBody> addRestaurantComment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_restaurent_comment")
    Call<SuccessResGetRestaurantComment> getRestaurantComments(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("ge_saved_restaurent")
    Call<SuccessResMyRestaurantRes> getSavedRestaurant(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("ge_saved_event")
    Call<SuccessResMyEventRes> getSavedEvents(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_card")
    Call<SuccessResAddCard> addCard(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_card")
    Call<SuccessResGetCard> getCards(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_user_card")
    Call<SuccessResDeleteCard> deleteCard(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_event")
    Call<SuccessResMyEventRes> searchEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("addeventcomment")
    Call<ResponseBody> addEventComment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_event_comment")
    Call<SuccessResGetEventComment> getEventComments(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_account")
    Call<ResponseBody> deleteAccount(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_restaurant")
    Call<SuccessResMyRestaurantRes> searchRest(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_notification_status")
    Call<SuccessResUpdateRate> updateWorkerNoti(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_story")
    Call<SuccessResUploadStory> uploadStory (
            @Part("user_id") RequestBody userId,
            @Part("description") RequestBody description,
            @Part("story_type") RequestBody type,
            @Part List<MultipartBody.Part> file);

    @Multipart
    @POST("update_story")
    Call<SuccessResUploadStory> updateStory (
            @Part("user_id") RequestBody userID,
            @Part("story_id") RequestBody storyID,
            @Part("story_type") RequestBody type,
            @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("get_my_story")
    Call<SuccessResGetMyStories> getMyStory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("apply_filter")
    Call<SuccessResFilterData> filter(@FieldMap Map<String, String> paramHashMap);

/*    @FormUrlEncoded
    @POST("create_group")
    Call<ResponseBody> createGroup(@FieldMap Map<String, String> paramHashMap);*/
    @Multipart
    @POST("create_group")
    Call<ResponseBody> createGroup(
            @Part("user_id") RequestBody userId,
            @Part("group_name") RequestBody group_name,
            @Part("members_id") RequestBody members_id,
            @Part MultipartBody.Part file);
    @FormUrlEncoded
    @POST("get_all_group")
    Call<SuccessResGetGroup> getAllGroupApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_group")
    Call<SuccessResGetGroup> getMygroupApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_group_chat")
    Call<SuccessResGetGroupChat> getGroupChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_token")
    Call<SuccessResGetToken> getToken(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_purchase_subscription")
    Call<SuccessResGetGroupData> getGroup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("stripe_payment")
    Call<SuccessResMakePayment> makePayment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_payment_history")
    Call<SuccessResGetTransaction> getTransaction(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_group_amount")
    Call<SuccessResGetGroupRestaurantEventAmount> getGroupPurchasePrice(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getGroup")
    Call<SuccessResGetGroupDetails> getGroupDetails(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("join_group_request")
    Call<ResponseBody> joinGroup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_online_status")
    Call<SuccessResSignup> updateOnLIneStatus(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_event_by_category")
    Call<SuccessResGetEvents> getEventsByCategory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_help_faq")
    Call<SuccessResGetHelp> getHelp(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_privacy_policy")
    Call<SuccessResGetPP> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_about")
    Call<SuccessResGetPP> getAboutUs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("accepte_reject_group_request")
    Call<ResponseBody> acceptRejectGroup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("join_event")
    Call<ResponseBody> joinEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_my_join_event")
    Call<SuccessResMyJoinedEvents> getMyJoinedEvents(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("storylikeunlike")
    Call<ResponseBody> addStoryLike(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("video_call_invitation")
    Call<SuccessResMakeCall> addNotification(@FieldMap Map<String, String> paramHashMap);


}
