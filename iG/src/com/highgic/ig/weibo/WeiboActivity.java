package com.highgic.ig.weibo;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.highgic.ig.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;

/**
 * 
 * @author liyan (liyan9@staff.sina.com.cn)
 */
public class WeiboActivity extends Activity {

    public static final String TAG = "WeiboActivity";

    private Weibo mWeibo;
    private static final String CONSUMER_KEY = "466249499";
    // 966056985替换为开发者的appkey，例如"1646212860";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static Oauth2AccessToken accessToken;
    private Button sendBtn, authBtn, cancelBtn;
    private TextView mText;
    private EditText mEditText;

    /**
     * SsoHandler 仅当sdk支持sso时有效，
     */
    SsoHandler mSsoHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo);
        
        
        mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
        
        authBtn = (Button) findViewById(R.id.auth_button);// 触发sso的按钮
        try {
            Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
            authBtn.setVisibility(View.VISIBLE);
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found");

        }


        authBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 下面两个注释掉的代码，仅当sdk支持sso时有效，
                 */

                mSsoHandler = new SsoHandler(WeiboActivity.this, mWeibo);
                mSsoHandler.authorize(new AuthDialogListener());
            }
        });

        mEditText = (EditText) findViewById(R.id.weibo_edittext);
        
        
        
        sendBtn = (Button) findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                
                if (accessToken!=null) {
                    String weiboString =   mEditText.getText().toString();
                    
                    StatusesAPI api = new StatusesAPI(accessToken);
                    api.update(weiboString, "90", "-90", new RequestListener() {

                        @Override
                        public void onIOException(IOException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @Override
                        public void onError(WeiboException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @Override
                        public void onComplete(String response) {
                            Log.i(TAG, response);
                            Util.showToast(WeiboActivity.this, response);

                        }
                    });
                  
                }else {
                    Log.i(TAG, "没有登录！");
                    Util.showToast(WeiboActivity.this, "没有登录！");
                }
                
               
                
            }
        });
        
        
        cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessTokenKeeper.clear(WeiboActivity.this);
                authBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.INVISIBLE);
                mEditText.setVisibility(View.INVISIBLE);
                sendBtn.setVisibility(View.INVISIBLE);
                
                mText.setText("");
            }
        });

        mText = (TextView) findViewById(R.id.show_textview);
        WeiboActivity.accessToken = AccessTokenKeeper.readAccessToken(this);
        if (WeiboActivity.accessToken.isSessionValid()) {
            Weibo.isWifi = Utility.isWifi(this);
           
            authBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.VISIBLE);
            
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date(WeiboActivity.accessToken.getExpiresTime()));
            mText.setText("access_token 仍在有效期内,无需再次登录: \naccess_token:" + WeiboActivity.accessToken.getToken() + "\n有效期：" + date);
        } else {
            mText.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_weibo, menu);
        return true;
    }

    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            WeiboActivity.accessToken = new Oauth2AccessToken(token, expires_in);
            if (WeiboActivity.accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(WeiboActivity.accessToken.getExpiresTime()));
                mText.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: " + expires_in + "\r\n有效期：" + date);

               
                cancelBtn.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);
                mEditText.setVisibility(View.VISIBLE);
                authBtn.setVisibility(View.INVISIBLE);
                
                AccessTokenKeeper.keepAccessToken(WeiboActivity.this, accessToken);
                Toast.makeText(WeiboActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 下面两个注释掉的代码，仅当sdk支持sso时有效，
         */
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
