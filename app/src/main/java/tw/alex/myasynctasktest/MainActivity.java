package tw.alex.myasynctasktest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mesg;
    private MyAsyncTask myAsyncTask;
    private int[] imgIds = {R.id.img0,R.id.img1};
    private ImageView[] imgViews = new ImageView[imgIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg = findViewById(R.id.mesg);
        for (int i=0; i<imgViews.length; i++){
            imgViews[i] = findViewById(imgIds[i]);
        }
    }

    public void test1(View view) {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("https://d35fo82fjcw0y8.cloudfront.net/2018/07/10224839/Incontent_1.png","https://www.popsci.com/sites/popsci.com/files/styles/1000_1x_/public/images/2017/10/00-phone-apps.jpg?itok=ST2Dxe5y&fc=50,50");
    }

    public void test2(View view) {
        if (myAsyncTask != null){
            myAsyncTask.cancel(true);
        }

    }

    private class MyAsyncTask extends AsyncTask<String, Bitmap, String>{
        private int index;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("alex", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... urls) {

            String ret = "Ok";
            for (String url : urls) {
                Log.v("alex", ""+ url);
                try {

                    if(isCancelled()){
                        throw new Exception();
                    }
                    URL imgurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
                    conn.getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    publishProgress(bmp);
                }catch(Exception e){
                    Log.v("alex",e.toString());
                    ret = "cancel";
                    break;
                }

            }
            return ret;
        }

        @Override
        protected void onPostExecute(String mesg) {
            super.onPostExecute(mesg);
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Load Finish")
                    .show();
        }

        @Override
        protected void onProgressUpdate(Bitmap... bmps) {
            super.onProgressUpdate(bmps);
            imgViews[index].setImageBitmap(bmps[0]);
            index++;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("alex", "onCancelled");
        }

        @Override
        protected void onCancelled(String mesg) {
            super.onCancelled(mesg);
            Log.v("alex", "onCancelled()");
        }


    }
}
