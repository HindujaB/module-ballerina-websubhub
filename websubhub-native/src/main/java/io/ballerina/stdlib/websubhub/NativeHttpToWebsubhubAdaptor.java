/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.websubhub;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Future;
import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.async.Callback;
import io.ballerina.runtime.api.async.StrandMetadata;
import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.types.MethodType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;

import java.util.ArrayList;
import java.util.List;

import static io.ballerina.runtime.api.utils.StringUtils.fromString;

/**
 * {@code NativeHttpToWebsubhubAdaptor} is a wrapper object used for service method execution.
 */
public class NativeHttpToWebsubhubAdaptor {
    private static final String SERVICE_OBJECT = "WEBSUBHUB_SERVICE_OBJECT";
    private static final List<String> moduleDefinedErrors;
    static {
        moduleDefinedErrors = List.of(
                Constants.TOPIC_REGISTRATION_ERROR, Constants.TOPIC_DEREGISTRATION_ERROR,
                Constants.UPDATE_MESSAGE_ERROR, Constants.BAD_SUBSCRIPTION_ERROR,
                Constants.SUBSCRIPTION_INTERNAL_ERROR, Constants.SUBSCRIPTION_DENIED_ERROR,
                Constants.UNSUBSCRIPTION_INTERNAL_ERROR, Constants.UNSUBSCRIPTION_DENIED_ERROR
        );
    }

    public static void externInit(BObject adaptor, BObject serviceObj) {
        adaptor.addNativeData(SERVICE_OBJECT, serviceObj);
    }

    public static BArray getServiceMethodNames(BObject adaptor) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        ArrayList<BString> methodNamesList = new ArrayList<>();
        for (MethodType method : bHubService.getType().getMethods()) {
            methodNamesList.add(StringUtils.fromString(method.getName()));
        }
        return ValueCreator.createArrayValue(methodNamesList.toArray(BString[]::new));
    }

    public static Object callRegisterMethod(Environment env, BObject adaptor,
                                            BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callRegisterMethod", "onRegisterTopic");
    }

    public static Object callDeregisterMethod(Environment env, BObject adaptor,
                                              BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callDeregisterMethod", "onDeregisterTopic");
    }

    public static Object callOnUpdateMethod(Environment env, BObject adaptor,
                                            BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callOnUpdateMethod", "onUpdateMessage");
    }

    public static Object callOnSubscriptionMethod(Environment env, BObject adaptor,
                                                  BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callOnSubscriptionMethod", "onSubscription");
    }

    public static Object callOnSubscriptionValidationMethod(Environment env, BObject adaptor,
                                                            BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callOnSubscriptionValidationMethod", "onSubscriptionValidation");
    }

    public static Object callOnSubscriptionIntentVerifiedMethod(Environment env, BObject adaptor,
                                                                BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callOnSubscriptionIntentVerifiedMethod",
                "onSubscriptionIntentVerified");
    }

    public static Object callOnUnsubscriptionMethod(Environment env, BObject adaptor,
                                                    BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args,
                "callOnUnsubscriptionMethod", "onUnsubscription");
    }

    public static Object callOnUnsubscriptionValidationMethod(Environment env, BObject adaptor,
                                                              BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args, "callOnUnsubscriptionValidationMethod",
                "onUnsubscriptionValidation");
    }

    public static Object callOnUnsubscriptionIntentVerifiedMethod(Environment env, BObject adaptor,
                                                                  BMap<BString, Object> message, BObject bHttpHeaders) {
        BObject bHubService = (BObject) adaptor.getNativeData(SERVICE_OBJECT);
        Object[] args = new Object[]{message, true, bHttpHeaders, true};
        return invokeRemoteFunction(env, bHubService, args, "callOnUnsubscriptionIntentVerifiedMethod",
                "onUnsubscriptionIntentVerified");
    }

    private static Object invokeRemoteFunction(Environment env, BObject bHubService, Object[] args,
                                               String parentFunctionName, String remoteFunctionName) {
        Future balFuture = env.markAsync();
        Module module = ModuleUtils.getModule();
        StrandMetadata metadata = new StrandMetadata(module.getOrg(), module.getName(), module.getVersion(),
                parentFunctionName);
        env.getRuntime().invokeMethodAsync(bHubService, remoteFunctionName, null, metadata, new Callback() {
            @Override
            public void notifySuccess(Object result) {
                if (result instanceof BError) {
                    BError error = (BError) result;
                    if (!isModuleDefinedError(error)) {
                        error.printStackTrace();
                    }
                }

                balFuture.complete(result);
            }

            @Override
            public void notifyFailure(BError bError) {
                BString errorMessage = fromString("service method invocation failed: " + bError.getErrorMessage());
                BError invocationError = ErrorCreator.createError(module, "ServiceExecutionError",
                        errorMessage, bError, null);
                balFuture.complete(invocationError);
            }
        }, args);
        return null;
    }

    private static boolean isModuleDefinedError(BError error) {
        Type errorType = error.getType();
        String errorName = errorType.getName();
        Module packageDetails = errorType.getPackage();
        String orgName = packageDetails.getOrg();
        String packageName = packageDetails.getName();
        return moduleDefinedErrors.contains(errorName)
                && Constants.PACKAGE_ORG.equals(orgName) && Constants.PACKAGE_NAME.equals(packageName);
    }
}
