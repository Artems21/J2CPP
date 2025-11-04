package cc.jlom;

import cc.jlom.transpiler.Transpiler;

public class Main {
    public static void main(String[] args) {
        String path = "/home/artem/IdeaProjects/J2CPP/jars/test.jar";
        var transpiler = new Transpiler(path);

        transpiler.process();
    }
}