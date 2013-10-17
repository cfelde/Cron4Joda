/*
 * cron4joda - A pure Joda Time cron pattern parser
 * 
 * This is a fork of cron4j: http://www.sauronsoftware.it/projects/cron4j/
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * Copyright (C) 2013      Christian Felde (cfelde.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.cfelde.cron4joda;

import org.joda.time.DateTime;

/**
 * <p>
 * A predictor is able to predict when a scheduling pattern will be matched.
 * </p>
 * <p>
 * Suppose you want to know when the scheduler will execute a task scheduled
 * with the pattern <em>0 3 * jan-jun,sep-dec mon-fri</em>. You can predict the
 * next <em>n</em> execution of the task using a Predictor instance:
 * </p>
 *
 * <pre>
 * String pattern = &quot;0 3 * jan-jun,sep-dec mon-fri&quot;;
 * Predictor p = new Predictor(pattern);
 * for (int i = 0; i &lt; n; i++) {
 * 	System.out.println(p.nextMatchingDate());
 * }
 * </pre>
 *
 * @author Carlo Pelliccia
 * @author Christian Felde
 * @since 1.1
 */
public class Predictor {
    /**
     * The scheduling pattern on which the predictor works.
     */
    private final SchedulingPattern schedulingPattern;
    
    /**
     * The start time for the next prediction.
     */
    private DateTime dt;

    /**
     * It builds a predictor with the given scheduling pattern and start time.
     * 
     * The time zone setting of the given DateTime will be used as the basis
     * of the cron pattern.
     *
     * @param schedulingPattern The pattern on which the prediction will be
     * based.
     * @param start The start time of the prediction.
     * @throws InvalidPatternException In the given scheduling pattern isn't
     * valid.
     */
    public Predictor(String schedulingPattern, DateTime start)
            throws InvalidPatternException {
        this(new SchedulingPattern(schedulingPattern), start);
    }
    
    /**
     * It builds a predictor with the given scheduling pattern and start time.
     * 
     * The time zone setting of the given DateTime will be used as the basis
     * of the cron pattern.
     *
     * @param schedulingPattern The pattern on which the prediction will be
     * based.
     * @param start The start time of the prediction.
     * @since 2.0
     */
    public Predictor(SchedulingPattern schedulingPattern, DateTime start) {
        this.schedulingPattern = schedulingPattern;
        this.dt = start.withMillisOfSecond(0).withSecondOfMinute(0);
    }

    /**
     * It returns the next matching moment as a {@link DateTime} object.
     *
     * @return The next matching moment as a {@link DateTime} object.
     */
    public synchronized DateTime nextMatchingDate() {
        do {
            dt = dt.plusMinutes(1);
        } while (!schedulingPattern.match(dt));
        
        return dt;
    }
}
