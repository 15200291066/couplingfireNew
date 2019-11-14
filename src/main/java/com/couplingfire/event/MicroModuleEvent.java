package com.couplingfire.event;

public abstract class MicroModuleEvent extends Event{
    private final long timestamp = System.currentTimeMillis();

    protected Class<?> eventDataType; //事件携带的数据类型

    protected String eventDataMsg; //事件携带的数据 JSON

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MicroModuleEvent(Object source) {
        super(source);
    }

    public final long getTimestamp() {
        return this.timestamp;
    }
}
