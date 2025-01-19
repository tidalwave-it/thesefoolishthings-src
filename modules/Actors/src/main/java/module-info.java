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
module it.tidalwave.actor
  {
    requires static lombok;
    requires jakarta.annotation;
    // requires static jsr305; // Cannot be used because packages clash with java.annotation
    requires static java.annotation;
    requires org.slf4j;
    requires javax.inject;
    requires java.management;
    requires it.tidalwave.util;
    requires it.tidalwave.messagebus;
    exports it.tidalwave.actor;
    exports it.tidalwave.actor.annotation;
    exports it.tidalwave.actor.spi;
  }