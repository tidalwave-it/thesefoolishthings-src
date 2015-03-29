/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
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
 * *********************************************************************************************************************
 * 
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.spring;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

/***********************************************************************************************************************
 *
 * A utility for scanning classes in the classpath with some criteria.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ClassScanner
  {
    private final String basePackages = System.getProperty(ClassScanner.class.getCanonicalName() + ".basePackages",
                                                           "com:org:it"); // FIME

    private final ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);

    /*******************************************************************************************************************
     *
     * Scans for classes and returns them.
     *
     * @return  the collection of scanned classes
     *
     ******************************************************************************************************************/
    @Nonnull
    public final Collection<Class<?>> findClasses()
      {
        final List<Class<?>> classes = new ArrayList<Class<?>>();

        for (final String basePackage : basePackages.split(":"))
          {
            for (final BeanDefinition candidate : scanner.findCandidateComponents(basePackage))
              {
                classes.add(ClassUtils.resolveClassName(candidate.getBeanClassName(),
                            ClassUtils.getDefaultClassLoader()));
              }
          }

        return classes;
      }

    /*******************************************************************************************************************
     *
     * Adds an "include" filter.
     *
     * @param  filter  the filter
     *
     ******************************************************************************************************************/
    @Nonnull
    public ClassScanner withIncludeFilter (final @Nonnull TypeFilter filter)
      {
        scanner.addIncludeFilter(filter);
        return this;
      }

    /*******************************************************************************************************************
     *
     * Adds a filter for an annotation.
     *
     * @param  annotationClass  the annotation class
     *
     ******************************************************************************************************************/
    @Nonnull
    public ClassScanner withAnnotationFilter (final @Nonnull Class<? extends Annotation> annotationClass)
      {
        return withIncludeFilter(new AnnotationTypeFilter(annotationClass));
      }
  }
