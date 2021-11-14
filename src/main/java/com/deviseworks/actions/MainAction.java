package com.deviseworks.actions;

import com.deviseworks.enumerate.SettingCommandsEnum;
import com.deviseworks.enumerate.SoftwareEnum;
import com.deviseworks.util.Directory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainAction {
    private final Paper paper = new Paper();
    private final Cuberite cuberite = new Cuberite();
    private final Mohist mohist = new Mohist();
    private final Directory directory = new Directory();
    // 利用可能コマンド
    private enum AvailableCommands{
        INSTALL,
        UNINSTALL,
        SETTINGS,
        HELP,
        EXIT
    }
    // 状態
    private enum STATE{
        NONE,
        INSTALL,
        UNINSTALL,
        SETTING
    }
    private STATE state = STATE.NONE;
    // コマンドログ
    private final List<String> history = new ArrayList<>();

    // Function: dialog
    // Description: Loop the function
    public void dialog(){
        Scanner scanner = new Scanner(System.in);

        int result;
        String command;
        while(true){
            if(state != STATE.NONE){
                System.out.print(state + "> ");
            }else{
                System.out.print("> ");
            }
            command = scanner.nextLine();
            result = this.exec(command);
            switch (result) {
                case 1 -> history.add(command);
                case -1 -> {
                    return;
                }
            }
        }
    }

    // Function: exec
    // Description: Execute the command.
    public int exec(String command){
        switch (state) {
            case NONE -> {
                for(AvailableCommands c: AvailableCommands.values()){
                    if(c.toString().equalsIgnoreCase(command)){
                        switch (c) {
                            case INSTALL -> {
                                state = STATE.INSTALL;
                                System.out.println("\nSelect the software that you want to install.");
                                System.out.println("\t- Paper / " + SoftwareEnum.PAPER.getDescription());
                                System.out.println("\t- Cuberite / " + SoftwareEnum.CUBERITE.getDescription());
                                System.out.println("\t- Mohist / " + SoftwareEnum.MOHIST.getDescription() + "\n");
                            }
                            case UNINSTALL -> state = STATE.UNINSTALL;
                            case SETTINGS -> state = STATE.SETTING;
                            case HELP -> this.showHelp();
                            case EXIT -> {
                                return -1;
                            }
                        }
                    }
                }
            }
            case INSTALL -> {
                if(command.equalsIgnoreCase("exit")){
                    state = STATE.NONE;
                    return 0;
                }else {
                    for (SoftwareEnum soft : SoftwareEnum.values()) {
                        if (soft.toString().equalsIgnoreCase(command) || command.equalsIgnoreCase(String.valueOf(soft.toString().charAt(0)))) {
                            switch (soft) {
                                case PAPER -> {
                                    // 変数宣言
                                    String version;
                                    String build;

                                    // バージョン取得
                                    System.out.println("[ Paper ] Getting the versions...");
                                    List<String> versions = paper.getVersions();
                                    version = defineVersion(versions);

                                    // ビルド取得
                                    System.out.println("[ Paper ] Getting the builds...");
                                    List<String> builds = paper.getBuilds(version);
                                    build = defineBuild(builds);

                                    // ディレクトリ確定
                                    Path currentPath = Paths.get("").toAbsolutePath();
                                    Path installPath = directory.seek(currentPath, "Paper", version);

                                    // インストール前確認
                                    System.out.println("[ Paper ] The server will be installed at " + installPath);
                                    if (!quickConfirm()) {
                                        state = STATE.NONE;
                                        return 0;
                                    }

                                    // インストール
                                    System.out.println("[ Paper ] Downloading...");
                                    if (directory.create(installPath)) {
                                        if (paper.install(installPath, version, build, true)) {
                                            System.out.println("[ Paper ] Complete!");
                                        } else {
                                            System.out.println("[ Paper ] Failed...");
                                        }
                                    }

                                    // 終了処理
                                    state = STATE.NONE;
                                    return 1;
                                }
                                case CUBERITE -> {
                                    // ディレクトリ確定
                                    Path installPath = directory.seek(Paths.get("").toAbsolutePath(), "Cuberite");

                                    // インストール前確認
                                    System.out.println("[ Cuberite ] The server will be installed at " + installPath);
                                    if (quickConfirm()) {
                                        System.out.println("[ Cuberite ] Downloading...");
                                        if (directory.create(installPath)) {
                                            if (cuberite.install(installPath)) {
                                                System.out.println("[ Cuberite ] Complete!");
                                            } else {
                                                System.out.println("[ Cuberite ] Failed...");
                                            }

                                            // 終了処理
                                            state = STATE.NONE;
                                            return 1;
                                        }
                                    } else {
                                        state = STATE.NONE;
                                        return 0;
                                    }
                                }
                                case MOHIST -> {
                                    // 変数宣言
                                    String version;
                                    String build;

                                    // バージョン取得
                                    System.out.println("[ Mohist ]Getting the versions...");
                                    List<String> versions = mohist.getVersions();
                                    version = defineVersion(versions);

                                    // ビルド取得
                                    System.out.println("[ Mohist ]Getting the builds...");
                                    List<String> builds = mohist.getBuilds(version);
                                    build = defineBuild(builds);

                                    // ディレクトリ確定
                                    Path installPath = directory.seek(Paths.get("").toAbsolutePath(), "Mohist", version);

                                    // インストール前確認
                                    System.out.println("[ Mohist ] The server will be installed at " + installPath);
                                    if (!quickConfirm()) {
                                        state = STATE.NONE;
                                        return 0;
                                    }

                                    // インストール
                                    System.out.println("[ Mohist ] Downloading...");
                                    if (directory.create(installPath)) {
                                        if (paper.install(installPath, version, build, true)) {
                                            System.out.println("[ Mohist ] Complete!");
                                        } else {
                                            System.out.println("[ Mohist ] Failed...");
                                        }
                                    }

                                    // 終了処理
                                    state = STATE.NONE;
                                    return 1;
                                }
                            }
                        }
                    }
                }
            }
            case UNINSTALL -> {
                state = STATE.NONE;
                return 1;
            }
            case SETTING -> {
                Settings settings = new Settings();
                for(SettingCommandsEnum s: SettingCommandsEnum.values()){
                    if(s.toString().equalsIgnoreCase(command)){
                        switch(s){
                            case getMaxMemory -> System.out.println("MaxMemory: " + settings.getMaxMemory() + "G");
                            case getMinMemory -> System.out.println("MinMemory: " + settings.getMinMemory() + "G");
                            case setMaxMemory -> {
                                System.out.println("[ SETTINGS ] Change the max memory size. Type a number.");
                                int max = this.defineNumber();
                                if(settings.setMaxMemory(max)){
                                    System.out.println("[ SETTINGS ] Success!");
                                }else{
                                    System.out.println("[ SETTINGS ] Failed...");
                                }
                            }
                            case setMinMemory -> {
                                System.out.println("[ SETTINGS ] Change the min memory size. Type a number.");
                                int min = this.defineNumber();
                                if(settings.setMinMemory(min)){
                                    System.out.println("[ SETTINGS ] Success!");
                                }else{
                                    System.out.println("[ SETTINGS ] Failed...");
                                }
                            }
                            case exit -> {
                                state = STATE.NONE;
                                return 0;
                            }
                        }
                        return 1;
                    }
                }
            }
            default -> {
                return 0;
            }
        }

        return 0;
    }

    // Function: defineNumber(){
    private int defineNumber(){
        Scanner scanner = new Scanner(System.in);
        String temp;
        int number;

        while(true){
            System.out.print("NUMBER> ");
            temp = scanner.nextLine();
            try{
                number = Integer.parseInt(temp);
            } catch (Exception e) {
                continue;
            }
            return number;
        }
    }


    // Function: defineVersion
    private String defineVersion(List<String> versions){
        Scanner scanner = new Scanner(System.in);
        String version;

        while(true){
            System.out.print("VERSIONS> ");
            version = scanner.nextLine();

            if("list".equalsIgnoreCase(version)){
                System.out.println("[ VERSIONS ] You can select the following version:");
                for(String vs: versions){
                    System.out.println("\t- " + vs);
                }
            }else if("latest".equalsIgnoreCase(version)){
                return versions.get(versions.size()-1);
            }else {
                for (String v : versions) {
                    if (v.equalsIgnoreCase(version)) {
                        return v;
                    }
                }
            }
        }
    }

    // Function: defineBuild
    private String defineBuild(List<String> builds){
        Scanner scanner = new Scanner(System.in);
        String build;

        while(true){
            System.out.print("BUILDS> ");
            build = scanner.nextLine();
            if("list".equalsIgnoreCase(build)){
                System.out.println("[ BUILDS ] You can select the following version:");
                for(String bd: builds){
                    System.out.println("\t- " + bd);
                }
            }else if("latest".equalsIgnoreCase(build)){
                return builds.get(builds.size()-1);
            }else {
                for (String b : builds) {
                    if (b.equalsIgnoreCase(build)) {
                        return b;
                    }
                }
            }
        }
    }

    // Function: quickConfirm
    private boolean quickConfirm(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Are you ok?(y/N)");
        while(true){
            System.out.print("CONFIRM> ");
            String confirm = scanner.nextLine();
            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                return true;
            }else if(confirm.equalsIgnoreCase("n") || confirm.equalsIgnoreCase("no")){
                return false;
            }
        }
    }

    // Function: showHelp
    // Description: Show the available commands.
    public void showHelp(){
        System.out.println("The following commands are available:");
        System.out.println("\t- install");
        System.out.println("\t- uninstall");
        System.out.println("\t- settings");
        System.out.println("\t- help");
        System.out.println("\t- exit");
    }

    // Function: exec
    // Argument: String[]
    public int exec(String[] args){
        enum State{
            NONE,
            SOFTWARE,
            BUILD,
            VERSION,
            DIRECTORY
        }

        State state = State.NONE;
        String software=null;
        String version=null;
        String build=null;
        boolean isAutoApprove = false;
        String directory=null;

        for(String arg: args){
            if(arg.startsWith("-")){
                if (state == State.NONE) {
                    switch(arg.substring(1)){
                        case "h" -> {
                            System.out.println("Arguments:");
                            System.out.println("\t-h : Show Helps");
                            System.out.println("\t-s : Select server software");
                            System.out.println("\t-v : Select a version");
                            System.out.println("\t-b : Select a build");
                            System.out.println("\t-y : Auto Approve (Not Recommended)");
                            System.out.println("\t-d : Select the directory to be used during installation. (Enclose in \"\")");
                            return 0;
                        }
                        case "s" -> {
                            if(arg.equalsIgnoreCase(args[args.length-1])){
                                System.out.println("\t- 引数が足りません: " + arg);
                                return 1;
                            }
                            state = State.SOFTWARE;
                        }
                        case "v" -> {
                            if(arg.equalsIgnoreCase(args[args.length-1])){
                                System.out.println("\t- 引数が足りません: " + arg);
                                return 1;
                            }
                            state = State.VERSION;
                        }
                        case "b" -> {
                            if(arg.equalsIgnoreCase(args[args.length-1])){
                                System.out.println("\t- 引数が足りません: " + arg);
                                return 1;
                            }
                            state = State.BUILD;
                        }
                        case "y" -> isAutoApprove = true;
                        case "d" -> {
                            if(arg.equalsIgnoreCase(args[args.length-1])){
                                System.out.println("\t- 引数が足りません: " + arg);
                                return 1;
                            }
                            state = State.DIRECTORY;
                        }
                        default -> {
                            System.out.println("\t- 無効な引数");
                            return 1;
                        }
                    }
                }else{
                    System.out.println("\t- 構文エラー");
                }
            }else{
                switch(state){
                    case NONE -> System.out.println("\t- 無効な引数");
                    case SOFTWARE -> software = arg;
                    case VERSION -> version = arg;
                    case BUILD -> build = arg;
                    case DIRECTORY -> directory = arg;
                }
                state = State.NONE;
            }
        }
        if(software != null){
            boolean isHit = false;
            for(SoftwareEnum e: SoftwareEnum.values()){
                if(software.equalsIgnoreCase(e.toString())){
                    isHit = true;
                    break;
                }
            }
            if(!isHit){
                System.out.println("\t- 無効なソフトウェア名 : " + software);
                return 1;
            }
            software = software.toLowerCase();

            switch (software){
                case "paper" -> {
                    Paper paper = new Paper();
                    // バージョン確定
                    isHit = false;
                    if(version != null){
                        List<String> versions = paper.getVersions();
                        for(String v: versions){
                            if(v.equalsIgnoreCase(version)){
                                isHit = true;
                                break;
                            }
                        }
                        if(!isHit){
                            System.out.println("\t- 無効なバージョン : " + version);
                            return 1;
                        }
                    }




                    if(version == null){
                        Path path = new Directory().seek(Paths.get("").toAbsolutePath(), "Paper");
                        if(paper.install(path)){
                            System.out.println("[ INSTALL ] Paper was installed to your computer.");
                        }else{
                            System.out.println("[ INSTALL ] Install failed...");
                        }
                    }
                }
            }
        }


        return 0;
    }
}
