/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.logging.task;

import org.slf4j.Logger;

import static org.sonatype.nexus.logging.task.TaskLogType.BOTH;

/**
 * Factory to create {@link TaskLogger} instances
 *
 * @since 3.5
 */
public class TaskLoggerFactory
{
  private TaskLoggerFactory() {
    throw new IllegalAccessError("Utility class");
  }

  public static TaskLogger create(final Object taskObject, final Logger log, final TaskLogInfo taskLogInfo) {
    TaskLogging taskLogging = taskObject.getClass().getAnnotation(TaskLogging.class);

    if (taskLogging == null) {
      taskLogging = TaskLoggingDefault.class.getAnnotation(TaskLogging.class);
    }

    switch (taskLogging.value()) {
      case NEXUS_LOG_ONLY:
        return new NoOpTaskLogger();
      case TASK_LOG_ONLY:
        return new TaskLogOnlyTaskLogger(log, taskLogInfo);
      case BOTH:
      default:
        return new DefaultTaskLogger(log, taskLogInfo);
    }
  }

  @TaskLogging(BOTH)
  private static final class TaskLoggingDefault { }
}
