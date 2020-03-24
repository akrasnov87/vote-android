package ru.mobnius.vote.data.manager;

import java.nio.ByteBuffer;
import java.util.UUID;

import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.mail.DeviceMail;
import ru.mobnius.vote.data.manager.mail.GeoMail;
import ru.mobnius.vote.data.manager.mail.StringMail;
import ru.mobnius.vote.data.manager.packager.MetaPackage;
import ru.mobnius.vote.data.manager.packager.MetaSize;

public class MailManager {

    public static byte[] send(GeoMail geoMail) {

        byte[] bytes = geoMail.toJsonString().getBytes();
        String tid = UUID.randomUUID().toString();

        MetaPackage meta = new MetaPackage(tid);
        meta.stringSize = bytes.length;
        meta.dataInfo = "geo";
        meta.version = PreferencesManager.MAILER_PROTOCOL;

        byte[] mBytes = meta.toJsonString().getBytes();

        MetaSize ms = new MetaSize(mBytes.length, MetaSize.CREATED, "NML");

        byte[] metaBytes = ms.toJsonString().getBytes();
        byte[] allByteArray = new byte[metaBytes.length + mBytes.length + bytes.length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(metaBytes);
        buff.put(mBytes);
        buff.put(bytes);

        return buff.array();
    }

    public static byte[] send(DeviceMail deviceMail) {

        byte[] bytes = deviceMail.toJsonString().getBytes();
        String tid = UUID.randomUUID().toString();

        MetaPackage meta = new MetaPackage(tid);
        meta.stringSize = bytes.length;
        meta.dataInfo = "device";
        meta.version = PreferencesManager.MAILER_PROTOCOL;

        byte[] mBytes = meta.toJsonString().getBytes();

        MetaSize ms = new MetaSize(mBytes.length, MetaSize.CREATED, "NML");

        byte[] metaBytes = ms.toJsonString().getBytes();
        byte[] allByteArray = new byte[metaBytes.length + mBytes.length + bytes.length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(metaBytes);
        buff.put(mBytes);
        buff.put(bytes);

        return buff.array();
    }

    public static byte[] send(String body, String to, String group) {

        StringMail stringMail = new StringMail(body);
        stringMail.mTo = to;
        stringMail.mGroup = group;

        byte[] bytes = stringMail.toJsonString().getBytes();
        String tid = UUID.randomUUID().toString();

        MetaPackage meta = new MetaPackage(tid);
        meta.stringSize = bytes.length;
        meta.dataInfo = "mail";
        meta.version = PreferencesManager.MAILER_PROTOCOL;

        byte[] mBytes = meta.toJsonString().getBytes();

        MetaSize ms = new MetaSize(mBytes.length, MetaSize.CREATED, "NML");

        byte[] metaBytes = ms.toJsonString().getBytes();
        byte[] allByteArray = new byte[metaBytes.length + mBytes.length + bytes.length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(metaBytes);
        buff.put(mBytes);
        buff.put(bytes);

        return buff.array();
    }
}
