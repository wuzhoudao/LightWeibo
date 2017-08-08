package com.jack.lightweibo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Message msg  = new Message();

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthInfo = new AuthInfo(MainActivity.this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
        // SSO 授权, 仅Web
        findViewById(R.id.obtain_token_via_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorizeWeb(new AuthListener());
            }
        });

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(final Bundle values) {
            // 从 Bundle 中解析 Token
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                    //从这里获取用户输入的 电话号码信息
                    String  phoneNum =  mAccessToken.getPhoneNum();
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        updateTokenView(false);

                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                        Toast.makeText(MainActivity.this,"授权成功"
                              /*  R.string.weibosdk_demo_toast_auth_success*/, Toast.LENGTH_SHORT).show();
                    } else {
                        // 以下几种情况，您会收到 Code：
                        // 1. 当您未在平台上注册的应用程序的包名与签名时；
                        // 2. 当您注册的应用程序包名与签名不正确时；
                        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                        String code = values.getString("code");
                        String message = "授权失败！";//getString(R.string.weibosdk_demo_toast_auth_failed);
                        if (!TextUtils.isEmpty(code)) {
                            message = message + "\nObtained the code: " + code;
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this,
                    "取消授权"/*R.string.weibosdk_demo_toast_auth_canceled*/, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(MainActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }
}
