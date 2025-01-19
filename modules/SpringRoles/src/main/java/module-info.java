/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
module it.tidalwave.role.spring
  {
    requires static lombok;
    // requires static jsr305; cannot be used because packages overlap with java.annotation
    requires static javax.inject;
    requires static org.aspectj.weaver;
    requires static java.annotation;
    requires jakarta.annotation;
    requires transitive jakarta.inject;
    requires static com.github.spotbugs.annotations;
    requires org.slf4j;
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires spring.aspects;
    requires transitive it.tidalwave.util;
    requires transitive it.tidalwave.role;
    opens it.tidalwave.role.spring to spring.core, spring.context, spring.beans;
    opens it.tidalwave.role.spring.spi to spring.core, spring.context, spring.beans;
    provides it.tidalwave.role.spi.SystemRoleFactoryProvider with it.tidalwave.role.spring.spi.SpringSystemRoleFactoryProvider;
  }