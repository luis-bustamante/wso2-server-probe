package org.wso2.carbon.server.admin.service;

public abstract interface ServerAdminMBean
{
  public abstract boolean restart()
    throws Exception;
  
  public abstract boolean shutdown()
    throws Exception;
  
  public abstract boolean restartGracefully()
    throws Exception;
  
  public abstract boolean shutdownGracefully()
    throws Exception;
  
  public abstract void startMaintenance()
    throws Exception;
  
  public abstract void endMaintenance()
    throws Exception;
  
  public abstract String getServerDataAsString()
    throws Exception;
  
  public abstract String getServerVersion();
  
  public abstract boolean isAlive();
  
  public abstract String getServerStatus()
    throws Exception;
}
