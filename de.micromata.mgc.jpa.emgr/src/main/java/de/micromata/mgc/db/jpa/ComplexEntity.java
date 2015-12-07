package de.micromata.mgc.db.jpa;

/**
 * Visitor to iterate through complex (linked) entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface ComplexEntity
{
  /**
   * Should be called for each entity
   * 
   * @param visitor
   */
  public void visit(ComplexEntityVisitor visitor);
}
