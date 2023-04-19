package com.example.douyayun.server;

public class JvmParam {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        // stringBuilder.append("-Dresin.home=$SERVER_ROOT ");
        stringBuilder.append("-server ");
        long totalMemory = 2 * 1024;  // 电脑内存1G，转换为M
        // System.out.println(totalMemory);

        // 初始堆内存大小（物理内存的1/64），默认(MinHeapFreeRatio参数可以调整)空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制.
        long heapSize = totalMemory / 4;
        stringBuilder.append("-Xms" + heapSize + "M ");
        // 最大堆内存大小（物理内存的1/4）， 默认(MaxHeapFreeRatio参数可以调整)空余堆内存大于70%时，JVM会减少堆直到 -Xms的最小限制
        stringBuilder.append("-Xmx" + heapSize + "M ");

        // 持久代初始值,物理内存1/64
        long maxPermSize = totalMemory / 4;
        stringBuilder.append("-XX:MetaspaceSize=" + maxPermSize + "M ");
        // 持久代初始值,物理内存1/4
        stringBuilder.append("-XX:MaxMetaspaceSize=" + maxPermSize + "M ");
        /*XMX和XMS设置一样大，MetaspaceSize和MaxMetaspaceSize设置一样大，这样可以减轻伸缩堆大小带来的压力*/

        // 年轻代大小(eden+ 2 survivor space),整个堆内存的1/3，一般eden区与S0、S1区的大小比例为为8:1:1
        // 整个堆大小=年轻代大小 + 年老代大小 + 持久代大小.
        //-XX:NewSize
        //-XX:MaxNewSize
        long youngSize = heapSize / 3;
        stringBuilder.append("-Xmn" + youngSize + "M ");
        // Eden区与Survivor区的大小比值,设置为8,则两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10
        stringBuilder.append("-XX:SurvivorRatio=8 ");
        // 线程的堆栈大小,一般小的应用，如果栈不是很深，应该是128k够用的 大的应用建议使用256k。
        // 根据应用的线程所需内存大小进行调整.在相同物理内存下,减小这个值能生成更多的线程
        stringBuilder.append("-Xss256K ");
        // 关闭System.gc()
        stringBuilder.append("-XX:+DisableExplicitGC ");
        // 使用CMS内存收集
        stringBuilder.append("-XX:+UseConcMarkSweepGC ");
        // 设置年轻代为并行收集
        stringBuilder.append("-XX:+UseParNewGC ");
        // 降低标记停顿
        stringBuilder.append("-XX:+CMSParallelRemarkEnabled ");
        // 在FULL GC的时候，对年老代的压缩,可能会影响性能,但是可以消除碎片
        // stringBuilder.append("-XX:+UseCMSCompactAtFullCollection ");
        // 多少次后进行内存压缩
        // stringBuilder.append("-XX:CMSFullGCsBeforeCompaction=0 ");
        stringBuilder.append("-XX:+CMSClassUnloadingEnabled ");
        // 内存页的大小不可设置过大， 会影响Perm的大小
        stringBuilder.append("-XX:LargePageSizeInBytes=128M ");
        // 原始类型的快速优化
        stringBuilder.append("-XX:+UseFastAccessorMethods ");
        // 使用手动定义初始化定义开始CMS收集
        stringBuilder.append("-XX:+UseCMSInitiatingOccupancyOnly ");
        // 设置Perm Gen使用到达多少比例时触发
        stringBuilder.append("-XX:CMSInitiatingOccupancyFraction=70 ");
        // 每兆堆空闲空间中SoftReference的存活时间
        stringBuilder.append("-XX:SoftRefLRUPolicyMSPerMB=0 ");
        // garbage collects before printing the histogram
        stringBuilder.append("-XX:+PrintClassHistogram ");
        stringBuilder.append("-XX:+PrintGCDetails ");
        // 打印GC前后的详细堆栈信息
        stringBuilder.append("-XX:+PrintHeapAtGC ");
        // stringBuilder.append("-Xloggc:log/gc.log ");

        System.out.println(stringBuilder.toString());
    }
}
