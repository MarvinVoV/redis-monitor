package sun.redis.monitor.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yamorn on 2015/10/30.
 *
 * The result of INFO command.
 */
public class RedisInfoEntity {
    /**
     * Server Section
     */
    private String redisVersion;
    private String os;  // Operating system hosting the Redis server
    private int tcpPort;    // TCP/IP listen port
    private String archBits;  // Architecture (32 or 64 bits)
    private String multiplexingApi; // event loop mechanism used by Redis
    private String gccVersion;  // Version of the GCC compiler used to compile the Redis server
    private int processId; // PID of the server process
    private long uptimeInSeconds; // PID of the server process
    private int uptimeInDays;  // Same value expressed in days
    private long lruClock; // Clock incrementing every minute, for LRU management

    /**
     * Clients Section
     */
    private int connectedClients; // Number of client connections (excluding connections from slaves)
    private int clientLongestOutputList; // longest output list among current client connections
    private int clientBiggestInputBuf; // biggest input buffer among current client connections
    private int blockedClients; // Number of clients pending on a blocking call (BLPOP, BRPOP, BRPOPLPUSH)

    /**
     * Memory Section
     */
    private long usedMemory; // total number of bytes allocated by Redis using its allocator
    private String usedMemoryHuman; // Human readable representation of previous value
    private long usedMemoryRss; // Number of bytes that Redis allocated as seen by the operating system (a.k.a resident set size). This is the number reported by tools such as top(1) and ps(1)
    private long usedMemoryPeak;//Peak memory consumed by Redis (in bytes)
    private String usedMemoryPeakHuman; //  Human readable representation of previous value
    private long usedMemoryLua; //  Number of bytes used by the Lua engine
    /**
     * Stats  Section
     */
    private long totalConnectionsReceived; //  Total number of connections accepted by the server
    private long totalCommandsProcessed; //  Total number of commands processed by the server
    private long instantaneousOpsPerSec; // Number of commands processed per second
    private long totalNetInputBytes;
    private long totalNetOutputBytes;
    private long rejectedConnections; //  Number of connections rejected because of maxclients limit
    private long expiredKeys; // Total number of key expiration events
    private long evictedKeys; //Number of evicted keys due to maxmemory limit
    private long keyspaceHits; //  Number of successful lookup of keys in the main dictionary
    private long keyspaceMisses; // Number of failed lookup of keys in the main dictionary
    private long pubsubChannels; // Global number of pub/sub channels with client subscriptions
    private long pubsubPatterns;//  Global number of pub/sub pattern with client subscriptions
    private long latestForkUsec; // Duration of the latest fork operation in microseconds

    /**
     * Cluster
     */
    private int clusterEnabled;
    /**
     * Replication Section
     */
    private String role; // master or slave
    private int connectedSlaves;
    private String masterHost; // Host or IP address of the master
    private int masterPort; // Master listening TCP port
    private List<String> dbStatistics = new LinkedList<String>(); // dbXXX:

    public String getRedisVersion() {
        return redisVersion;
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getArchBits() {
        return archBits;
    }

    public void setArchBits(String archBits) {
        this.archBits = archBits;
    }

    public String getMultiplexingApi() {
        return multiplexingApi;
    }

    public void setMultiplexingApi(String multiplexingApi) {
        this.multiplexingApi = multiplexingApi;
    }

    public String getGccVersion() {
        return gccVersion;
    }

    public void setGccVersion(String gccVersion) {
        this.gccVersion = gccVersion;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public long getUptimeInSeconds() {
        return uptimeInSeconds;
    }

    public void setUptimeInSeconds(long uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    public int getUptimeInDays() {
        return uptimeInDays;
    }

    public void setUptimeInDays(int uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    public long getLruClock() {
        return lruClock;
    }

    public void setLruClock(long lruClock) {
        this.lruClock = lruClock;
    }

    public int getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(int connectedClients) {
        this.connectedClients = connectedClients;
    }

    public int getClientLongestOutputList() {
        return clientLongestOutputList;
    }

    public void setClientLongestOutputList(int clientLongestOutputList) {
        this.clientLongestOutputList = clientLongestOutputList;
    }

    public int getClientBiggestInputBuf() {
        return clientBiggestInputBuf;
    }

    public void setClientBiggestInputBuf(int clientBiggestInputBuf) {
        this.clientBiggestInputBuf = clientBiggestInputBuf;
    }

    public int getBlockedClients() {
        return blockedClients;
    }

    public void setBlockedClients(int blockedClients) {
        this.blockedClients = blockedClients;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public String getUsedMemoryHuman() {
        return usedMemoryHuman;
    }

    public void setUsedMemoryHuman(String usedMemoryHuman) {
        this.usedMemoryHuman = usedMemoryHuman;
    }

    public long getUsedMemoryRss() {
        return usedMemoryRss;
    }

    public void setUsedMemoryRss(long usedMemoryRss) {
        this.usedMemoryRss = usedMemoryRss;
    }

    public long getUsedMemoryPeak() {
        return usedMemoryPeak;
    }

    public void setUsedMemoryPeak(long usedMemoryPeak) {
        this.usedMemoryPeak = usedMemoryPeak;
    }

    public String getUsedMemoryPeakHuman() {
        return usedMemoryPeakHuman;
    }

    public void setUsedMemoryPeakHuman(String usedMemoryPeakHuman) {
        this.usedMemoryPeakHuman = usedMemoryPeakHuman;
    }

    public long getUsedMemoryLua() {
        return usedMemoryLua;
    }

    public void setUsedMemoryLua(long usedMemoryLua) {
        this.usedMemoryLua = usedMemoryLua;
    }

    public long getTotalConnectionsReceived() {
        return totalConnectionsReceived;
    }

    public void setTotalConnectionsReceived(long totalConnectionsReceived) {
        this.totalConnectionsReceived = totalConnectionsReceived;
    }

    public long getTotalCommandsProcessed() {
        return totalCommandsProcessed;
    }

    public void setTotalCommandsProcessed(long totalCommandsProcessed) {
        this.totalCommandsProcessed = totalCommandsProcessed;
    }

    public long getInstantaneousOpsPerSec() {
        return instantaneousOpsPerSec;
    }

    public void setInstantaneousOpsPerSec(long instantaneousOpsPerSec) {
        this.instantaneousOpsPerSec = instantaneousOpsPerSec;
    }

    public long getTotalNetInputBytes() {
        return totalNetInputBytes;
    }

    public void setTotalNetInputBytes(long totalNetInputBytes) {
        this.totalNetInputBytes = totalNetInputBytes;
    }

    public long getTotalNetOutputBytes() {
        return totalNetOutputBytes;
    }

    public void setTotalNetOutputBytes(long totalNetOutputBytes) {
        this.totalNetOutputBytes = totalNetOutputBytes;
    }

    public long getRejectedConnections() {
        return rejectedConnections;
    }

    public void setRejectedConnections(long rejectedConnections) {
        this.rejectedConnections = rejectedConnections;
    }

    public long getExpiredKeys() {
        return expiredKeys;
    }

    public void setExpiredKeys(long expiredKeys) {
        this.expiredKeys = expiredKeys;
    }

    public long getEvictedKeys() {
        return evictedKeys;
    }

    public void setEvictedKeys(long evictedKeys) {
        this.evictedKeys = evictedKeys;
    }

    public long getKeyspaceHits() {
        return keyspaceHits;
    }

    public void setKeyspaceHits(long keyspaceHits) {
        this.keyspaceHits = keyspaceHits;
    }

    public long getKeyspaceMisses() {
        return keyspaceMisses;
    }

    public void setKeyspaceMisses(long keyspaceMisses) {
        this.keyspaceMisses = keyspaceMisses;
    }

    public long getPubsubChannels() {
        return pubsubChannels;
    }

    public void setPubsubChannels(long pubsubChannels) {
        this.pubsubChannels = pubsubChannels;
    }

    public long getPubsubPatterns() {
        return pubsubPatterns;
    }

    public void setPubsubPatterns(long pubsubPatterns) {
        this.pubsubPatterns = pubsubPatterns;
    }

    public long getLatestForkUsec() {
        return latestForkUsec;
    }

    public void setLatestForkUsec(long latestForkUsec) {
        this.latestForkUsec = latestForkUsec;
    }

    public int getClusterEnabled() {
        return clusterEnabled;
    }

    public void setClusterEnabled(int clusterEnabled) {
        this.clusterEnabled = clusterEnabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getConnectedSlaves() {
        return connectedSlaves;
    }

    public void setConnectedSlaves(int connectedSlaves) {
        this.connectedSlaves = connectedSlaves;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    public List<String> getDbStatistics() {
        return dbStatistics;
    }

    public void setDbStatistics(List<String> dbStatistics) {
        this.dbStatistics = dbStatistics;
    }
}
