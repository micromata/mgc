package de.micromata.genome.jpa;

/**
 * Internal visitor to an entity to update records.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface ComplexEntityVisitor
{
  /**
   * Visit one entity. Internal nested entities should be visited to
   * 
   * @param rec
   */
  public void visit(DbRecord rec);
}
