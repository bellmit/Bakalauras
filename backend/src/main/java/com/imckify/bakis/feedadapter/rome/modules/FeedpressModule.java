package com.imckify.bakis.feedadapter.rome.modules;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.Module;

/**
 * This is a ROME module that provides support for the <a href="https://feed.press/xmlns">https://feed.press/xmlns</a> namespace.
 */
public interface FeedpressModule extends Module, CopyFrom {

    /**
     * The URI of the namespace. (<a href="https://feed.press/xmlns">https://feed.press/xmlns</a>)
     */
    String URI = "https://feed.press/xmlns";

    /**
     * Get the Feedpress newsletter ID.
     *
     * @return The Feedpress newsletter ID.
     */
    String getNewsletterId();

    /**
     * Set t Feedpress newsletter ID.
     *
     * @param newsletterId The Feedpress newsletter ID.
     */
    void setNewsletterId(String newsletterId);

    /**
     * Get the Feedpress locale identifier.
     *
     * @return The Feedpress locale identifier.
     */
    String getLocale();

    /**
     * Set the Feedpress locale identifier.
     *
     * @param locale The Feedpress locale identifier.
     */
    void setLocale(String locale);

    /**
     * Get the Feedpress podcast ID.
     *
     * @return The Feedpress podcast ID.
     */
    String getPodcastId();

    /**
     * Set the Feedpress podcast ID.
     *
     * @param podcastId The Feedpress podcast ID.
     */
    void setPodcastId(String podcastId);

    /**
     * Get the Feedpress CSS file.
     *
     * @return The Feedpress CSS file.
     */
    String getCssFile();

    /**
     * Set the Feedpress CSS file.
     *
     * @param cssFile The Feedpress CSS file.
     */
    void setCssFile(String cssFile);

    String getCompanyInfo();

    void setCompanyInfo(String companyInfo);

}