package com.semmet.personattribute.personattribute.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AppLogger class provides just provides you a Logger object, available to
 * all the classes so you don't have to create a new one for every class.
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

public class AppLogger {
   public static final Logger LOGGER = LoggerFactory.getLogger(AppLogger.class);

   private AppLogger() {
   }
}
