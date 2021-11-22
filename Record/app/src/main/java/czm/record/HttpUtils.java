package czm.record;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {

    private static volatile HttpUtils instance;
    private OkHttpClient client = new OkHttpClient();
    private String url = "http://inpluslab.com/paperwriting2019/multifile.php";

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if(null == instance) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void syncRecordToCloud(Context context, File gps, File usage, Callback callback){
        if (Utils.getIMEI(context) != null) {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("IMEI", Utils.getIMEI(context));
            builder.addFormDataPart("gps", gps.getName(), RequestBody.create(mediaType, gps));
            builder.addFormDataPart("usage", usage.getName(), RequestBody.create(mediaType, usage));
            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }

}
