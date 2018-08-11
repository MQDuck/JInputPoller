package net.mqduck.jinputpoller;

import net.java.games.input.Component;

public class Event {
    public final int deviceType;
    public final int deviceIndex;
    public final Component component;
    public final float value;
    public final long nanos;

    public Event(net.java.games.input.Event jiEvent, int deviceType, int deviceIndex) {
        this.deviceType = deviceType;
        this.deviceIndex = deviceIndex;
        component = jiEvent.getComponent();
        value = jiEvent.getValue();
        nanos = jiEvent.getNanos();
    }
}
