package de.micromata.genome.db.jpa.tabattr.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.db.jpa.tabattr.api.AttrGroup;
import de.micromata.genome.db.jpa.tabattr.api.TimeableService;
import de.micromata.genome.db.jpa.tabattr.testentities.FooDO;
import de.micromata.genome.db.jpa.tabattr.testentities.FooTimedDO;

public class TimeableServiceImplTest
{
  private static final TimeableService<Integer, FooTimedDO> timeableService = new TimeableServiceImpl<>();

  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  private static final AttrGroup groupPeriod = new AttrGroup();
  private static final AttrGroup groupInstantOfTime = new AttrGroup();
  private static final AttrGroup groupNotTimeable = new AttrGroup();

  static {
    groupPeriod.setType(AttrGroup.Type.PERIOD);
    groupInstantOfTime.setType(AttrGroup.Type.INSTANT_OF_TIME);
    groupNotTimeable.setType(AttrGroup.Type.NOT_TIMEABLE);
  }

  @Test
  public void testGetAttrRowForDate() throws ParseException
  {
    final List<FooTimedDO> attrRows = Arrays.asList(
        createFooTimedDOWithDate("15.04.2047"), // 0
        createFooTimedDOWithDate("22.02.2017"), // 1
        createFooTimedDOWithDate("31.12.2016"), // 2
        createFooTimedDOWithDate("09.07.2016"), // 3
        createFooTimedDOWithDate("28.02.2016"), // 4
        createFooTimedDOWithDate("22.02.2016"), // 5
        createFooTimedDOWithDate("01.01.2016"), // 6
        createFooTimedDOWithDate("31.12.2015"), // 7
        createFooTimedDOWithDate("02.08.1995")  // 8
    );

    // group type period
    Assert.assertEquals(attrRows.get(0), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("01.01.2048")));
    Assert.assertEquals(attrRows.get(0), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("15.04.2047")));
    Assert.assertEquals(attrRows.get(1), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("14.04.2047")));
    Assert.assertEquals(attrRows.get(5), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("22.02.2016")));
    Assert.assertEquals(attrRows.get(6), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("21.02.2016")));
    Assert.assertEquals(attrRows.get(6), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("01.01.2016")));
    Assert.assertEquals(attrRows.get(7), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("31.12.2015")));
    Assert.assertEquals(attrRows.get(8), timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("02.08.1995")));
    Assert.assertEquals(null, timeableService.getAttrRowForDate(attrRows, groupPeriod, sdf.parse("01.08.1995")));

    // group type instant of time
    Assert.assertEquals(null, timeableService.getAttrRowForDate(attrRows, groupInstantOfTime, sdf.parse("22.02.2016")));
    Assert.assertEquals(null, timeableService.getAttrRowForDate(attrRows, groupInstantOfTime, sdf.parse("21.02.2016")));

    // group type not timeable
    boolean exceptionThrown = false;
    try {
      timeableService.getAttrRowForDate(attrRows, groupNotTimeable, sdf.parse("22.02.2016"));
    } catch (IllegalArgumentException e) {
      exceptionThrown = true;
    }
    Assert.assertTrue(exceptionThrown);
  }

  @Test
  public void testGetAttrRowForDateWithNullDate() throws ParseException
  {
    final List<FooTimedDO> attrRowsWithNullDate = Arrays.asList(
        createFooTimedDOWithDate(null),
        createFooTimedDOWithDate("22.02.2017"),
        createFooTimedDOWithDate("31.12.2016")
    );

    // should always return the row with the null date
    Assert.assertEquals(attrRowsWithNullDate.get(0), timeableService.getAttrRowForDate(attrRowsWithNullDate, groupPeriod, sdf.parse("31.12.2016")));
    Assert.assertEquals(attrRowsWithNullDate.get(0), timeableService.getAttrRowForDate(attrRowsWithNullDate, groupPeriod, sdf.parse("01.01.2015")));
  }

  @Test
  public void testGetTimeableAttrRowsForGroup()
  {
    final FooDO fooDO = new FooDO();
    fooDO.addTimeableAttribute(createFooTimedDO("foo"));
    fooDO.addTimeableAttribute(createFooTimedDO("foo"));
    fooDO.addTimeableAttribute(createFooTimedDO("bar"));
    fooDO.addTimeableAttribute(createFooTimedDO("baz"));
    fooDO.addTimeableAttribute(createFooTimedDO("bar"));
    fooDO.addTimeableAttribute(createFooTimedDO("foo"));
    fooDO.addTimeableAttribute(createFooTimedDO("baz"));
    fooDO.addTimeableAttribute(createFooTimedDO("foo"));
    fooDO.addTimeableAttribute(createFooTimedDO("baz"));

    final AttrGroup group = new AttrGroup();
    group.setName("foo");
    final List<FooTimedDO> foos = timeableService.getTimeableAttrRowsForGroup(fooDO, group);
    Assert.assertEquals(4, foos.size());
    Assert.assertTrue(foos.stream().allMatch(e -> e.getGroupName().equals("foo")));

    group.setName("bar");
    final List<FooTimedDO> bars = timeableService.getTimeableAttrRowsForGroup(fooDO, group);
    Assert.assertEquals(2, bars.size());
    Assert.assertTrue(bars.stream().allMatch(e -> e.getGroupName().equals("bar")));

    group.setName("baz");
    final List<FooTimedDO> bazs = timeableService.getTimeableAttrRowsForGroup(fooDO, group);
    Assert.assertEquals(3, bazs.size());
    Assert.assertTrue(bazs.stream().allMatch(e -> e.getGroupName().equals("baz")));

    group.setName(null);
    final List<FooTimedDO> shouldBeEmpty = timeableService.getTimeableAttrRowsForGroup(fooDO, group);
    Assert.assertTrue(shouldBeEmpty.isEmpty());
  }

  @Test
  public void testSortTimeableAttrRowsByDateDescending() throws ParseException
  {
    final List<FooTimedDO> sortedExpected = Arrays.asList(
        createFooTimedDOWithDate(null),
        createFooTimedDOWithDate("15.04.2047"),
        createFooTimedDOWithDate("22.02.2017"),
        createFooTimedDOWithDate("31.12.2016"),
        createFooTimedDOWithDate("09.07.2016"),
        createFooTimedDOWithDate("28.02.2016"),
        createFooTimedDOWithDate("22.02.2016"),
        createFooTimedDOWithDate("01.01.2016"),
        createFooTimedDOWithDate("31.12.2015"),
        createFooTimedDOWithDate("02.08.1995")
    );

    final List<FooTimedDO> unsorted = Arrays.asList(
        sortedExpected.get(8),
        sortedExpected.get(7),
        sortedExpected.get(1),
        sortedExpected.get(3),
        sortedExpected.get(2),
        sortedExpected.get(9),
        sortedExpected.get(5),
        sortedExpected.get(0),
        sortedExpected.get(4),
        sortedExpected.get(6)
    );

    final List<FooTimedDO> sortedActual = timeableService.sortTimeableAttrRowsByDateDescending(unsorted);

    Assert.assertArrayEquals(sortedExpected.toArray(), sortedActual.toArray());
  }

  @Test
  public void testSortTimeableAttrRowsByDateDescendingWithEmptyList() throws ParseException
  {
    final List<FooTimedDO> shouldBeEmpty = timeableService.sortTimeableAttrRowsByDateDescending(Collections.emptyList());
    Assert.assertTrue(shouldBeEmpty.isEmpty());
  }

  private FooTimedDO createFooTimedDO(final String groupName)
  {
    final FooTimedDO fooTimedDO = new FooTimedDO();
    fooTimedDO.setGroupName(groupName);
    return fooTimedDO;
  }

  private FooTimedDO createFooTimedDOWithDate(final String date) throws ParseException
  {
    final FooTimedDO fooTimedDO = new FooTimedDO();
    if (date != null) {
      fooTimedDO.setStartTime(sdf.parse(date));
    }
    return fooTimedDO;
  }

}
