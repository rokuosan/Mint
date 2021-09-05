package com.deviseworks;

public enum ServerSoftwareEnum {
    VANILA,
    FORGE,
    PAPER;

    public String parseSoftware(int id) {
        for(ServerSoftwareEnum s: ServerSoftwareEnum.values()){
            if(s.ordinal()==id){
                return s.name();
            }
        }
        return "";
    }

}
