/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oshi.SystemInfo
 *  oshi.hardware.CentralProcessor
 *  oshi.hardware.CentralProcessor$TickType
 *  oshi.hardware.GlobalMemory
 *  oshi.hardware.HardwareAbstractionLayer
 *  oshi.software.os.FileSystem
 *  oshi.software.os.OSFileStore
 *  oshi.software.os.OperatingSystem
 *  oshi.util.Util
 */
package com.mdd.common.core;

import com.mdd.common.entity.server.Cpu;
import com.mdd.common.entity.server.Disk;
import com.mdd.common.entity.server.Jvm;
import com.mdd.common.entity.server.Mem;
import com.mdd.common.entity.server.Sys;
import com.mdd.common.util.ArithUtils;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.TimeUtils;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

public class ServerResult {
    private static final int OSHI_WAIT_SECOND = 1000;
    private final Cpu cpu = new Cpu();
    private final Mem mem = new Mem();
    private final Jvm jvm = new Jvm();
    private final Sys sys = new Sys();
    private final List<Disk> disk = new LinkedList<Disk>();

    public Map<String, Object> copyTo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        this.setCpuInfo(hal.getProcessor());
        this.setMemInfo(hal.getMemory());
        this.setSysInfo();
        this.setJvmInfo();
        this.setSysFiles(si.getOperatingSystem());
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("cpu", this.cpu);
        map.put("mem", this.mem);
        map.put("sys", this.sys);
        map.put("disk", this.disk);
        map.put("jvm", this.jvm);
        return map;
    }

    private void setCpuInfo(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep((long)1000L);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softer = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softer + steal;
        this.cpu.setCpuNum(processor.getLogicalProcessorCount());
        this.cpu.setTotal(ArithUtils.round(ArithUtils.mul(totalCpu, 100.0), 2));
        this.cpu.setSys(ArithUtils.round(ArithUtils.mul((double)cSys / this.cpu.getTotal(), 100.0), 2));
        this.cpu.setUsed(ArithUtils.round(ArithUtils.mul((double)user / this.cpu.getTotal(), 100.0), 2));
        this.cpu.setWait(ArithUtils.round(ArithUtils.mul((double)ioWait / this.cpu.getTotal(), 100.0), 2));
        this.cpu.setFree(ArithUtils.round(ArithUtils.mul((double)idle / this.cpu.getTotal(), 100.0), 2));
    }

    private void setMemInfo(GlobalMemory memory) {
        int number = 0x40000000;
        this.mem.setTotal(ArithUtils.div(memory.getTotal(), number, 2));
        this.mem.setUsed(ArithUtils.div(memory.getTotal() - memory.getAvailable(), number, 2));
        this.mem.setFree(ArithUtils.div(memory.getAvailable(), number, 2));
        this.mem.setUsage(ArithUtils.mul(ArithUtils.div(this.mem.getUsed(), memory.getTotal(), 4), 100.0));
    }

    private void setSysInfo() {
        Properties props = System.getProperties();
        this.sys.setComputerName(IpUtils.getHostName());
        this.sys.setComputerIp(IpUtils.getHostIp());
        this.sys.setOsName(props.getProperty("os.name"));
        this.sys.setOsArch(props.getProperty("os.arch"));
        this.sys.setUserDir(props.getProperty("user.dir"));
    }

    private void setJvmInfo() {
        Properties props = System.getProperties();
        this.jvm.setTotal(ArithUtils.div(Runtime.getRuntime().totalMemory(), 1048576.0, 2));
        this.jvm.setMax(ArithUtils.div(Runtime.getRuntime().maxMemory(), 1048576.0, 2));
        this.jvm.setFree(ArithUtils.div(Runtime.getRuntime().freeMemory(), 1048576.0, 2));
        this.jvm.setUsage(ArithUtils.mul(ArithUtils.div(this.jvm.getTotal() - this.jvm.getFree(), this.jvm.getTotal(), 4), 100.0));
        this.jvm.setVersion(props.getProperty("java.version"));
        this.jvm.setHome(props.getProperty("java.home"));
        this.jvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
        this.jvm.setInputArgs(ManagementFactory.getRuntimeMXBean().getInputArguments().toString());
        this.jvm.setRunTime(TimeUtils.datePoor(TimeUtils.nowDate(), TimeUtils.serverStartDate()));
        this.jvm.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TimeUtils.serverStartDate()));
    }

    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            Disk sysFile = new Disk();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(this.convertFileSize(total));
            sysFile.setFree(this.convertFileSize(free));
            sysFile.setUsed(this.convertFileSize(used));
            sysFile.setUsage(ArithUtils.mul(ArithUtils.div(used, total, 4), 100.0));
            this.disk.add(sysFile);
        }
    }

    public String convertFileSize(long size) {
        long kb = 1024L;
        long mb = kb * 1024L;
        long gb = mb * 1024L;
        if (size >= gb) {
            return String.format("%.1f GB", Float.valueOf((float)size / (float)gb));
        }
        if (size >= mb) {
            float f = (float)size / (float)mb;
            return String.format(f > 100.0f ? "%.0f MB" : "%.1f MB", Float.valueOf(f));
        }
        if (size >= kb) {
            float f = (float)size / (float)kb;
            return String.format(f > 100.0f ? "%.0f KB" : "%.1f KB", Float.valueOf(f));
        }
        return String.format("%d B", size);
    }
}

