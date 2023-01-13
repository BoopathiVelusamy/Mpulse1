package mahendra.school.mpulse1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Manish on 10/2/2016.
 */

public class FilePath {

    static String getUriRealPath(Context ctx , Uri uri) throws FileNotFoundException {

        InputStream inputStream = ctx.getContentResolver().openInputStream(uri);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(ctx.getFilesDir(), "a/b/");
        storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".pdf",         // suffix
                    storageDir      // directory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("our file", image.toString());
        writeFile(inputStream, image);
        String filePath = image.getAbsolutePath();
        return filePath;
    }

    static void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}

