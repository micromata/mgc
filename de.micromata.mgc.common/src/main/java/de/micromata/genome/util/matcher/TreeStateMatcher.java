package de.micromata.genome.util.matcher;

/**
 * The Class TreeStateMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class TreeStateMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6117823200897120959L;

  /**
   * The value.
   */
  private boolean value;

  /**
   * The nested.
   */
  private Matcher<T> nested;

  /**
   * Instantiates a new tree state matcher.
   */
  public TreeStateMatcher()
  {

  }

  /**
   * Instantiates a new tree state matcher.
   *
   * @param nested the nested
   * @param value the value
   */
  public TreeStateMatcher(Matcher<T> nested, boolean value)
  {
    this.nested = nested;
    this.value = value;
  }

  @Override
  public MatchResult apply(T token)
  {
    MatchResult mr = nested.apply(token);
    if (mr == MatchResult.NoMatch) {
      return mr;
    }
    return value ? MatchResult.MatchPositive : MatchResult.MatchNegative;
  }

  @Override
  public boolean match(T token)
  {
    return apply(token) == MatchResult.MatchPositive;
  }

  @Override
  public String toString()
  {
    String s = "<EXPR>.match(" + nested.toString() + ")";
    if (value == false) {
      s = "!" + s;
    }
    return s;
  }

  public boolean isValue()
  {
    return value;
  }

  public void setValue(boolean value)
  {
    this.value = value;
  }

  public Matcher<T> getNested()
  {
    return nested;
  }

  public void setNested(Matcher<T> nested)
  {
    this.nested = nested;
  }

}
