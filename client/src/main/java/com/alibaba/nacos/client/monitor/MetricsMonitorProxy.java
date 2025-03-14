/*
 * Copyright 1999-2025 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.client.monitor;

import com.alibaba.nacos.client.naming.utils.UtilAndComs;

public class MetricsMonitorProxy {
    public static final boolean ENABLE_METRICS_MONITOR = Boolean.parseBoolean(
            System.getProperty(UtilAndComs.ENABLE_METRICS_MONITOR, "true"));
    
    public static void updateServiceInfoMapSize(int size) {
        if (ENABLE_METRICS_MONITOR) {
            MetricsMonitor.getServiceInfoMapSizeMonitor().set(size);
        }
    }
    
    public static void updateListenConfigCount(int count) {
        if (ENABLE_METRICS_MONITOR) {
            MetricsMonitor.getListenConfigCountMonitor().set(count);
        }
    }
    
    public static void updateConfigRequestObserve(String method, String url, String code, double amt) {
        if (ENABLE_METRICS_MONITOR) {
            MetricsMonitor.getConfigRequestMonitor(method, url, code).observe(amt);
        }
    }
    
    public static void updateNamingRequestObserve(String method, String url, String code, double amt) {
        if (ENABLE_METRICS_MONITOR) {
            MetricsMonitor.getNamingRequestMonitor(method, url, code).observe(amt);
        }
    }
    
    public static void namingRequestFailed(String reqClass, String resStatus, String resCode, String errClass) {
        if (ENABLE_METRICS_MONITOR) {
            MetricsMonitor.getNamingRequestFailedMonitor(reqClass, resStatus, resCode, errClass).inc();
        }
    }
}
