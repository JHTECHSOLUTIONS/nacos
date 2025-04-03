/*
 * Copyright 1999-$toady.year Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.naming.controllers.v3;

import com.alibaba.nacos.api.annotation.NacosApi;
import com.alibaba.nacos.api.exception.api.NacosApiException;
import com.alibaba.nacos.api.model.v2.ErrorCode;
import com.alibaba.nacos.api.model.v2.Result;
import com.alibaba.nacos.api.naming.pojo.maintainer.MetricsInfo;
import com.alibaba.nacos.api.remote.RemoteConstants;
import com.alibaba.nacos.auth.annotation.Secured;
import com.alibaba.nacos.core.paramcheck.ExtractorManager;
import com.alibaba.nacos.naming.core.Operator;
import com.alibaba.nacos.naming.misc.SwitchDomain;
import com.alibaba.nacos.naming.misc.UtilsAndCommons;
import com.alibaba.nacos.naming.model.form.UpdateSwitchForm;
import com.alibaba.nacos.naming.model.vo.MetricsInfoVo;
import com.alibaba.nacos.naming.paramcheck.NamingDefaultHttpParamExtractor;
import com.alibaba.nacos.plugin.auth.constant.ActionTypes;
import com.alibaba.nacos.plugin.auth.constant.ApiType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Operator controller.
 *
 * @author Nacos
 */
@NacosApi
@RestController
@RequestMapping(UtilsAndCommons.OPERATOR_CONTROLLER_V3_ADMIN_PATH)
@ExtractorManager.Extractor(httpExtractor = NamingDefaultHttpParamExtractor.class)
@Tag(name = "nacos.admin.naming.ops.api.controller.name", description = "nacos.admin.naming.ops.api.controller.description", extensions = {
        @Extension(name = RemoteConstants.LABEL_MODULE,
                properties = @ExtensionProperty(name = RemoteConstants.LABEL_MODULE, value = RemoteConstants.LABEL_MODULE_NAMING))})
public class OperatorControllerV3 {
    
    private final Operator operatorV2Impl;
    
    public OperatorControllerV3(Operator operatorV2Impl) {
        this.operatorV2Impl = operatorV2Impl;
    }
    
    /**
     * Get switch information.
     */
    @GetMapping("/switches")
    @Secured(resource = UtilsAndCommons.OPERATOR_CONTROLLER_V3_ADMIN_PATH, action = ActionTypes.READ, apiType = ApiType.ADMIN_API)
    @Operation(summary = "nacos.admin.naming.ops.api.switches.get.summary", description = "nacos.admin.naming.ops.api.switches.get.description",
            security = @SecurityRequirement(name = "nacos"))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Result.class, example = "nacos.admin.naming.ops.api.switches.get.example")))
    public Result<SwitchDomain> switches() {
        return Result.success(operatorV2Impl.switches());
    }
    
    /**
     * Update switch information.
     */
    @PutMapping("/switches")
    @Secured(resource = UtilsAndCommons.OPERATOR_CONTROLLER_V3_ADMIN_PATH, action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
    @Operation(summary = "nacos.admin.naming.ops.api.switches.update.summary",
            description = "nacos.admin.naming.ops.api..switches.update.description", security = @SecurityRequirement(name = "nacos"))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Result.class, example = "nacos.admin.naming.ops.api.switches.update.example")))
    @Parameters(value = {@Parameter(name = "entry", required = true, example = "pushEnabled"),
            @Parameter(name = "value", required = true, example = "true"),
            @Parameter(name = "debug", example = "false"), @Parameter(name = "updateSwitchForm", hidden = true)})
    public Result<String> updateSwitch(UpdateSwitchForm updateSwitchForm) throws Exception {
        updateSwitchForm.validate();
        try {
            operatorV2Impl.updateSwitch(updateSwitchForm.getEntry(), updateSwitchForm.getValue(), updateSwitchForm.getDebug());
            return Result.success("ok");
        } catch (IllegalArgumentException e) {
            throw new NacosApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SERVER_ERROR,
                    e.getMessage());
        }
    }
    
    /**
     * Get metrics information.
     */
    @GetMapping("/metrics")
    @Secured(resource = UtilsAndCommons.OPERATOR_CONTROLLER_V3_ADMIN_PATH, action = ActionTypes.READ, apiType = ApiType.ADMIN_API)
    @Operation(summary = "nacos.admin.naming.ops.api.metrics.summary", description = "nacos.admin.naming.ops.api.metrics.description",
            security = @SecurityRequirement(name = "nacos"))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Result.class, example = "nacos.admin.naming.ops.api.metrics.example")))
    @Parameters(value = {@Parameter(name = "onlyStatus", example = "true")})
    public Result<MetricsInfo> metrics(
            @RequestParam(value = "onlyStatus", required = false, defaultValue = "true") Boolean onlyStatus) {
        return Result.success(MetricsInfoVo.toNewMetricsInfo(operatorV2Impl.metrics(onlyStatus)));
    }
    
    /**
     * Set log level.
     */
    @PutMapping("/log")
    @Secured(resource = UtilsAndCommons.OPERATOR_CONTROLLER_V3_ADMIN_PATH, action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
    @Operation(summary = "nacos.admin.naming.ops.api.log.summary", description = "nacos.admin.naming.ops.api.log.description",
            security = @SecurityRequirement(name = "nacos"))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Result.class, example = "nacos.admin.naming.ops.api.log.example")))
    @Parameters(value = {@Parameter(name = "logName", required = true, example = "naming-event"),
            @Parameter(name = "value", required = true, example = "DEBUG")})
    public Result<String> setLogLevel(@RequestParam String logName, @RequestParam String logLevel) {
        operatorV2Impl.setLogLevel(logName, logLevel);
        
        return Result.success("ok");
    }
}