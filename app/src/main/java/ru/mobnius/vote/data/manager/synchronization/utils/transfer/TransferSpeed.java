package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import ru.mobnius.vote.utils.StringUtil;

/**
 * Скорость передачи данных. Блоков chunk за время time
 */
public class TransferSpeed {
    private TransferSpeed(){}
    /**
     * размер блока
     */
    private long chunk;
    /**
     * время затраченное на обработку
     */
    private long time;

    public static TransferSpeed getInstance(long chunk, long time){
        TransferSpeed speed = new TransferSpeed();
        speed.chunk = chunk;
        speed.time = time == 0 ? 1 : time;
        return speed;
    }

    @Override
    public String toString() {
        return StringUtil.getSize((1000 * chunk) / time) + "\\сек.";
    }
}
