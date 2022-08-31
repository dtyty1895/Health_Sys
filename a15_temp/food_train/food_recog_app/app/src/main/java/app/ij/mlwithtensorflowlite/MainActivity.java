/*
 * Created by ishaanjav
 * github.com/ishaanjav
 */

package app.ij.mlwithtensorflowlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import app.ij.mlwithtensorflowlite.ml.SaveM;


public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            SaveM model = SaveM.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            SaveM.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            final String conf = String.valueOf(maxConfidence * 100).substring(0,5);
            String[] classes = {"肉圓", "牛肉麵", "台南牛肉湯", "鹹蛋苦瓜", "白菜滷", "滷肉飯", "黑糖糕",
                    "珍珠奶茶", "草仔粿", "香菇雞湯", "醃小黃瓜", "棺材板", "涼麵", "紅蟳米糕", "排骨酥湯",
                    "雞排", "魷魚絲", "蛋餅", "八寶冰", "砂鍋魚頭", "鱔魚意麵", "炒泡麵", "炒米粉", "土魠魚羹麵",
                    "薑母鴨", "烤玉米", "烤香腸", "客家小炒","酸辣湯", "洪瑞珍三明治", "大腸／蚵仔麵線", "鐵蛋",
                    "雞腳凍", "肉乾", "宮保雞丁", "滷味", "芒果冰", "紅油抄手", "虱目魚粥", "麻糬", "綠豆沙牛奶",
                    "羊肉炒麵", "羊肉爐", "鍋燒意麵", "牛排", "牛軋糖", "蚵嗲", "蚵仔煎", "木瓜牛奶", "花生糖",
                    "胡椒餅", "豬血湯", "鳳梨酥", "五更腸旺", "鍋貼", "皮蛋豆腐", "粽子", "小卷米粉", "爌肉飯",
                    "烤番薯", "黑輪", "鹹酥雞", "牛角麵包", "蔥爆牛肉", "蔥抓餅", "蝦仁炒蛋", "番茄炒蛋",
                    "海鮮粥", "麻油雞", "蝦仁飯", "四神湯", "刈包", "麻辣鴨血", "豆酥鱈魚", "芋頭糕", "水煎包",
                    "滷豬腳", "臭豆腐", "炒海瓜子", "魷魚羹", "鴨肉湯", "絲瓜蛤蜊湯", "薑絲炒大腸", "拔絲地瓜",
                    "太陽餅", "糖醋排骨", "地瓜球", "潤餅", "貢丸湯", "大腸包小腸", "糖葫蘆", "湯圓", "芋圓",
                    "三杯雞", "筒仔米糕", "火雞肉飯", "蘿蔔糕", "麻花捲", "車輪餅", "小籠包", "蛋黃酥"};

            if(0 <= maxPos && maxPos <=100)
                result.setText(classes[maxPos] + "     " + conf + '%');
            else
                result.setText("無法辨識");
            //result.setText(maxPos);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}