package ru.mobnius.vote.data.manager.packager;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.zip.ZipManager;
import ru.mobnius.vote.data.manager.rpc.RPCResult;

/**
 * Объект для обработки мета информации пакета
 */
public class PackageUtil {

    /**
     * чтение массива байтов для чтения информации
     * @param bytes данные
     * @return информация о чтении мета информации
     */
    public static MetaSize readSize(byte[] bytes) throws Exception {
        if(bytes.length < MetaSize.MAX_LENGTH)
            throw new Exception("Длина пакета меньше " + MetaSize.MAX_LENGTH);
        StringBuilder str = new StringBuilder();
        for(int i = 0; i< MetaSize.MAX_LENGTH; i++){
            byte[] temp = new byte[1];
            temp[0] = bytes[i];
            str.append(new String(temp));
        }
        int status;
        String type;
        try {
            type = str.substring(0, 3);
            status = Integer.parseInt(str.substring(MetaSize.MAX_LENGTH - 1));
        }catch (Exception e){
            throw new Exception("Ошибка чтения статуса пакета: " + str + " (символ "+ MetaSize.MAX_LENGTH+")");
        }
        String sizeStr = str.substring(3, MetaSize.MAX_LENGTH -1).replace(".", "");
        int size;
        try {
            size = Integer.parseInt(sizeStr);
        }catch (Exception e){
            throw new Exception("Ошибка чтения длины мета информации: " + sizeStr);
        }
        return new MetaSize(size, status, type);
    }

    /**
     * Чтение метаинформации
     * @param bytes масиив байтов - данные
     * @return объект с метаинформацией
     */
    public static MetaPackage readMeta(byte[] bytes, boolean zip) throws Exception {
        MetaSize ms = readSize(bytes);
        int size = ms.metaSize;
        int length = size + MetaSize.MAX_LENGTH;

        byte[] temp = Arrays.copyOfRange(bytes, MetaSize.MAX_LENGTH, length);
        String str = getString(temp, zip);
        return new Gson().fromJson(str, MetaPackage.class);
    }

    /**
     * Чтение блока со строковыми данными
     * @param bytes массив байтов
     * @return объект с информацией о запросах
     */
    public static StringBlock readStringBlock(byte[] bytes, boolean zip) throws Exception {
        MetaSize metaSize = PackageUtil.readSize(bytes);
        MetaPackage aPackage = PackageUtil.readMeta(bytes, zip);
        int start = MetaSize.MAX_LENGTH + metaSize.metaSize;
        int end = start + aPackage.stringSize;
        byte[] temp = Arrays.copyOfRange(bytes, start, end);

        return new Gson().fromJson(getString(temp, zip), StringBlock.class);
    }

    /**
     * Чтение блока со строковыми данными
     * @param bytes массив байтов
     * @return объект с информацией о запросах
     */
    public static String readString(byte[] bytes, boolean zip) throws Exception {
        MetaSize metaSize = PackageUtil.readSize(bytes);
        MetaPackage aPackage = PackageUtil.readMeta(bytes, zip);
        int start = MetaSize.MAX_LENGTH + metaSize.metaSize;
        int end = start + aPackage.stringSize;
        byte[] temp = Arrays.copyOfRange(bytes, start, end);

        return getString(temp, zip);
    }

    /**
     * Чтение блока со строковыми данными
     * @param bytes массив байтов
     * @param blockName иям блока для обработки
     * @param zip архивирование
     * @return объект с информацией о запросах
     */
    public static RPCResult[] readResultStringBlock(byte[] bytes, String blockName, boolean zip) throws Exception {
        MetaSize metaSize = PackageUtil.readSize(bytes);
        MetaPackage aPackage = PackageUtil.readMeta(bytes, zip);
        int start = MetaSize.MAX_LENGTH + metaSize.metaSize;
        int end = start + aPackage.stringSize;
        byte[] temp = Arrays.copyOfRange(bytes, start, end);

        JSONObject obj = new JSONObject(getString(temp, zip));
        return RPCResult.createInstance(obj.getString(blockName));
    }

    /**
     * Чтение блока с бинарными данными
     * @param bytes массив байтов
     * @param zip архивирование
     * @return объект с информацией о вложених
     */
    public static BinaryBlock readBinaryBlock(byte[] bytes, boolean zip) throws Exception {
        MetaSize metaSize = PackageUtil.readSize(bytes);
        MetaPackage aPackage = PackageUtil.readMeta(bytes, zip);

        int start = MetaSize.MAX_LENGTH + metaSize.metaSize + aPackage.stringSize;
        int end = start + aPackage.binarySize;

        BinaryBlock binaryBlock = new BinaryBlock();

        if(start == end)
            return binaryBlock;
        else{
            byte[] temp = Arrays.copyOfRange(bytes, start, end);
            MetaAttachment[] attachments = aPackage.attachments;
            int idx = 0;
            for (MetaAttachment attachment : attachments) {
                byte[] t = new byte[attachment.size];
                System.arraycopy(temp, idx, t, 0, attachment.size);
                idx += attachment.size;
                binaryBlock.add(attachment.name, attachment.key, t);
            }

            return binaryBlock;
        }
    }

    /**
     * обновление статуса пакета
     * @param bytes масиив байтов
     * @param status статус пакета
     * @return Обновленный массив данных
     */
    public static byte[] updateStatus(byte[] bytes, int status) throws Exception {
        if(bytes.length < MetaSize.MAX_LENGTH)
            throw new Exception("Длина пакета меньше " + MetaSize.MAX_LENGTH);

        bytes[MetaSize.MAX_LENGTH -1] = String.valueOf(status).getBytes()[0];

        return bytes;
    }

    /**
     * Получение строки из массива байтов
     * @param temp масиив байтов
     * @param zip применялось ли сжатие
     * @return строка
     */
    public static String getString(byte[] temp, boolean zip) {
        String str;

        if(zip) {
            try {
                byte[] zipBytes = ZipManager.decompress(temp);
                if(zipBytes != null) {
                    str = new String(zipBytes);
                } else {
                    str = new String(temp);
                }
            }catch (IOException | DataFormatException e) {
                Logger.error("Ошибка распаковки строкового блока.", e);
                str = new String(temp);
            }
        } else {
            str = new String(temp);
        }

        return str;
    }
}
