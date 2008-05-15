/*
 * Copyright 2007 Tailrank, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.spinn3r.api;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * 
 */
public class PermalinkStatusClient extends BaseClient implements Client {

    public static int MAX_LIMIT            = 10;
    public static int OPTIMAL_LIMIT        = 10;
    public static int CONSERVATIVE_LIMIT   = 10;

    /**
     * Base router request URL.
     */
    public static String ROUTER = "http://api.spinn3r.com/rss/permalink.status?";

    public void fetch() throws IOException,
                               ParseException,
                               InterruptedException {
        
        super.fetch( config );
    }

    /**
     * Generate the first request URL based just on configuration directives.
     */
    protected String generateFirstRequestURL() {

        PermalinkStatusConfig config = (PermalinkStatusConfig)super.getConfig();
        
        StringBuffer params = new StringBuffer( 1024 ) ;

        int limit = config.getLimit();
        
        if ( limit > getMaxLimit() )
            limit = getMaxLimit();
        
        addParam( params, "limit",   limit );
        addParam( params, "vendor",  config.getVendor() );
        addParam( params, "version", config.getVersion() );
        addParam( params, "resource",  config.getResource() );

        String result = getRouter() + params.toString();

        System.out.printf( "DUMP: %s\n" , result );
        
        return result;
        
    }

    protected BaseItem parseItem( Element current ) throws Exception {

        PermalinkItem item = new PermalinkItem();

        return super.parseItem( current, item );
        
    }

    protected int getMaxLimit() {
        return MAX_LIMIT;
    }

    protected int getOptimalLimit() {
        return OPTIMAL_LIMIT;
    }

    protected int getConservativeLimit() {
        return CONSERVATIVE_LIMIT;
    }

    public String getRouter() {
        return "http://" + getHost() + "/rss/permalink.status?";
    }

    public static void main( String[] args ) throws Exception {

        PermalinkStatusConfig config = new PermalinkStatusConfig();
        PermalinkStatusClient client = new PermalinkStatusClient();

        config.setVendor( "debug" );
        config.setVersion( "2.2.1" );
        config.setResource( "http://feedblog.org/2008/05/10/using-zookeeper-to-configure-external-monitoring-systems/" );
        
        client.setConfig( config );
        client.setHost( "dev.api.spinn3r.com" );

        client.fetch();
        List<BaseItem> results = client.getResults();

        System.out.printf( "%s\n", client.getLastRequestURL() );
        System.out.printf( "DUMP: Found %d items\n" , results.size() );

        for( BaseItem item : results ) {

            System.out.println( "----" );
            System.out.println( "link:                   " + item.getLink() );
            System.out.println( "guid:                   " + item.getGuid() );
            System.out.println( "feed URL:               " + item.getFeedURL() );

            System.out.println( "title:                  " + item.getTitle() );
            System.out.println( "source:                 " + item.getSource() );
            System.out.println( "pubDate:                " + item.getPubDate() );
            System.out.println( "published:              " + item.getPublished() );
            
            System.out.println( "weblog title:           " + item.getWeblogTitle() );
            System.out.println( "weblog tier:            " + item.getWeblogTier() );
            System.out.println( "weblog publisher type:  " + item.getWeblogPublisherType() );
            System.out.println( "weblog indegree:        " + item.getWeblogIndegree() );
            
            System.out.println( "author name:            " + item.getAuthorName() );
            System.out.println( "author email:           " + item.getAuthorEmail() );
            System.out.println( "author link:            " + item.getAuthorLink() );

            System.out.println( "lang:                   " + item.getLang() );
            System.out.println( "tags:                   " + item.getTags() );
            
            System.out.println( "description: " );
            System.out.println( "-" );
            System.out.println( item.getDescription() );
            System.out.println( "-" );

        }
        
    }

}
