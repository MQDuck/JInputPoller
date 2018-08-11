package net.mqduck.jinputpoller;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.EventQueue;

import java.util.ArrayList;
import java.util.HashSet;

public class JInputPoller {
    private ArrayList<Controller> keyboards;
    private ArrayList<Controller> mice;
    private ArrayList<Controller> joysticks;
    private HashSet<JInputListener> keyboardListeners;
    private HashSet<JInputListener> mouseListeners;
    private HashSet<JInputListener> joystickListeners;
    private boolean polling = false;

    public JInputPoller(int deviceTypes) {
        init(deviceTypes);
    }

    public JInputPoller() {
        init(
                JInputListener.DeviceType.JOYSTICK
                        | JInputListener.DeviceType.KEYBOARD
                        | JInputListener.DeviceType.MOUSE
        );
    }

    private void init(int deviceTypes) {
        if((deviceTypes & JInputListener.DeviceType.KEYBOARD) != 0) {
            keyboards = new ArrayList<Controller>();
            keyboardListeners = new HashSet<JInputListener>();
        }
        if((deviceTypes & JInputListener.DeviceType.MOUSE) != 0) {
            mice = new ArrayList<Controller>();
            mouseListeners = new HashSet<JInputListener>();
        }
        if((deviceTypes & JInputListener.DeviceType.JOYSTICK) != 0) {
            joysticks = new ArrayList<Controller>();
            joystickListeners = new HashSet<JInputListener>();
        }

        Controller[] devices = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for(Controller dev : devices) {
            if(dev.getType() == Controller.Type.KEYBOARD
                    && keyboards != null
                    && dev.getName().toLowerCase().contains("keyboard"))
                keyboards.add(dev);
            else if(dev.getType() == Controller.Type.MOUSE && mice != null)
                mice.add(dev);
            else if(dev.getType() == Controller.Type.STICK && joysticks != null)
                joysticks.add(dev);
        }
    }

    public void start() {
        if(        (keyboards == null || keyboards.isEmpty())
                && (mice == null || mice.isEmpty())
                && (joysticks == null || joysticks.isEmpty()) ) {
            polling = false;
            return;
        }

        polling = true;
        new Thread(new Runnable() {
            public void run() {
                while (polling) {
                    JInputPoller.this.pollDevices(keyboards, keyboardListeners, JInputListener.DeviceType.KEYBOARD);
                    JInputPoller.this.pollDevices(mice, mouseListeners, JInputListener.DeviceType.MOUSE);
                    JInputPoller.this.pollDevices(joysticks, joystickListeners, JInputListener.DeviceType.JOYSTICK);

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        polling = false;
    }

    private void pollDevices(ArrayList<Controller> devices, HashSet<JInputListener> listeners, int deviceType) {
        if(devices == null)
            return;
        for(int i = 0; i < devices.size(); ++i) {
            devices.get(i).poll();
            EventQueue queue = devices.get(i).getEventQueue();
            net.java.games.input.Event jiEvent = new net.java.games.input.Event();
            while(queue.getNextEvent(jiEvent)) {
                Event event = new Event(jiEvent, deviceType, i);
                for(JInputListener listener : listeners) {
                    listener.stateChanged(event);
                }
            }
        }
    }

    public void addListener(JInputListener listener) {
        int deviceTypes = listener.getDeviceTypes();
        if((deviceTypes & JInputListener.DeviceType.KEYBOARD) != 0)
            keyboardListeners.add(listener);
        if((deviceTypes & JInputListener.DeviceType.MOUSE) != 0)
            mouseListeners.add(listener);
        if((deviceTypes & JInputListener.DeviceType.JOYSTICK) != 0)
            joystickListeners.add(listener);
    }

    public void removeListener(JInputListener listener) {
        int deviceTypes = listener.getDeviceTypes();
        if((deviceTypes & JInputListener.DeviceType.KEYBOARD) != 0)
            keyboardListeners.remove(listener);
        if((deviceTypes & JInputListener.DeviceType.MOUSE) != 0)
            mouseListeners.remove(listener);
        if((deviceTypes & JInputListener.DeviceType.JOYSTICK) != 0)
            joystickListeners.remove(listener);
    }
}
