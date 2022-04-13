package com.mobileproject.valhalla.utils.Models;

import com.cometchat.pro.models.TextMessage;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {
    private TextMessage textMessage;

    public Message(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    @Override
    public String getId() {
        return textMessage.getMuid();
    }

    @Override
    public String getText() {
        return textMessage.getText();
    }

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(textMessage.getSentAt() * 1000);
    }
}
