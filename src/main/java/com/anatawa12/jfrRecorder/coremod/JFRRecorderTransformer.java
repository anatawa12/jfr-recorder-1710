package com.anatawa12.jfrRecorder.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JFRRecorderTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName == null) transformedName = name;

        if (basicClass == null) return null;
        if (transformedName == null) return basicClass;

        switch (transformedName) {
            case entityPlayerMPName: {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(0);

                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                    @Override
                    public void visitEnd() {
                        visitField(Opcodes.ACC_PUBLIC, modListName, modListDesc, modListSig, null);
                        super.visitEnd();
                    }
                };

                cr.accept(cv, 0);

                return cw.toByteArray();
            }
            case fmlHandshakeServerStateHelloName: {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(0);

                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                    int verify = 0;
                    int verifyCnt = 0;

                    @Override
                    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                        if (name.equals("accept")
                                && desc.equals(fmlHandshakeServerStateAcceptDesc)) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                            mv = new MethodVisitor(Opcodes.ASM5, mv) {
                                @Override
                                public void visitCode() {
                                    super.visitCode();
                                    super.visitVarInsn(Opcodes.ALOAD, 1);
                                    super.visitVarInsn(Opcodes.ALOAD, 2);
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                            hooksInternalName,
                                            onAcceptOnHelloStateOnHandshakeOnServerName,
                                            onAcceptOnHelloStateOnHandshakeOnServerDesc,
                                            false);
                                }
                            };
                            verify |= 0x0001;
                            verifyCnt++;
                            return mv;
                        }
                        return super.visitMethod(access, name, desc, signature, exceptions);
                    }

                    @Override
                    public void visitEnd() {
                        super.visitEnd();
                        if (verify != 0x0001) throw new AssertionError();
                        if (verifyCnt != 1) throw new AssertionError();
                    }
                };

                cr.accept(cv, 0);

                return cw.toByteArray();
            }
            case fmlHandshakeClientStateWaitingServerDataName: {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(0);

                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                    int verify = 0;
                    int verifyCnt = 0;

                    @Override
                    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                        if (name.equals("accept")
                                && desc.equals(fmlHandshakeClientStateAcceptDesc)) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                            mv = new MethodVisitor(Opcodes.ASM5, mv) {
                                @Override
                                public void visitCode() {
                                    super.visitCode();
                                    super.visitVarInsn(Opcodes.ALOAD, 1);
                                    super.visitVarInsn(Opcodes.ALOAD, 2);
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                            hooksInternalName,
                                            onAcceptOnWaitingServerDataStateOnHandshakeOnClientName,
                                            onAcceptOnWaitingServerDataStateOnHandshakeOnClientDesc,
                                            false);
                                }
                            };
                            verify |= 0x0001;
                            verifyCnt++;
                            return mv;
                        }
                        return super.visitMethod(access, name, desc, signature, exceptions);
                    }

                    @Override
                    public void visitEnd() {
                        super.visitEnd();
                        if (verify != 0x0001) throw new AssertionError();
                        if (verifyCnt != 1) throw new AssertionError();
                    }
                };

                cr.accept(cv, 0);

                return cw.toByteArray();
            }
            case networkDispatcherName: {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(0);

                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                    int verify = 0;
                    int verifyCnt = 0;

                    @Override
                    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                        if (name.equals(playerName) && desc.equals(playerDesc)) {
                            access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_PUBLIC);
                            access |= Opcodes.ACC_PUBLIC;
                            verify |= 0x0001;
                            verifyCnt++;
                        }
                        return super.visitField(access, name, desc, signature, value);
                    }

                    @Override
                    public void visitEnd() {
                        super.visitEnd();
                        if (verify != 0x0001) throw new AssertionError();
                        if (verifyCnt != 1) throw new AssertionError();
                    }
                };

                cr.accept(cv, 0);

                return cw.toByteArray();
            }
            case hooksName: {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(0);

                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                        if (name.equals(modListOfName) && desc.equals(modListOfDesc)) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                            if (mv == null) return null;

                            mv.visitCode();
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, entityPlayerMPInternalName, modListName, modListDesc);
                            mv.visitInsn(Opcodes.ARETURN);
                            mv.visitMaxs(1, 1);
                            mv.visitEnd();

                            return null;
                        } else if (name.equals(setModListOfName) && desc.equals(setModListOfDesc)) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                            if (mv == null) return null;

                            mv.visitCode();
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitVarInsn(Opcodes.ALOAD, 1);
                            mv.visitFieldInsn(Opcodes.PUTFIELD, entityPlayerMPInternalName, modListName, modListDesc);
                            mv.visitInsn(Opcodes.RETURN);
                            mv.visitMaxs(2, 2);
                            mv.visitEnd();

                            return null;
                        } else if (name.equals(playerOfName) && desc.equals(playerOfDesc)) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                            if (mv == null) return null;

                            mv.visitCode();
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, networkDispatcherInternalName, playerName, playerDesc);
                            mv.visitInsn(Opcodes.ARETURN);
                            mv.visitMaxs(2, 2);
                            mv.visitEnd();

                            return null;
                        } else {
                            return super.visitMethod(access, name, desc, signature, exceptions);
                        }
                    }
                };

                cr.accept(cv, 0);

                return cw.toByteArray();
            }
            default:
                return basicClass;
        }
    }

    // EntityPlayerMP
    private static final String entityPlayerMPName = "net.minecraft.entity.player.EntityPlayerMP";
    private static final String entityPlayerMPInternalName = "net/minecraft/entity/player/EntityPlayerMP";

    private static final String modListName = "com anatawa12 jfrRecorder modList";
    private static final String modListDesc = "Ljava/util/Map;";
    private static final String modListSig = "Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;";

    // FMLHandshakeServerState.Hello
    private static final String fmlHandshakeServerStateHelloName = "cpw.mods.fml.common.network.handshake.FMLHandshakeServerState$2";
    private static final String fmlHandshakeServerStateAcceptDesc
            = "(Lio/netty/channel/ChannelHandlerContext;" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage;)" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeServerState;";

    // FMLHandshakeClientState.WaitingServerData
    private static final String fmlHandshakeClientStateWaitingServerDataName = "cpw.mods.fml.common.network.handshake.FMLHandshakeClientState$3";
    private static final String fmlHandshakeClientStateAcceptDesc
            = "(Lio/netty/channel/ChannelHandlerContext;" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage;)" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeClientState;";

    // NetworkDispatcher
    private static final String networkDispatcherName = "cpw.mods.fml.common.network.handshake.NetworkDispatcher";
    private static final String networkDispatcherInternalName = "cpw/mods/fml/common/network/handshake/NetworkDispatcher";
    private static final String playerName = "player";
    private static final String playerDesc = "Lnet/minecraft/entity/player/EntityPlayerMP;";

    // Hooks
    private static final String hooksInternalName = "com/anatawa12/jfrRecorder/Hooks";
    private static final String hooksName = "com.anatawa12.jfrRecorder.Hooks";

    private static final String modListOfName = "modListOf";
    private static final String modListOfDesc = "(Lnet/minecraft/entity/player/EntityPlayerMP;)Ljava/util/Map;";

    private static final String setModListOfName = "setModListOf";
    private static final String setModListOfDesc = "(Lnet/minecraft/entity/player/EntityPlayerMP;Ljava/util/Map;)V";

    private static final String playerOfName = "playerOf";
    private static final String playerOfDesc = "(Lcpw/mods/fml/common/network/handshake/NetworkDispatcher;)" +
            "Lnet/minecraft/entity/player/EntityPlayerMP;";

    private static final String onAcceptOnHelloStateOnHandshakeOnServerName = "onAcceptOnHelloStateOnHandshakeOnServer";
    private static final String onAcceptOnHelloStateOnHandshakeOnServerDesc = "(Lio/netty/channel/ChannelHandlerContext;" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage;)V";

    private static final String onAcceptOnWaitingServerDataStateOnHandshakeOnClientName = "onAcceptOnWaitingServerDataStateOnHandshakeOnClient";
    private static final String onAcceptOnWaitingServerDataStateOnHandshakeOnClientDesc = "(Lio/netty/channel/ChannelHandlerContext;" +
            "Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage;)V";
}
