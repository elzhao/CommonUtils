package com.elzhao.library.core;

public final class BaseMessage {
    private int messageId = -1;

    private int messageWhat = -1;

    private Object messageObject = null;

    public BaseMessage(int messageWhat) {
        this.messageWhat = messageWhat;
    }

    public BaseMessage(int messageWhat, Object messageObject) {
        this.messageWhat = messageWhat;
        this.messageObject = messageObject;
    }

    public BaseMessage(int messageId, int messageWhat) {
        this.messageId = messageId;
        this.messageWhat = messageWhat;
    }

    public BaseMessage(int messageId, int messageWhat, Object messageObject) {
        this.messageId = messageId;
        this.messageWhat = messageWhat;
        this.messageObject = messageObject;
    }

    public int getMessageWhat() {
        return messageWhat;
    }

    public void setMessageWhat(int messageWhat) {
        this.messageWhat = messageWhat;
    }

    public Object getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(Object messageObject) {
        this.messageObject = messageObject;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

}
