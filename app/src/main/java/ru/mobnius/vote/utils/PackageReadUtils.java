package ru.mobnius.vote.utils;


import ru.mobnius.vote.data.manager.packager.FileBinary;
import ru.mobnius.vote.data.manager.packager.MetaSize;
import ru.mobnius.vote.data.manager.packager.PackageUtil;

/**
 * утилита для работы с пакетами синхронизаций
 * Чтение информации из пакета
 */
public class PackageReadUtils {
    private byte[] all;
    private final boolean isZip;

    /**
     * конструктор
     * @param bytes массив байтов
     */
    public PackageReadUtils(byte[] bytes, boolean isZip){
        all = bytes;
        this.isZip = isZip;
    }

    /**
     * чтение информации о пакете
     * @return объект с данными
     */
    public MetaSize getMetaSize() throws Exception {
        return PackageUtil.readSize(all);
    }

    /**
     * чтени мета информации
     * @return метаинформация пакета
     */
    public ru.mobnius.vote.data.manager.packager.MetaPackage getMeta() throws Exception {
        return PackageUtil.readMeta(all, isZip);
    }

    /**
     * чтение блока с бинарными данными
     * @return массив данных
     */
    public FileBinary[] getFiles() throws Exception {
        return PackageUtil.readBinaryBlock(all, isZip).getFiles();
    }

    /**
     * Получение файла по имени
     * @param name имя
     * @return возарщается файл
     */
    public FileBinary getFile(String name) throws Exception {
        FileBinary[] files = getFiles();
        for(FileBinary file : files){
            if(file.name.equals(name)){
                return file;
            }
        }
        return null;
    }

    /**
     * Чтение информации о переданных данных
     * @return запросы
     */
    public ru.mobnius.vote.data.manager.rpc.RPCItem[] getTo() throws Exception {
        return PackageUtil.readStringBlock(all, isZip).to;
    }

    /**
     * Чтение информации о данных которые были приняты от сервера
     * @param zip использовать сжатие
     * @return запросы
     */
    public ru.mobnius.vote.data.manager.rpc.RPCResult[] getResultTo(boolean zip) throws Exception {
        return PackageUtil.readResultStringBlock(all, "to", zip);
    }

    /**
     * получение информации, котрую дал сервер
     * @return запросы
     */
    public ru.mobnius.vote.data.manager.rpc.RPCItem[] getFrom() throws Exception {
        return PackageUtil.readStringBlock(all, isZip).from;
    }

    /**
     * Чтение информации о данных которые были приняты от сервера
     * @param zip использовать сжатие
     * @return запросы
     */
    public ru.mobnius.vote.data.manager.rpc.RPCResult[] getResultFrom(boolean zip) throws Exception {
        return PackageUtil.readResultStringBlock(all, "from", zip);
    }

    /**
     * очистка объекта
     */
    public void destroy(){
        all = null;
    }
}
