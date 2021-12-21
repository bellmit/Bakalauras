package com.imckify.bakis.feedadapter.rome.io;

/*
 * Copyright 2019 Maximilian Irro
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
 */

import com.imckify.bakis.feedadapter.rome.modules.FeedpressModule;
import com.imckify.bakis.feedadapter.rome.modules.FeedpressModuleImpl;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleParser;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Locale;

/**
 * The ModuleParser implementation for the Feedpress module.
 */
public class FeedpressParser implements ModuleParser {

    private static final Namespace NS = Namespace.getNamespace(FeedpressModule.URI);

    @Override
    public String getNamespaceUri() {
        return FeedpressModule.URI;
    }

    @Override
    public Module parse(Element element, Locale l) {
        FeedpressModule feedpress = null;
        if (element.getName().equals("channel") || element.getName().equals("feed")) {
            feedpress = new FeedpressModuleImpl();

            final Element companyInfo = element.getChild("company-info", NS);
            if (companyInfo != null && companyInfo.getValue() != null) {
                feedpress.setCompanyInfo(companyInfo.getValue().trim());
            }

            final Element newsletterId = element.getChild("newsletterId", NS);
            if (newsletterId != null && newsletterId.getValue() != null) {
                feedpress.setNewsletterId(newsletterId.getValue().trim());
            }

            final Element locale = element.getChild("locale", NS);
            if (locale != null && locale.getValue() != null) {
                feedpress.setLocale(locale.getValue().trim());
            }

            final Element podcastId = element.getChild("podcastId", NS);
            if (podcastId != null && podcastId.getValue() != null) {
                feedpress.setPodcastId(podcastId.getValue().trim());
            }

            final Element cssFile = element.getChild("cssFile", NS);
            if (cssFile != null && cssFile.getValue() != null) {
                feedpress.setCssFile(cssFile.getValue().trim());
            }
        }
        return feedpress;
    }

}