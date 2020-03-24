package ru.mobnius.vote.data.manager.mail;

import ru.mobnius.vote.data.storage.models.MobileDevices;

public class DeviceMail extends BaseMail {
    public DeviceMail(MobileDevices mobileDevices) {
        super();

        mGroup = "manager";
        this.mDevice = mobileDevices;
    }

    public MobileDevices mDevice;
}
