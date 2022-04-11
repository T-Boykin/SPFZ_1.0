package com.dev.swapftrz.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class SPFZDBOperations implements java.sql.Driver
{
   private final SPFZResourceManager resourceManager;

   public SPFZDBOperations(SPFZResourceManager resourceManager) {
      //Context c = //Setup constructor to accept android context
      // Setup connection to SPFZ database
      this.resourceManager = resourceManager;
      createDBConnection(urlString());
   }

   public String urlString() {
      int device = resourceManager.appDevice();
      String file = resourceManager.preferencesFile();
      Preferences spfzprefs = Gdx.app.getPreferences(file);
      String driver = spfzprefs.getString("driver");
      String assetPath = spfzprefs.getString("assetPath");
      String androidDevicePath = spfzprefs.getString("androidDevicePath");
      String desktopPath = spfzprefs.getString("desktopPath");
      String filePath;

      filePath = spfzprefs.getString(driver);

      if (device == resourceManager.ANDROID)
      {
         filePath = filePath.concat(androidDevicePath);
      }
      else if (device == resourceManager.DESKTOP)
      {
         filePath = filePath.concat(desktopPath);
      }
      else
      {
         filePath = "N/A";
      }

      filePath = filePath.concat(assetPath.concat(file));

      return filePath;
   }

   public void createDBConnection(String url) {
      Connection c = null;
      try
      {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection(url, "", "");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
      System.out.println("1st open successful");
      //init();
   }

   @Override
   public Connection connect(String s, Properties properties) throws SQLException {
      return null;
   }

   @Override
   public boolean acceptsURL(String s) throws SQLException {
      return false;
   }

   @Override
   public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
      return new DriverPropertyInfo[0];
   }

   @Override
   public int getMajorVersion() {
      return 0;
   }

   @Override
   public int getMinorVersion() {
      return 0;
   }

   @Override
   public boolean jdbcCompliant() {
      return false;
   }

   @Override
   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      return null;
   }
}
