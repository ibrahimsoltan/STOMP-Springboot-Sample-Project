package com.ims.stomp;

import lombok.Data;

@Data
public class HelloMessage {
    String name;

    public HelloMessage(String name) {
        this.name = name;
    }

    public HelloMessage() {}

    public String getName() {
        return name;
    }

}
