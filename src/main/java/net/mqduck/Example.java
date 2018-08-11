package net.mqduck;

import net.mqduck.jinputpoller.Event;
import net.mqduck.jinputpoller.JInputListener;
import net.mqduck.jinputpoller.JInputPoller;

import javax.swing.*;

public class Example extends JFrame implements JInputListener {
    @Override
    public void stateChanged(Event event) {
        String deviceType = "";
        switch(event.deviceType) {
            case DeviceType.JOYSTICK:
                deviceType = "joystick";
                break;
            case DeviceType.KEYBOARD:
                deviceType = "keyboard";
                break;
            case DeviceType.MOUSE:
                deviceType = "mouse";
                break;
        }
        System.out.println(String.format("%s %d component %s changed to %f",
                deviceType,
                event.deviceIndex,
                event.component,
                event.value));
    }

    @Override
    public int getDeviceTypes() {
        return DeviceType.JOYSTICK | DeviceType.KEYBOARD | DeviceType.MOUSE;
    }

    public Example() {
        setSize(100, 100);
        setTitle("JInputPoller Example");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JInputPoller jiPoller = new JInputPoller();
        jiPoller.start();
        jiPoller.addListener(this);
    }

    public static void main(String[] args) {
        new Example().setVisible(true);
    }
}
