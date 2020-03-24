package ru.mobnius.vote.ui.fragment.form;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.Date;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DocumentManager;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.data.DocumentUtil;
import ru.mobnius.vote.ui.fragment.data.IMeterReadingTextWatcher;
import ru.mobnius.vote.ui.fragment.data.IStringTextWatcher;
import ru.mobnius.vote.ui.fragment.photoGallery.GalleryFragment;
import ru.mobnius.vote.ui.fragment.photoGallery.GalleryListener;
import ru.mobnius.vote.ui.model.Meter;
import ru.mobnius.vote.utils.DateUtil;

public abstract class  BaseFormFragment extends BaseFragment
        implements IMeterReadingTextWatcher, IStringTextWatcher {

    private DocumentUtil mDocumentUtil;
    private GalleryListener mGalleryListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof GalleryListener) {
            mGalleryListener = (GalleryListener)context;
        }
    }

    /**
     * Получение даты ввода показаний
     * @return дата ввода показания
     */
    protected String getCurrentDate() {
        DocumentUtil serializable = getDocumentUtil();
        if (serializable.getDate() != null) {
            return DateUtil.convertDateToUserString(serializable.getDate(),DateUtil.USER_SHORT_FORMAT);
        } else {
            return "undefined";
        }
    }

    /**
     * Получение даты пред. ввода показаний
     * @return дата ввода показания
     */
    protected String getPrevDate() {
        DocumentUtil serializable = getDocumentUtil();
        if (serializable.getPrevDate() != null) {
            Date datePrev = serializable.getPrevDate();
            if (datePrev == null) {
                return "undefined";
            }
            return DateUtil.convertDateToUserString(datePrev, DateUtil.USER_SHORT_FORMAT);
        } else {
            return "undefined";
        }
    }

    protected DocumentUtil getDocumentUtil() {
        return mDocumentUtil;
    }

    protected void setDocumentUtil(DocumentUtil documentUtil) {
        mDocumentUtil = documentUtil;
    }

    /**
     * Обработчик изменения показаний
     * @param meterId идентификатор показания
     * @param prevValue пред. значение показания
     * @param value значение показания
     */
    @Override
    public void afterMeterReadingTextChanged(String meterId, Double prevValue, Double value) {
        getDocumentUtil().updateMeterReading(meterId, value);
    }

    /**
     * Изменение текстового поля
     * @param id идентификатор
     * @param prevValue пред. значение
     * @param value значение
     */
    @Override
    public void afterStringTextChanged(String id, String prevValue, String value) {
        getDocumentUtil().updateValue(id, value);
    }

    /**
     * Сохранение показаний
     * @param documentManager управление документами
     * @param serverMeters показания с сервера
     * @param userPointId иден. пользов. точки
     */
    protected void saveMeters(DocumentManager documentManager, Meter[] serverMeters, String userPointId) {
        DocumentUtil.MeterReading[] meters = getDocumentUtil().getMeters();

        for (int i = 0; i < meters.length; i++) {
            String outputMeterReadingsId = serverMeters[i].getOutputMeterReadingsId();
            if (outputMeterReadingsId != null && !outputMeterReadingsId.isEmpty()) {
                documentManager.updateOutputMeterReadings(outputMeterReadingsId, meters[i].getValue());
            } else {
                documentManager.createOutputMeterReadings(
                        serverMeters[i].getInputMeterReadingsId(),
                        userPointId,
                        meters[i].getValue());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public GalleryListener getGalleryListener() {
        return mGalleryListener;
    }

    /**
     * Вывести фотогалерею
     * @param pointId инден. точки маршрута
     * @param result результат
     */
    protected void toGallery(String pointId, Results result) {
        GalleryFragment galleryFragment;
        if (result != null) {
            String resultId = result.id;
            galleryFragment = GalleryFragment.updateInstance(resultId);
        } else {
            galleryFragment = GalleryFragment.createInstance(pointId);
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.single_fragment_container, galleryFragment)
                .addToBackStack(null)
                .commit();
    }
}
