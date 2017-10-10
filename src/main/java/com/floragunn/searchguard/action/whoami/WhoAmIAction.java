/*
 * Copyright 2015 floragunn UG (haftungsbeschränkt)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.floragunn.searchguard.action.whoami;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

public class WhoAmIAction extends Action<WhoAmIRequest, WhoAmIResponse, WhoAmIRequestBuilder> {

    public static final WhoAmIAction INSTANCE = new WhoAmIAction();
    public static final String NAME = "cluster:admin/searchguard/whoami";

    protected WhoAmIAction() {
        super(NAME);
    }

    @Override
    public WhoAmIRequestBuilder newRequestBuilder(final ElasticsearchClient client) {
        return new WhoAmIRequestBuilder(client, this);
    }

    @Override
    public WhoAmIResponse newResponse() {
        return new WhoAmIResponse(null, false, false, false);
    }

}
