package com.deviseworks.enumerate;

public enum SoftwareEnum {
    CUBERITE("+ 独自のコントロールパネルを持ったバニラ向けサーバー"),
    PAPER("+ 高パフォーマンスが魅力のプラグインサーバー"),
    MOHIST("+ 1.12.2, 1.16.5に対応したBukkitForgeサーバー");

    private final String description;

    SoftwareEnum(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
