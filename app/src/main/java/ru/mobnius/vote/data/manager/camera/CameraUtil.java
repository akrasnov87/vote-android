package ru.mobnius.vote.data.manager.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.utils.BitmapUtil;

public class CameraUtil {
    public static final String WEBP_IMAGE_FORMAT = "webp";
    public static final String JPEG_IMAGE_FORMAT = "jpeg";
    /**
     * Максимальная высота изображения для сохранения в БД
     */
    public static int MAX_IMAGE_HEIGHT = BitmapUtil.QUALITY_1080p;

    /**
     * сжатие изображения
     * @param inputStream поток
     * @param imageFormat формат изображения WEBP_IMAGE_FORMAT или JPEG_IMAGE_FORMAT
     * @param quality качество сжатия от о до 100
     * @return сжатие данные
     */
    public static byte[] compress(InputStream inputStream, String imageFormat, int quality) {
        try {
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            Bitmap resizeBmp = BitmapUtil.scaleToFitWidth(bmp, MAX_IMAGE_HEIGHT);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            switch (imageFormat) {
                default:
                case JPEG_IMAGE_FORMAT:
                    resizeBmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                    break;

                case WEBP_IMAGE_FORMAT:
                    resizeBmp.compress(Bitmap.CompressFormat.WEBP, quality, bos);
                    break;
            }

            return bos.toByteArray();
        }catch (Exception e){
            Logger.error("Ошибка сжатия изображения.", e);
            return null;
        }
    }

    /**
     * сохранение результата после фотографирования
     * @param fileManager менеджер файлов
     * @param fileName имя файла
     * @param bytes данные
     * @throws IOException исключение
     */
    public static void saveDataFromCamera(FileManager fileManager, String fileName, byte[] bytes) throws IOException {
        fileManager.writeBytes(FileManager.TEMP_PICTURES, fileName, bytes);
        fileManager.writeBytes(FileManager.CACHES, fileName, BitmapUtil.cacheBitmap(bytes, BitmapUtil.IMAGE_QUALITY, BitmapUtil.QUALITY_120p)); // тут создаем кэш
    }
}
