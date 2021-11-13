package deviseworks;

public enum SettingCommandsEnum {
    setMaxMemory("最大使用可能メモリ量"),
    setMinMemory("最小利用可能メモリ量"),
    getMaxMemory("現在の最大メモリ量を取得する"),
    getMinMemory("現在の最小メモリ量を取得する"),
    exit("設定を終了する");


    private final String description;

    SettingCommandsEnum(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
