/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.ui.vaadin;

import com.github.wolfie.refresher.Refresher;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Gabriele Cuccu <gabriele.cuccu@gmail.com>
 */
@RequiredArgsConstructor
@Slf4j
public class RefresherQueue {

    @Nonnull
    private final Refresher refresher;
    private boolean started;
    private final Queue<Runnable> pendingJobs = new ConcurrentLinkedQueue<Runnable>();
    private final Refresher.RefreshListener listener = new Refresher.RefreshListener() {

        @Override
        public void refresh(Refresher source) {
//            log.info("executing pending jobs...");

            for (;;) {
                Runnable job = pendingJobs.poll();

                if (job == null) {
                    break;
                }

                log.debug(">>>> executing pending job: {} ...", job);
                job.run();
            }
        }
    };

    public void start() {
        log.info("start()");
        refresher.addListener(listener);
    }

    public void stop() {
        log.info("stop()");
        refresher.removeListener(listener);


        synchronized (this) {
            if (started) {
                refresher.setRefreshInterval(0);
                started = false;
                log.info(">>>> stopped refresher");
            }
        }
    }

    public void invokeLater(final @Nonnull Runnable job) {
        log.info("invokeLater({})", job);
        pendingJobs.add(job);

        synchronized (this) {
            if (!started) {
                log.debug(">>>> started refresher");
                refresher.setRefreshInterval(500);
                started = true;
            }
        }
    }
}
