package de.micromata.genome.util.runtime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenericUtilsTest
{

  @Test
  public void testFieldGeneric()
  {
    Field pkkf = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "pkk");
    Class<?> concr = GenericsUtils.getConcreteFieldType(TypedDoubleGenerics.class, pkkf);
    Assert.assertEquals(concr, Long.class);

    Field yp = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "ypg");
    concr = GenericsUtils.getConcreteFieldType(TypedDoubleGenerics.class, yp);
    Assert.assertEquals(concr, String.class);
  }

  @Test
  public void testFieldGeneric1()
  {
    Field pkkf = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "pkk");
    Field yp = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "ypg");
    Class<?> concr = GenericsUtils.getConcreteFieldType(Concrete1PartGenerics.class, pkkf);
    Assert.assertEquals(concr, Date.class);

    Class<?> concr2 = GenericsUtils.getConcreteFieldType(Concrete1PartGenerics.class, yp);
    Assert.assertEquals(concr2, String.class);
  }

  @Test
  public void testFieldGeneric2()
  {
    Field pkkf = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "pkk");
    Field yp = PrivateBeanUtils.findField(TypedDoubleGenerics.class, "ypg");
    Class<?> concr = GenericsUtils.getConcreteFieldType(Concrete2PartGenerics.class, pkkf);
    Assert.assertEquals(Long.class, concr);

    Class<?> concr2 = GenericsUtils.getConcreteFieldType(Concrete2PartGenerics.class, yp);
    Assert.assertEquals(Integer.class, concr2);
  }

  @Test
  public void testSimple()
  {
    SimpleClass ns = new SimpleClass();
    Class<?> stringcl = PrivateBeanUtils.getFieldType(ns, "stringField");
    Assert.assertEquals(String.class, stringcl);
    Class<?> doubleGenericscls = PrivateBeanUtils.getFieldType(ns, "doubleGenerics");
    Assert.assertEquals(DoubleGenerics.class, doubleGenericscls);

  }

  static class SimpleClass
  {
    String stringField;
    DoubleGenerics<Long, String> doubleGenerics;
  }

  static class DoubleGenerics<PKK extends Serializable, YPG extends Serializable>
  {

    PKK pkk;
    YPG ypg;

    public YPG getYpg()
    {
      return ypg;
    }

    public void setYpg(YPG ypg)
    {
      this.ypg = ypg;
    }

    public PKK getPkk()
    {
      return pkk;
    }

    public void setPkk(PKK pkk)
    {
      this.pkk = pkk;
    }
  }

  static class AbstractTypedDoubleGenerics<APKK extends Serializable, AYPG extends Serializable>
      extends DoubleGenerics<APKK, AYPG>
  {

  }

  static class Concrete1PartGenerics extends Part1Generics<Date>
  {

  }

  static class Concrete2PartGenerics extends Part2Generics<Integer>
  {

  }

  static class Part1Generics<APK extends Serializable>extends AbstractTypedDoubleGenerics<APK, String>
  {

  }

  static class Part2Generics<X extends Serializable>extends AbstractTypedDoubleGenerics<Long, X>
  {

  }

  static class TypedDoubleGenerics extends AbstractTypedDoubleGenerics<Long, String>
  {

  }

  static class DoubleDerived extends TypedDoubleGenerics
  {

  }
}
