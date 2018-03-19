package me.gavin.net;

import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.account.User;
import me.gavin.app.contact.Contact;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {


    /* **************************************************************************** *
     * *********************************** 账号 ************************************ *
     * **************************************************************************** */

    @FormUrlEncoded
    @POST("user/register")
    Observable<Result<Object>> register(@Field("account") String account, @Field("pwd") String pwd);

    @GET("user/login")
    Observable<Result<User>> login(@Query("account") String account, @Query("pwd") String pwd);

    @GET("user/info")
    Observable<Result<User>> getUserInfo(@Query("account") String account);

    @PATCH("user/nickname")
    Observable<Result> updateName(@Query("nickName") String value);

    @PATCH("user/signature")
    Observable<Result> updateSign(@Query("signature") String value);

    @GET("user/logout")
    Observable<Result> logout();


    /* **************************************************************************** *
     * *********************************** 通讯录 ********************************** *
     * **************************************************************************** */

    @GET("friend/search/{query}")
    Observable<Result<List<Contact>>> queryContact(@Path("query") String query);

    @POST("friend/{id}")
    Observable<Result> applyContact(@Path("id") long fid);

    @PATCH("friend/pass/{id}")
    Observable<Result> passContact(@Path("id") long fid);

    @GET("friends")
    Observable<Result<List<Contact>>> getContacts();


    /* **************************************************************************** *
     * *********************************** 设置 ************************************ *
     * **************************************************************************** */

    @FormUrlEncoded
    @POST("upload/image")
    Observable<Result> uploadImage(@Field("file") String imgBase64);

    @POST("upload/file")
    Observable<Result> uploadFile(@Field("file") String base64);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Headers("Cache-Control: max-stale=2419200")
    @GET("https://raw.githubusercontent.com/gavinxxxxxx/Sensual/master/json/license.json")
    Observable<JsonArray> getLicense();
}
