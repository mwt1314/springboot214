package cn.matio.api.test;

/**
 * @author mawt
 * @description
 * @date 2019/12/30
 */
public class SyncMain {

    public void print(){
        synchronized (this){
            System.out.println("hello");
        }
    }

}
/**
 * Compiled from "SyncMain.java"
 * public class cn.matio.api.test.SyncMain {
 *   public cn.matio.api.test.SyncMain();
 *     Code:
 *        0: aload_0
 *        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *        4: return
 *
 *   public void print();
 *     Code:
 *        0: aload_0
 *        1: dup
 *        2: astore_1
 *        3: monitorenter
 *        4: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *        7: ldc           #3                  // String hello
 *        9: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *       12: aload_1
 *       13: monitorexit
 *       14: goto          22
 *       17: astore_2
 *       18: aload_1
 *       19: monitorexit
 *       20: aload_2
 *       21: athrow
 *       22: return
 *     Exception table:
 *        from    to  target type
 *            4    14    17   any
 *           17    20    17   any
 * }
 *
 *
 *
 */
