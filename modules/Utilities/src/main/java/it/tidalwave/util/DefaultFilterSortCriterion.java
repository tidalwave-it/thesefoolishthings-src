/***********************************************************************************************************************
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import it.tidalwave.util.Finder.SortDirection;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft
 *
 **********************************************************************************************************************/
public class DefaultFilterSortCriterion<T> implements Finder.FilterSortCriterion<T> 
  {
    @Nonnull
    private final String name;
    
    @Nonnull
    private final Comparator<? super T> comparator;

    public DefaultFilterSortCriterion (final @Nonnull String name, final @Nonnull Comparator<? super T> comparator) 
      {
        this.name = name;
        this.comparator = comparator;
      }
    
    @Override @Nonnull
    public Finder<T> sort (final @Nonnull Finder<T> finder,
                           final @Nonnull SortDirection sortDirection)
      {
        return new FinderSupport<T>(name) 
          {
            @Override @Nonnull
            protected List<? extends T> doCompute() 
              {
                final List<? extends T> results = finder.results();
                Collections.sort(results, new Comparator<T>()
                  {
                    public int compare (final @Nonnull T o1, final @Nonnull T o2) 
                      {
                        return comparator.compare(o1, o2) * sortDirection.intValue();
                      }
                  });
                
                return results;
              }
          };
      }
  }
