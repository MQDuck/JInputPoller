package net.mqduck.jinputpoller;

public interface JInputListener {
    void stateChanged(Event event);
    int getDeviceTypes();

    public class DeviceType {
        public static final int
                KEYBOARD = 1,
                MOUSE    = 2,
                JOYSTICK = 4;

        private DeviceType() {}
    }
}
