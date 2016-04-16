package net.lyxnx.chestshop;

import lombok.Getter;
import lombok.Setter;

public class Item {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String code;

    public Item() {

    }

    public Item(final String code) {
        this.code = code;
    }
}