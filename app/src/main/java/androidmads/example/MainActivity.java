package androidmads.example;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtValue;
    private ImageView qrImage;
    private String inputValue;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    private static final String TAG = "log";
    SharedPreferences sharedPreferences;
    MyTask myTask = new MyTask();

    private String mJSONURLString = "https://myrik8333.github.io/t.json";
    //inputValue=mJSONURLString;
  //  private String mJSONURLString2 = "https://myrik8333.github.io/piter.json";
   // private String mJSONURLString3 = "https://myrik8333.github.io/kazan.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTask.execute();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
                    Request.Method.GET,
                    mJSONURLString,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            qrImage = findViewById(R.id.qr_image);
                            //edtValue = findViewById(R.id.edt_value);
                          //  activity = this;
                            inputValue="12345678901231123";

                            Log.d(TAG, "sfdfsdf");
                            try {
                                final JSONObject object = new JSONObject(response.toString());
                                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                Display display = manager.getDefaultDisplay();
                                Point point = new Point();
                                display.getSize(point);
                                int width = point.x;
                                int height = point.y;
                                int smallerDimension = width < height ? width : height;
                                smallerDimension = smallerDimension * 3 / 4;
                                qrgEncoder = new QRGEncoder(
                                        object.toString(), null,
                                        QRGContents.Type.TEXT,
                                        smallerDimension);
                                qrgEncoder.setColorBlack(Color.RED);
                                qrgEncoder.setColorWhite(Color.BLUE);
                                try {
                                    bitmap = qrgEncoder.getBitmap();
                                    qrImage.setImageBitmap(bitmap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("responce", object.toString());
                                Log.d(TAG, object.toString());
                                edit.apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);

            return null;
        }

      /*  @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // В связи с тем что ответ приходит мнгновенно, пришлось создать временную задержку в 3 секунды для
            // демонстрации работающего ProgressBar-а

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(StartActivity.this, ActivityBalls3.class);
                    startActivity(intent);
                    //dialog.dismiss();
                }
            }, 500);
        }*/
    }



}
