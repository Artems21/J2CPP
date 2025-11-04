package cc.jlom.wrappers;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class ClassWrapper {
    private final String className;
    private final ClassNode node;
    private final List<MethodWrapper> methods;
    private byte[] data;

    public ClassWrapper(String className, ClassNode node, byte[] data) {
        this.className = className;
        this.node = node;
        this.methods = node.methods.stream().map(MethodWrapper::new).toList();
        this.data = data;
    }

    public String name() {
        return className;
    }

    public ClassNode node() {
        return node;
    }

    public List<MethodWrapper> methods() {
        return methods;
    }

    public byte[] data(){
        return data;
    }

    public void replace_data(byte[] data){
        this.data = data;
    }
}
