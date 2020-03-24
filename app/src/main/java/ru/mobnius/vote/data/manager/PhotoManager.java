package ru.mobnius.vote.data.manager;

import com.google.gson.annotations.Expose;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.ui.model.Image;

public class PhotoManager implements Serializable {
    /**
     * Временные изображения, пока еще не хранятся в БД
     */
    private List<Image> mTempImages;

    /**
     * Изображения
     */
    private List<Image> mImages;

    /**
     * конструктор
     */
    public PhotoManager() {
        mTempImages = new ArrayList<>(2);
        mImages = new ArrayList<>(2);
    }

    /**
     * Запись изображений по умолчанию
     *
     * @param images изображения, Image
     */
    public void addPictures(Image[] images) {
        mImages.addAll(Arrays.asList(images));
    }

    /**
     * Обновление изображений по умолчанию
     *
     * @param images изображения, Image
     */
    public void updatePictures(Image[] images) {
        mImages.clear();
        if (images != null) {
            mImages.addAll(Arrays.asList(images));
        }
    }

    /**
     * Добавить временного изображение
     *
     * @param image Image
     */
    public void addTempPicture(Image image) throws Exception {
        boolean exists = findImage(image.getName()) != null;
        if (!exists) {
            mTempImages.add(image);
        } else {
            throw new Exception("Изображение с именем " + image.getName() + " уже существует. ");
        }
    }

    /**
     * Обновление изображения
     *
     * @param dataManager объект управления данными
     * @param image       Image
     * @param type        тип, AttachamentTypes
     * @param notice      примечание
     */
    public void updatePicture(DataManager dataManager, Image image, long type, String notice) {
        Image img = findImage(image.getName());
        if (img != null) {
            img.update(dataManager, type, notice);
        }
    }

    /**
     * Удаление изображения
     *
     * @param dataManager объект управления данными
     * @param fileManager объект управления файлами
     * @param image       Image
     */
    public void deletePicture(DataManager dataManager, FileManager fileManager, Image image) {
        if (isTempImage(image)) {
            try {
                // TODO: 21/01/2020 может это нужно убрать?
                fileManager.deleteFile(FileManager.CACHES, image.getName());
            } catch (FileNotFoundException ignored) {

            }
            mTempImages.remove(image);
        } else {
            dataManager.removeAttachment(image.getId());
            mImages.remove(image);
        }
    }

    /**
     * Сохранение изображений
     *
     * @param dataManager объект управления данными
     * @param fileManager объект управления файлами
     * @param resultId    идентификтаор результата
     */
    public void savePictures(DataManager dataManager, FileManager fileManager, String resultId) throws IOException, ParseException {
        for (Image image : mTempImages) {
            image.setResultId(resultId);
            Attachments attachment = dataManager.saveAttachment(image.getName(), image.getType(), resultId, image.getNotice(), image.getLocation(), image.getBytes());
            mImages.add(Image.getInstance(dataManager, attachment));
        }

        clearTempImage(fileManager);

        for (Image image : mImages) {
            if (image.isChanged()) {
                image.update(dataManager, image.getType(), image.getNotice());
            }

            image.resetChange();
        }
    }

    /**
     * Получение списка изображение, отсортировано по дате
     *
     * @return список изображенией, Image
     */
    public Image[] getImages() {

        List<Image> images = new ArrayList<>(4);
        images.addAll(mTempImages);
        images.addAll(mImages);

        Collections.sort(images, new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                return Long.compare(o2.getDate().getTime(), o1.getDate().getTime());
            }
        });

        return images.toArray(new Image[0]);
    }

    /**
     * Поиск изображения по имени
     *
     * @param name имя файла, Image.getName()
     * @return Результат поиска
     */
    public Image findImage(String name) {
        Image[] images = getImages();
        for (Image image : images) {
            if (image.getName().equals(name)) {
                return image;
            }
        }
        return null;
    }

    /**
     * Является ли изображение временным. Не было еще сохранено в БД
     *
     * @param image изображение, Image
     * @return true - является временным
     */
    public boolean isTempImage(Image image) {
        for (Image img : mTempImages) {
            if (image.getName().equals(img.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * были изменения
     *
     * @return true - были изменения
     */
    public boolean isChanged() {
        boolean isChanged = false;
        for (Image image : mImages) {
            isChanged = image.isChanged();
            if (isChanged) {
                break;
            }
        }
        return isChanged || mTempImages.size() > 0;
    }

    /**
     * Очистка данных
     *
     * @param fileManager объект управления файлами
     */
    public void destroy(FileManager fileManager) {
        clearTempImage(fileManager);
        mImages.clear();
    }

    public void clearTempImage(FileManager fileManager) {
        for (Image image : mTempImages) {
            try {
                fileManager.deleteFile(FileManager.CACHES, image.getName());
                fileManager.deleteFile(FileManager.TEMP_PICTURES, image.getName());
            } catch (FileNotFoundException ignored) {

            }
        }
        mTempImages.clear();
    }
}
