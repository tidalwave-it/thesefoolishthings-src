/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 **********************************************************************************************************************/
public class StopWatchStats implements Comparable<StopWatchStats>
  {
    private final static String CLASS = StopWatchStats.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);

    private final static Map<String, StopWatchStats> stopWatchStatsMapByName = new HashMap<String, StopWatchStats>();

    private final String name;

    private long accumulatedTime;

    private long minAccumulatedTime = Long.MAX_VALUE;

    private long maxAccumulatedTime = Long.MIN_VALUE;

    private long elapsedTime;

    private long minElapsedTime = Long.MAX_VALUE;

    private long maxElapsedTime = Long.MIN_VALUE;

    private int sampleCount;

    public synchronized static StopWatchStats find (final String name)
      {
        StopWatchStats stopWatchStats = stopWatchStatsMapByName.get(name);

        if (stopWatchStats == null)
          {
            stopWatchStats = new StopWatchStats(name);
            stopWatchStatsMapByName.put(name, stopWatchStats);
          }

        return stopWatchStats;
      }

    public long getAccumulatedTime()
      {
        return accumulatedTime;
      }

    public double getAvgAccumulatedTime()
      {
        return (double)accumulatedTime / sampleCount;
      }

    public long getElapsedTime()
      {
        return elapsedTime;
      }

    public long getMaxAccumulatedTime()
      {
        return maxAccumulatedTime;
      }

    public long getMaxElapsedTime()
      {
        return maxElapsedTime;
      }

    public long getMinAccumulatedTime()
      {
        return minAccumulatedTime;
      }

    public long getMinElapsedTime()
      {
        return minElapsedTime;
      }

    public double getAvgElapsedTime()
      {
        return (double)elapsedTime / sampleCount;
      }

    public String getName()
      {
        return name;
      }

    public int getSampleCount()
      {
        return sampleCount;
      }

    protected StopWatchStats (final String name)
      {
        this.name = name;
      }

    protected synchronized  void addTimeSample (final long accumulatedTimeSample, final long elapsedTimeSample)
      {
        accumulatedTime += accumulatedTimeSample;
        minAccumulatedTime = Math.min(minAccumulatedTime, accumulatedTimeSample);
        maxAccumulatedTime = Math.max(maxAccumulatedTime, accumulatedTimeSample);
        elapsedTime += elapsedTimeSample;
        minElapsedTime = Math.min(minElapsedTime, elapsedTimeSample);
        maxElapsedTime = Math.max(maxElapsedTime, elapsedTimeSample);

        sampleCount++;
      }

    public static SortedSet<StopWatchStats> findAllStats()
      {
        final TreeSet<StopWatchStats> result = new TreeSet<StopWatchStats>();
        result.addAll(stopWatchStatsMapByName.values());
        return result;
      }

    public static void dump()
      {
        final double scale = 1000000;

        for (final StopWatchStats stats : findAllStats())
          {
            final String template = "STATS: %-120s [%4d samples]: acc: %9.2f/%7.2f/%7.2f/%7.2f msec, "
                                                               + "ela: %9.2f/%7.2f/%7.2f/%7.2f msec (val/min/max/avg)";
            logger.info(String.format(template,
                                      stats.getName(),
                                      stats.getSampleCount(),
                                      stats.getAccumulatedTime() / scale,
                                      stats.getMinAccumulatedTime() / scale,
                                      stats.getMaxAccumulatedTime() / scale,
                                      stats.getAvgAccumulatedTime() / scale,
                                      stats.getElapsedTime() / scale,
                                      stats.getMinElapsedTime() / scale,
                                      stats.getMaxElapsedTime() / scale,
                                      stats.getAvgElapsedTime() / scale));
          }
      }

    @Override
    public boolean equals (final Object obj)
      {
        if (obj == null)
          {
            return false;
          }

        if (getClass() != obj.getClass())
          {
            return false;
          }

        final StopWatchStats other = (StopWatchStats) obj;

        if (!this.name.equals(other.name))
          {
            return false;
          }

        return true;
      }

    @Override
    public int hashCode()
      {
        int hash = 7;
        hash = 53 * hash + this.name.hashCode();
        return hash;
      }

    public int compareTo (final StopWatchStats other)
      {
        return this.name.compareTo(other.name);
      }

    @Override
    public String toString()
      {
        return String.format("StopWatch[%s accTime: %d, samples: %d]", name, accumulatedTime, sampleCount);
      }
  }
