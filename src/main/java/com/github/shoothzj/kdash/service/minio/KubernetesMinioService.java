/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.shoothzj.kdash.service.minio;

import com.github.shoothzj.kdash.module.minio.CreateMinioDashboardReq;
import com.github.shoothzj.kdash.module.minio.CreateMinioReq;
import com.github.shoothzj.kdash.service.KubernetesDeployService;
import com.github.shoothzj.kdash.service.KubernetesServiceService;
import com.github.shoothzj.kdash.service.KubernetesStatefulSetService;
import com.github.shoothzj.kdash.util.KubernetesUtil;
import com.github.shoothzj.kdash.util.MinioUtil;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KubernetesMinioService {

    @Autowired
    private KubernetesStatefulSetService statefulSetService;

    @Autowired
    private KubernetesDeployService deployService;

    @Autowired
    private KubernetesServiceService serviceService;

    public void createMinio(String namespace, CreateMinioReq req) throws ApiException {
        serviceService.createService(namespace, MinioUtil.service(req));
        statefulSetService.createNamespacedStatefulSet(namespace, MinioUtil.statefulSet(req));
    }

    public void deleteMinio(String namespace, String name) throws ApiException {
        serviceService.deleteService(namespace, KubernetesUtil.name("minio", name));
        statefulSetService.deleteStatefulSet(namespace, KubernetesUtil.name("minio", name));
    }

    public void createDashboard(String namespace, CreateMinioDashboardReq req) throws ApiException {
        deployService.createNamespacedDeploy(namespace, MinioUtil.dashboardDeploy(req));
    }

    public void deleteDashboard(String namespace, String dashboardName) throws ApiException {
        deployService.deleteDeploy(namespace, KubernetesUtil.name("minio-dashboard", dashboardName));
    }
}
