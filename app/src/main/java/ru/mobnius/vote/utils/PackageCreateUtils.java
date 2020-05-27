package ru.mobnius.vote.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.packager.MetaPackage;
import ru.mobnius.vote.data.manager.packager.MetaSize;
import ru.mobnius.vote.data.manager.packager.StringBlock;
import ru.mobnius.vote.data.manager.zip.ZipResult;
import ru.mobnius.vote.data.manager.zip.ZipManager;

/**
 * утилита для работы с пакетами синхронизаций
 * Создание пакета
 */
public class PackageCreateUtils {
    private ArrayList<ru.mobnius.vote.data.manager.rpc.RPCItem> to;
    private ArrayList<ru.mobnius.vote.data.manager.rpc.RPCItem> from;
    private final boolean isZip;

    /**
     * очитка объекта
     */
    public void destroy(){
        to.clear();
        to = null;
        from.clear();
        from = null;
    }

    public PackageCreateUtils(boolean isZip){
        to = new ArrayList<>();
        from = new ArrayList<>();
        this.isZip = isZip;
    }

    /**
     * добавляет информацию, которую нужно передать на сервер
     * @param to запрос
     * @return текущий объект
     */
    public PackageCreateUtils addTo(ru.mobnius.vote.data.manager.rpc.RPCItem to){
        this.to.add(to);
        return this;
    }

    /**
     * добавляет информацию, которую нужно получить от сервера
     * @param from запрос
     * @return текущий объект
     */
    public PackageCreateUtils addFrom(ru.mobnius.vote.data.manager.rpc.RPCItem from){
        this.from.add(from);
        return this;
    }

    /**
     * создание пакета
     * @param tid идентификатор пакета
     * @param dataInfo тип пакета
     * @return возвращается массив байтов
     */
    private byte[] generatePackage(String tid, @SuppressWarnings("SameParameterValue") String dataInfo) throws IOException {
        String stringBlock = new StringBlock(this.to.toArray(new ru.mobnius.vote.data.manager.rpc.RPCItem[0]), this.from.toArray(new ru.mobnius.vote.data.manager.rpc.RPCItem[0])).toJsonString();

        byte[] stringBlockBytes;
        if(isZip){
            ZipResult zipResult = ZipManager.compress(stringBlock);
            stringBlockBytes = zipResult.getCompress();
        }else{
            stringBlockBytes = stringBlock.getBytes();
        }

        MetaPackage meta = new MetaPackage(tid);
        meta.stringSize = stringBlockBytes.length;
        meta.dataInfo = dataInfo;
        meta.transaction = true;
        meta.version = PreferencesManager.SYNC_PROTOCOL;

        byte[] mBytes;
        if(isZip){
            ZipResult zipResult = ZipManager.compress(meta.toJsonString());
            mBytes = zipResult.getCompress();
        }else{
            mBytes = meta.toJsonString().getBytes();
        }

        MetaSize ms = new MetaSize(mBytes.length, MetaSize.CREATED, isZip ? ZipManager.getMode() : "NML");

        byte[] metaBytes = ms.toJsonString().getBytes();
        byte[] allByteArray = new byte[metaBytes.length + mBytes.length + stringBlockBytes.length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(metaBytes);
        buff.put(mBytes);
        buff.put(stringBlockBytes);

        return buff.array();
    }

    /**
     * создание пакета
     * @return возвращается массив байтов
     */
    public byte[] generatePackage(String tid) throws IOException {
        return generatePackage(tid, "");
    }
}
