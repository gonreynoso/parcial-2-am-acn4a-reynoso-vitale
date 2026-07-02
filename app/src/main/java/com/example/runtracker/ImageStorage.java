package com.example.runtracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ImageStorage {

    private static final String FILE_NAME = "profile.jpg";
    private static final int MAX_SIZE_PX = 512;
    private static final int JPEG_QUALITY = 85;

    private ImageStorage() {
    }

    
    public static String saveProfilePhoto(Context context, Uri source) throws IOException {
        Bitmap decoded = decodeSampled(context, source);
        if (decoded == null) {
            throw new IOException("No se pudo leer la imagen");
        }
        Bitmap scaled = scaleDown(decoded);
        File file = new File(context.getFilesDir(), FILE_NAME);
        try (FileOutputStream out = new FileOutputStream(file)) {
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out);
        }
        return file.getAbsolutePath();
    }

    public static Bitmap load(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return BitmapFactory.decodeFile(path);
    }

    private static Bitmap decodeSampled(Context context, Uri source) throws IOException {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        try (InputStream in = context.getContentResolver().openInputStream(source)) {
            BitmapFactory.decodeStream(in, null, bounds);
        }

        int sample = 1;
        int halfWidth = bounds.outWidth / 2;
        int halfHeight = bounds.outHeight / 2;
        while (halfWidth / sample >= MAX_SIZE_PX && halfHeight / sample >= MAX_SIZE_PX) {
            sample *= 2;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sample;
        try (InputStream in = context.getContentResolver().openInputStream(source)) {
            return BitmapFactory.decodeStream(in, null, options);
        }
    }

    private static Bitmap scaleDown(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        float ratio = Math.min((float) MAX_SIZE_PX / width, (float) MAX_SIZE_PX / height);
        if (ratio >= 1f) {
            return src;
        }
        return Bitmap.createScaledBitmap(src, Math.round(width * ratio), Math.round(height * ratio), true);
    }
}
