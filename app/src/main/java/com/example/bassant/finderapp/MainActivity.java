package com.example.bassant.finderapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    EditText userName ;
    EditText password ;
    EditText email;
    EditText mobile ;
    User user;
    Button button;
    TextView login;
    int f = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email) ;
        mobile = (EditText)findViewById(R.id.mobile);
        button = (Button)findViewById(R.id.signup);
        login = (TextView)findViewById(R.id.login) ;

        String userNameString = userName.getText().toString();
        String passwordString = password.getText().toString();
        String emailString = email.getText().toString();
        String mobileString = mobile.getText().toString();

        user.setUserName(userNameString);
        user.setMobile(mobileString);
        user.setEmail(emailString);
        user.setPassword(passwordString);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute("http://alexmorsi.pythonanywhere.com/api/users/signup/");
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!validateUserName(userName.getText().toString())) {
                    userName.setError("User name should consists of  2 words of only letters");
                    f=0;

                }
                else {
                    f =f+1;
                }


                if(!validateEmail(email.getText().toString())) {
                    email.setError("Email format should be : xxx@xxx.com");
                    f=0;

                }
                else {
                    f =f+1;
                }



                if(mobile.getText().length() < 11 ||mobile.getText().length() > 11){
                    mobile.setError("Mobile number should equals to 11 numbers");
                    f=0;

                }
                else {
                    f =f+1;
                }

                if ( f==3){

                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
                    httpAsyncTask.execute("http://alexmorsi.pythonanywhere.com/api/users/signup/");

                }
                else {
                    Toast.makeText(getBaseContext(),"Please check the errors!",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }


    //Return true if UserName is valid and false if UserName is invalid
    protected boolean validateUserName(String UserName) {
        String UsernamePattern = "[a-zA-Z]+";
        Pattern pattern = Pattern.compile(UsernamePattern);
        Matcher matcher = pattern.matcher(UserName);

        return matcher.matches();
    }

    //Return true if email is valid and false if email is invalid
    protected boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static String POST(String url, User user){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userName", user.getUserName());
            jsonObject.accumulate("password", user.getPassword());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("mobile", user.getMobile());


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
           Log.i("json",json);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());

           result =responseBody;

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
       /* private Context mContext;

        public HttpAsyncTask (Context context){
            mContext = context;
        }
       */
        @Override
        protected  void onPreExecute ()
        {
            user = new User();
            userName = (EditText)findViewById(R.id.userName);
            password = (EditText)findViewById(R.id.password);
            email = (EditText)findViewById(R.id.email) ;
            mobile = (EditText)findViewById(R.id.mobile);
            //button = (Button)findViewById(R.id.button);

            String userNameString = userName.getText().toString();
            String passwordString = password.getText().toString();
            String emailString = email.getText().toString();
            String mobileString = mobile.getText().toString();

            user.setUserName(userNameString);
            user.setMobile(mobileString);
            user.setEmail(emailString);
            user.setPassword(passwordString);


        }
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0],user);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.i("Response",result);
            //Toast.makeText(getBaseContext(),"Data Sent!",Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsonobject = new JSONObject(result);
                String uN = jsonobject.getString("userName");
                // Log.i("userName", uN);
                String p = jsonobject.getString("password");
                String e = jsonobject.getString("email");
                String m = jsonobject.getString("mobile");

                if(uN!=null)
                {
                    if (p == null)
                    {
                        Toast.makeText(getBaseContext(),"user with this userName already exists, please enter another userName",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra("username", uN);
                        MainActivity.this.startActivity(intent);
                    }
                }

                else {
                    Toast.makeText(getBaseContext(),"Error in signup, please check the inputs!",Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}


