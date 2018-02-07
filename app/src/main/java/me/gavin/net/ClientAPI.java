package me.gavin.net;

import com.google.gson.JsonArray;

import io.reactivex.Observable;
import me.gavin.app.account.User;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
     * *********************************** 知乎日报 ******************************** *
     * **************************************************************************** */


    /* **************************************************************************** *
     * *********************************** 账号 ********************************** *
     * **************************************************************************** */

    @FormUrlEncoded
    @POST("user/register")
    Observable<Result<Object>> register(@Field("account") String account, @Field("pwd") String pwd);

    @GET("user/login")
    Observable<Result<User>> login(@Query("account") String account, @Query("pwd") String pwd);

    @GET("user/getUserInfo")
    Observable<Result<User>> getUserInfo(@Query("account") String account);


    /* **************************************************************************** *
     * *********************************** 设置 ************************************ *
     * **************************************************************************** */

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Headers("Cache-Control: max-stale=2419200")
    @GET("https://raw.githubusercontent.com/gavinxxxxxx/Sensual/master/json/license.json")
    Observable<JsonArray> getLicense();
}
