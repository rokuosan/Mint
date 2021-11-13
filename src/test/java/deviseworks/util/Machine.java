package deviseworks.util;

import java.util.Locale;

public class Machine {
    private final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    private final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

    public String getOperatingSystem(){
        return this.OS_NAME;
    }

    public String getArchitecture(){
        return this.OS_ARCH;
    }

    public boolean isWindows(){
        return OS_NAME.startsWith("windows");
    }

    public boolean isLinux(){
        return OS_NAME.startsWith("linux");
    }

    public boolean is64bit(){
        return OS_ARCH.equalsIgnoreCase("amd64");
    }

    public boolean is32bit(){
        return OS_ARCH.equalsIgnoreCase("x86");
    }

    public int getBit(){
        if(this.is64bit()){
            return 64;
        }else if(this.is32bit()){
            return 32;
        }else{
            return 0;
        }
    }
}
