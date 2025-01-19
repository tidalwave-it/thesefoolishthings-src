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
module it.tidalwave.util
  {
    requires static lombok;
    // requires static jsr305;
    requires static java.annotation;
    requires jakarta.annotation;
    requires static com.github.spotbugs.annotations;
    requires org.slf4j;
    exports it.tidalwave.util;
    exports it.tidalwave.util.annotation;
    exports it.tidalwave.util.spi;
    exports it.tidalwave.util.thread;
    exports it.tidalwave.util.thread.annotation;
    exports it.tidalwave.dci.annotation;
    exports it.tidalwave.role.spi;
    exports it.tidalwave.role.spi.annotation;
    provides it.tidalwave.role.spi.ContextManagerProvider with it.tidalwave.util.impl.DefaultContextManagerProvider;
    provides it.tidalwave.role.spi.OwnerRoleFactoryProvider with it.tidalwave.util.impl.DefaultOwnerRoleFactoryProvider;
    provides it.tidalwave.role.spi.SystemRoleFactoryProvider with it.tidalwave.util.impl.DefaultSystemRoleFactoryProvider;
    provides it.tidalwave.util.PreferencesHandler with it.tidalwave.util.impl.DefaultPreferencesHandler;
    uses it.tidalwave.role.spi.ContextManagerProvider;
    uses it.tidalwave.role.spi.OwnerRoleFactoryProvider;
    uses it.tidalwave.role.spi.SystemRoleFactoryProvider;
    uses it.tidalwave.util.PreferencesHandler;
  }