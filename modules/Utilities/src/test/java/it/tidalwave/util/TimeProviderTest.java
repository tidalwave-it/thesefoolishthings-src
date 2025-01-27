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
package it.tidalwave.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class TimeProviderTest
  {
    /**********************************************************************************************************************************************************/
    @Test
    public void test()
      {
        // given
        final var underTest = TimeProvider.getInstance();
        // when
        final var nowInstant = underTest.currentInstant();
        final var nowLocal = underTest.currentLocalDateTime();
        final var nowZoned = underTest.currentZonedDateTime();
        // then
        final var zoneId = ZoneId.systemDefault();
        final var offset = zoneId.getRules().getStandardOffset(nowInstant);
        assertThat(getDeltaMillis(nowInstant, nowLocal, offset)).isLessThan(1L);
        assertThat(getDeltaMillis(nowInstant, nowZoned)).isLessThan(1L);
      }

    private static long getDeltaMillis (final Instant i, final LocalDateTime ldt, final ZoneOffset offset)
      {
        return Duration.between(ldt.toInstant(offset), i).toMillis();
      }

    private static long getDeltaMillis (final Instant i, final ZonedDateTime zdt)
      {
        return Duration.between(zdt.toInstant(), i).toMillis();
      }
  }
