package ru.mobnius.vote.ui.fragment.photoGallery;

import ru.mobnius.vote.data.manager.PhotoManager;

/**
 * Интерфейс для работы с галереей
 */
public interface GalleryListener {
    /**
     * Обработчик получения служебного объекта по работе с изображениями
     * @return служебный объект по работе с изображением
     */
    PhotoManager getPhotoManager();

    /**
     * Обработчик вызова камеры
     */
    void onCamera();

    /**
     * Сохранение данных на форме
     */
    void onSave();

    /**
     * Уничтожние галереи
     * Требуется вызывать при уничтожение activity
     */
    void onDestroyGallery();
}
