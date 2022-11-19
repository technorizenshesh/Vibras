package com.my.vibras.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
