package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.utils.FileUtil;

/**
 * Работа с файловой системой
 */
public class FileManager {
    public static final String APP_NAME = "Mobnius";
    /**
     * папка для хранение вложений
     */
    public static final String ATTACHMENTS = "attachments";

    /**
     * папка для хранение файлов
     */
    public static final String FILES = "files";

    /**
     * папка для временных изображений
     */
    public static final String TEMP_PICTURES = "temp";

    /**
     * папка для хранения миниатюрок
     */
    public static final String CACHES = "thumbs";

    private final BasicCredentials credentials;
    private static FileManager fileManager;
    private Context context;

    /**
     * Конструктор
     * @param context контекст
     * @param credentials информация о пользователе
     */
    private FileManager(BasicCredentials credentials, Context context) {
        this.credentials = credentials;
        this.context = context;
    }

    /**
     * Создание экземпляра файлового менеджера
     *
     * @param credentials авторизация
     * @param context контекст
     * @return Объект для работы с файловой системой
     */
    public static FileManager createInstance(BasicCredentials credentials, Context context) {
        return fileManager = new FileManager(credentials, context);
    }

    /**
     * получение текущего экземпляра
     *
     * @return Объект для работы с файловой системой
     */
    public static FileManager getInstance() {
        return fileManager;
    }

    private File getRootCatalog(String folder) {
        return new File(FileUtil.getRoot(context, Environment.DIRECTORY_PICTURES), APP_NAME + "/" + credentials.login + "/" + folder);
    }

    /**
     * Каталог с вложениями
     *
     * @return возвращается путь к папке
     */
    public File getAttachmentsFolder() {
        return getRootCatalog(ATTACHMENTS);
    }

    /**
     * Каталог с файлами
     *
     * @return возвращается путь к папке
     */
    public File getFilesFolder() {
        return getRootCatalog(FILES);
    }

    /**
     * каталог для хранения временных файлов изображений
     *
     * @return возвращается путь к папке
     */
    public File getTempPictureFolder() {
        return getRootCatalog(TEMP_PICTURES);
    }

    /**
     * каталог для хранения кэшированных изображений
     *
     * @return возвращается путь к папке
     */
    public File getCaches() {
        return getRootCatalog(CACHES);
    }

    /**
     * Запись байтов в файловую систему
     *
     * @param folder   папка
     * @param fileName имя файла
     * @param bytes    массив байтов
     * @throws IOException исключение
     */
    public void writeBytes(String folder, String fileName, byte[] bytes) throws IOException {

        File dir = getRootCatalog(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);

        FileOutputStream outputStream = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        bos.write(bytes, 0, bytes.length);
        bos.flush();
        bos.close();
    }

    /**
     * Чтение информации о файле
     *
     * @param folder   папка
     * @param fileName имя файла
     * @return возвращается массив байтов
     * @throws IOException исключение
     */
    public byte[] readPath(String folder, String fileName) throws IOException {
        File dir = getRootCatalog(folder);
        File file = new File(dir, fileName);
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            return buf.toByteArray();
        } else {
            return null;
        }
    }

    /**
     * Доступен ли файл
     *
     * @param folder   папка
     * @param fileName имя файла
     * @return возвращается доступен ли файл
     */
    public boolean exists(String folder, String fileName) {
        File dir = getRootCatalog(folder);
        File file = new File(dir, fileName);
        return file.exists();
    }

    /**
     * удаление файла
     *
     * @param folder   имя папки
     * @param fileName имя файла
     * @throws FileNotFoundException исключение при отсуствие директории или файла
     */
    public void deleteFile(String folder, String fileName) throws FileNotFoundException {
        File dir = getRootCatalog(folder);
        if (!dir.exists()) {
            throw new FileNotFoundException("Корневая директория " + folder + " не найдена.");
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            deleteRecursive(file);
        } else {
            throw new FileNotFoundException("Файл " + fileName + " в директории " + folder + " не найден.");
        }
    }

    /**
     * очистка папки
     *
     * @param folder папка
     */
    public void deleteFolder(String folder) throws FileNotFoundException {
        File dir = getRootCatalog(folder);
        if (dir.exists()) {
            deleteRecursive(dir);
        } else {
            throw new FileNotFoundException("Директория " + folder + " не найдена.");
        }
    }

    /**
     * удаление объекта File
     *
     * @param fileOrDirectory файл или директория
     */
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    /**
     * удаление пользовательской папки
     */
    public void clearUserFolder() {
        File dir = new File(Environment.getExternalStorageDirectory(), credentials.login);
        deleteRecursive(dir);
    }

    public void destroy() {
        fileManager = null;
    }
}
