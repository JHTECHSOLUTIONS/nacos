/*
 * Copyright 1999-2024 Alibaba Group Holding Ltd.
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
 *
 */

package com.alibaba.nacos.console.controller.v3.core;

import com.alibaba.nacos.api.annotation.NacosApi;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.model.response.NacosMember;
import com.alibaba.nacos.api.model.v2.Result;
import com.alibaba.nacos.api.remote.RemoteConstants;
import com.alibaba.nacos.auth.annotation.Secured;
import com.alibaba.nacos.console.proxy.core.ClusterProxy;
import com.alibaba.nacos.core.utils.Commons;
import com.alibaba.nacos.plugin.auth.constant.ActionTypes;
import com.alibaba.nacos.plugin.auth.constant.ApiType;
import com.alibaba.nacos.plugin.auth.constant.SignType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller for handling HTTP requests related to cluster operations.
 *
 * @author zhangyukun on:2024/8/16
 */
@NacosApi
@RestController
@RequestMapping("/v3/console/core/cluster")
@Tag(name = "nacos.console.core.cluster.api.controller.name", description = "nacos.console.core.cluster.api.controller.description", extensions = {
        @Extension(name = RemoteConstants.LABEL_MODULE, properties = @ExtensionProperty(name = RemoteConstants.LABEL_MODULE, value = "common"))})
public class ConsoleClusterController {
    
    private final ClusterProxy clusterProxy;
    
    /**
     * Constructs a new ConsoleClusterController with the provided ClusterProxy.
     *
     * @param clusterProxy the proxy used for handling cluster-related operations
     */
    public ConsoleClusterController(ClusterProxy clusterProxy) {
        this.clusterProxy = clusterProxy;
    }
    
    /**
     * The console displays the list of cluster members.
     *
     * @param ipKeyWord search keyWord
     * @return all members
     */
    @GetMapping(value = "/nodes")
    @Secured(resource = Commons.NACOS_CORE_CONTEXT
            + "/cluster", action = ActionTypes.READ, signType = SignType.CONSOLE, apiType = ApiType.CONSOLE_API)
    @Operation(summary = "nacos.console.core.cluster.api.nodes.summary", description = "nacos.console.core.cluster.api.nodes.description",
            security = @SecurityRequirement(name = "nacos"))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Result.class, example = "nacos.console.core.cluster.api.nodes.example")))
    public Result<Collection<NacosMember>> getNodeList(
            @RequestParam(value = "keyword", required = false) String ipKeyWord) throws NacosException {
        Collection<NacosMember> result = clusterProxy.getNodeList(ipKeyWord);
        return Result.success(result);
    }
}
