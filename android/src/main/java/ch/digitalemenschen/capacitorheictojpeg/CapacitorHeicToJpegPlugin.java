package ch.digitalemenschen.capacitorheictojpeg;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;

import android.graphics.Matrix;

import androidx.core.content.FileProvider;

import android.content.Intent;

@CapacitorPlugin(name = "CapacitorHeicToJpeg")
public class CapacitorHeicToJpegPlugin extends Plugin {

    private CapacitorHeicToJpeg implementation = new CapacitorHeicToJpeg();

    @PluginMethod
    public void convertToJpeg(PluginCall call) {
        String uriString = call.getString("path");
        if (uriString == null) {
            call.reject("Must provide a URI path");
            return;
        }

        Bitmap bitmap;
        Uri uri = Uri.parse(uriString);
        if (uriString.startsWith("content://")) {
            // Handle content URIs
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                call.reject("Unable to open image", e);
                return;
            }
        } else {
            // Handle file paths
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(uriString, options);
        }

        if (bitmap == null) {
            call.reject("Failed to decode bitmap");
            return;
        }

        // Rotate based on exif metadata.
        int rotation = getRotationFromExif(uri);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        // Convert and save the bitmap as JPEG
        // Convert and save the bitmap as JPEG in the cache directory with a random filename
        String fileName = "converted_" + System.currentTimeMillis() + ".jpg";
        String jpegPath = new File(getContext().getCacheDir(), fileName).getAbsolutePath();
        try {
            OutputStream out = new FileOutputStream(jpegPath);
            bitmap.compress(CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            if (uriString.startsWith("content://")) {
                File outputFile = new File(jpegPath);

                // Get content URI
                Uri contentUri = androidx.core.content.FileProvider.getUriForFile(
                        getContext(),
                        getContext().getPackageName() + ".fileprovider",
                        outputFile
                );

                // Grant temporary read permission to the content URI for the calling app
                getContext().grantUriPermission(
                        getContext().getPackageName(),
                        contentUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                // Return the content URI
                JSObject ret = new JSObject();
                ret.put("path", contentUri.toString());
                call.resolve(ret);
            } else {
                JSObject ret = new JSObject();
                ret.put("path", jpegPath);
                call.resolve(ret);
            }
        } catch (Exception e) {
            call.reject("Failed to save JPEG", e);
        }
    }

    private int getRotationFromExif(Uri imageUri) {
        ExifInterface exif;
        try {
            InputStream in = getContext().getContentResolver().openInputStream(imageUri);
            if (in == null) return 0;
            exif = new ExifInterface(in);
        } catch (IOException e) {
            return 0;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        return switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90 -> 90;
            case ExifInterface.ORIENTATION_ROTATE_180 -> 180;
            case ExifInterface.ORIENTATION_ROTATE_270 -> 270;
            default -> 0;
        };
    }
}
