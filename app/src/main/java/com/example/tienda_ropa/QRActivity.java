package com.example.tienda_ropa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.OutputStream;

public class QRActivity extends AppCompatActivity {

    private ImageView qrImageView;
    private TextView textSubtotal, textIGV, textTotal;
    private Button btnDescargarQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrImageView = findViewById(R.id.qrImageView);
        textSubtotal = findViewById(R.id.textSubtotal);
        textIGV = findViewById(R.id.textIGV);
        textTotal = findViewById(R.id.textTotal);
        btnDescargarQR = findViewById(R.id.btnDescargarQR);

        String subtotalStr = getIntent().getStringExtra("subtotal");
        String igvStr = getIntent().getStringExtra("igv");
        String totalStr = getIntent().getStringExtra("total");

        double subtotal = Double.parseDouble(subtotalStr);
        double igv = Double.parseDouble(igvStr);
        double total = Double.parseDouble(totalStr);

        textSubtotal.setText("Subtotal: S/ " + String.format("%.2f", subtotal));
        textIGV.setText("IGV: S/ " + String.format("%.2f", igv));
        textTotal.setText("Total: S/ " + String.format("%.2f", total));

        String qrContent = "000201010211393262b2ccea97f35f9f8318783dfbe3ceb05204561153036045802PE5906YAPERO6004Lima630470AE";
        generarQR(qrContent);

        btnDescargarQR.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    tomarCapturaPantalla();
                }
            } else {
                tomarCapturaPantalla();
            }
        });
    }

    private void generarQR(String contenido) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            int size = 512;
            BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, size, size);

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            qrImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void tomarCapturaPantalla() {
        try {
            View rootView = getWindow().getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);

            String filename = "QR_" + System.currentTimeMillis() + ".png";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QRDescargados");
            } else { // Android 9 o inferior
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/QRDescargados";
                values.put(MediaStore.Images.Media.DATA, path + "/" + filename);
            }

            ContentResolver resolver = getContentResolver();
            OutputStream outputStream = resolver.openOutputStream(
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            if (outputStream != null) {
                outputStream.close();
            }

            Toast.makeText(this, "QR guardado en la galería", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarCapturaPantalla();
            } else {
                Toast.makeText(this, "Permiso denegado para guardar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
