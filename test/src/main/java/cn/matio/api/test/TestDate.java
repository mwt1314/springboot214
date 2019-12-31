package cn.matio.api.test;

import java.util.Date;

/**
 * 一般常用的是-v -l -c三个选项。
 * javap -v classxx，不仅会输出行号、本地变量表信息、反编译汇编代码，还会输出当前类用到的常量池等信息。
 * javap -l 会输出行号和本地变量表信息。
 * javap -c 会对当前class字节码进行反编译生成汇编代码。
 */
public class TestDate {
    private int count = 0;

    public static void main(String[] args) {
        TestDate testDate = new TestDate();
        testDate.test1();
    }

    public void test1() {
        Date date = new Date();
        String name1 = "wangerbei";
        test2(date, name1);
        System.out.println(date + name1);
    }

    public void test2(Date dateP, String name2) {
        dateP = null;
        name2 = "zhangsan";
    }

    public void test3() {
        count++;
    }

    public void test4() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        int c = a + 1;
    }

}
/*
Compiled from "TestDate.java"
public class cn.matio.api.test.TestDate {

  //默认的构造方法，在构造方法执行时主要完成一些初始化操作，包括一些成员变量的初始化赋值等操作
  public cn.matio.api.test.TestDate();
    Code:
       0: aload_0                           //从本地变量表种加载索引为0的变量的值，也就是this的引用，压入栈
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: aload_0
       5: iconst_0
       6: putfield      #2                  // Field count:I
       9: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #3                  // class cn/matio/api/test/TestDate
       3: dup
       4: invokespecial #4                  // Method "<init>":()V
       7: astore_1
       8: aload_1
       9: invokevirtual #5                  // Method test1:()V
      12: return

  public void test1();
    Code:
       0: new           #6                  // class java/util/Date
       3: dup
       4: invokespecial #7                  // Method java/util/Date."<init>":()V
       7: astore_1
       8: ldc           #8                  // String wangerbei
      10: astore_2
      11: aload_0
      12: aload_1
      13: aload_2
      14: invokevirtual #9                  // Method test2:(Ljava/util/Date;Ljava/lang/String;)V
      17: getstatic     #10                 // Field java/lang/System.out:Ljava/io/PrintStream;
      20: new           #11                 // class java/lang/StringBuilder
      23: dup
      24: invokespecial #12                 // Method java/lang/StringBuilder."<init>":()V
      27: aload_1
      28: invokevirtual #13                 // Method java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      31: aload_2
      32: invokevirtual #14                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      35: invokevirtual #15                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      38: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      41: return

  public void test2(java.util.Date, java.lang.String);
    Code:
       0: aconst_null
       1: astore_1
       2: ldc           #17                 // String zhangsan
       4: astore_2
       5: return

  public void test3();
    Code:
       0: aload_0
       1: dup
       2: getfield      #2                  // Field count:I
       5: iconst_1
       6: iadd
       7: putfield      #2                  // Field count:I
      10: return

  public void test4();
    Code:
       0: iconst_0
       1: istore_1
       2: iconst_0
       3: istore_2
       4: iload_1
       5: iconst_1
       6: iadd
       7: istore_2
       8: iload_1
       9: iconst_1
      10: iadd
      11: istore_2
      12: return
}


*
*
*/



