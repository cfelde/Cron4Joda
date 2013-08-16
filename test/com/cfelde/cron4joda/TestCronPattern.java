package com.cfelde.cron4joda;

import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cfelde
 */
public class TestCronPattern {
    private static final int maxLoop1 = 10000;
    private static final int maxLoop2 = 1000;
    
    public TestCronPattern() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAllStars() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("* * * * *", dt);
        
        for (int i = 0; i < maxLoop1; i++) {
            dt = dt.plusMinutes(1);
            
            assertEquals(dt, predictor.nextMatchingDate());
        }
    }
    
    @Test
    public void testZeroMinute() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 * * * *", dt);
        
        for (int i = 0; i < maxLoop1; i++) {
            dt = dt.plusHours(1);
            
            assertEquals(dt, predictor.nextMatchingDate());
        }
    }
    
    @Test
    public void test10PastMinute() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("10 * * * *", dt);
        
        dt = dt.plusMinutes(10);
        
        for (int i = 0; i < maxLoop1; i++) {
            assertEquals(dt, predictor.nextMatchingDate());
            dt = dt.plusHours(1);
        }
    }
    
    @Test
    public void testZeroHour() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 * * *", dt);
        
        for (int i = 0; i < maxLoop1; i++) {
            dt = dt.plusDays(1);
            
            assertEquals(dt, predictor.nextMatchingDate());
        }
    }
    
    @Test
    public void test10PastHour() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 10 * * *", dt);
        
        dt = dt.plusHours(10);
        
        for (int i = 0; i < maxLoop1; i++) {
            assertEquals(dt, predictor.nextMatchingDate());
            dt = dt.plusDays(1);
        }
    }
    
    @Test
    public void testFirstDate() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 1 * *", dt);
        
        for (int i = 0; i < maxLoop2; i++) {
            dt = dt.plusMonths(1);
            
            assertEquals(dt, predictor.nextMatchingDate());
        }
    }
    
    @Test
    public void test10thDate() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 10 * *", dt);
        
        dt = dt.plusDays(9);
        
        for (int i = 0; i < maxLoop2; i++) {
            assertEquals(dt, predictor.nextMatchingDate());
            dt = dt.plusMonths(1);
        }
    }
    
    @Test
    public void test31thDate() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 31 * *", dt);
        
        dt = dt.plusDays(30);
        
        int count = 0;
        int lastYear = dt.getYear();
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            
            if (dt.getYear() == lastYear) {
                count++;
            } else {
                assertEquals(7, count);
                count = 1;
                lastYear = dt.getYear();
            }
        }
    }
    
    @Test
    public void testSingleDayInMonth() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 10 1-12 *", dt);
        
        dt = dt.plusDays(9);
        
        int count = 0;
        int lastYear = dt.getYear();
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            
            if (dt.getYear() == lastYear) {
                count++;
            } else {
                assertEquals(12, count);
                count = 1;
                lastYear = dt.getYear();
            }
        }
    }
    
    @Test
    public void testDoubleDayInMonth() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 10,20 1-12 *", dt);
        
        dt = dt.plusDays(9);
        
        int count = 0;
        int lastYear = dt.getYear();
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            
            if (dt.getYear() == lastYear) {
                count++;
            } else {
                assertEquals(24, count);
                count = 1;
                lastYear = dt.getYear();
            }
        }
    }
    
    @Test
    public void testOnceEveryDay() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 * * 1-7", dt);
        
        int day = predictor.nextMatchingDate().getDayOfWeek();
        
        for (int i = 0; i < maxLoop2; i++) {
            int newDay = predictor.nextMatchingDate().getDayOfWeek();
            
            if (newDay != 1) {
                assertTrue(day + 1 == newDay);
            } else {
                assertEquals(7, day);
            }
            
            day = newDay;
        }
    }
    
    @Test
    public void testFirstLastDay1() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 * * 1,7", dt);
        
        if (predictor.nextMatchingDate().getDayOfWeek() != 7)
            predictor.nextMatchingDate().getDayOfWeek();
        
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            assertEquals(1, dt.getDayOfWeek());
            dt = predictor.nextMatchingDate();
            assertEquals(7, dt.getDayOfWeek());
        }
    }
    
    @Test
    public void testFirstLastDay2() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 * * mon,sun", dt);
        
        if (predictor.nextMatchingDate().getDayOfWeek() != 7)
            predictor.nextMatchingDate().getDayOfWeek();
        
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            assertEquals(1, dt.getDayOfWeek());
            dt = predictor.nextMatchingDate();
            assertEquals(7, dt.getDayOfWeek());
        }
    }
    
    @Test
    public void testLastDayOfMonth1() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 L * *", dt);
        
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            assertEquals(dt.dayOfMonth().getMaximumValue(), dt.getDayOfMonth());
        }
    }
    
    @Test
    public void testLastDayOfMonth2() {
        DateTime dt = new DateTime(1970, 1, 1, 0, 0);
        Predictor predictor = new Predictor("0 0 L * mon,tue,fri,sun", dt);
        
        List<Integer> validDays = Arrays.asList(new Integer[] {1,2,5,7});
        for (int i = 0; i < maxLoop2; i++) {
            dt = predictor.nextMatchingDate();
            assertEquals(dt.dayOfMonth().getMaximumValue(), dt.getDayOfMonth());
            assertTrue(validDays.contains(dt.getDayOfWeek()));
        }
    }
}