package ru.mobnius.vote.data.manager.camera;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.IProgressListener;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.camera.CameraUtil;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.utils.BitmapUtil;
import ru.mobnius.vote.utils.StreamUtil;

/**
 * класс для управления камерой в заданиях
 */
public class CameraManager {
    public static final int REQUEST_CODE_PHOTO = 2;

    private CompressAsync compressAsync;
    private IProgressListener listener;

    private final Activity context;
    /**
     * Качество сохраняемого изображения
     */
    private int quality;
    private String fileName;
    private Uri imageUri;
    File output;

    /**
     * имя файла
     *
     * @return Возвращается имя файла
     */
    private String getFileName() {
        return fileName;
    }

    /**
     * Конструктор
     *
     * @param context Текущее активити
     */
    public CameraManager(Activity context) {
        this.context = context;
    }

    /**
     * Вызыв камеры
     *
     * @param quality качество камеры от 0 до 100, где 100 - это максимальное качество
     */
    public void open(int quality) {
        this.quality = quality;
        fileName = System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = FileManager.getInstance().getTempPictureFolder();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            output = new File(dir, System.currentTimeMillis() + ".jpg");
            imageUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    output);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            context.startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    /**
     * Обработчик результата камеры
     *
     * @param resultCode статус код
     * @param callback   функция обратного вызова
     * @throws Exception исключение при сохранении изображения или обработке
     */
    public void processing(int resultCode, IProgressListener callback) throws Exception {
        listener = callback;
        if (resultCode == Activity.RESULT_OK) {
            InputStream iStream = context.getContentResolver().openInputStream(imageUri);

            compressAsync = new CompressAsync();
            compressAsync.execute(iStream);

        } else {
            throw new Exception("Вызов камеры был отменен. RequestCode=" + resultCode);
        }
    }

    public void destroy() {
        if (compressAsync != null) {
            if (!compressAsync.isCancelled()) {
                compressAsync.cancel(true);
            }
            compressAsync = null;
        }
    }

    class CompressAsync extends AsyncTask<InputStream, Void, byte[]> {

        /**
         * событие о том что произошла ошибка
         */
        private boolean isMistake = false;

        @Override
        protected byte[] doInBackground(InputStream... inputStreams) {
            byte[] bytes = CameraUtil.compress(inputStreams[0], CameraUtil.WEBP_IMAGE_FORMAT, quality);
            isMistake = bytes == null;
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);

            if (listener != null) {
                String fileName = null;
                String errorText = "Ошибка при сжатии изображения";
                if (isMistake) {
                    listener.onError(new Exception(errorText + " " + getFileName()));
                } else {
                    try {
                        FileManager fileManager = FileManager.getInstance();
                        fileName = getFileName();
                        CameraUtil.saveDataFromCamera(fileManager, fileName, bytes);
                    } catch (IOException e) {
                        listener.onError(new Exception(errorText + " " + getFileName() + ". " + e.getMessage()));
                    }
                }

                listener.onDone(fileName, bytes);
            }
        }
    }
}
