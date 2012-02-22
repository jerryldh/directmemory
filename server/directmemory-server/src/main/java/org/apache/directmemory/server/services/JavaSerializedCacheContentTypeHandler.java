package org.apache.directmemory.server.services;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directmemory.server.commons.DirectMemoryHttpConstants;
import org.apache.directmemory.server.commons.DirectMemoryCacheException;
import org.apache.directmemory.server.commons.DirectMemoryCacheRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Olivier Lamy
 */
public class JavaSerializedCacheContentTypeHandler
    implements CacheContentTypeHandler
{
    private Logger log = LoggerFactory.getLogger( getClass() );

    @Override
    public byte[] handleGet( DirectMemoryCacheRequest request, byte[] cacheResponseContent, HttpServletResponse resp )
        throws DirectMemoryCacheException, IOException
    {
        resp.setContentType( DirectMemoryHttpConstants.JAVA_SERIALIZED_OBJECT_CONTENT_TYPE_HEADER );
        return cacheResponseContent;
    }

    @Override
    public DirectMemoryCacheRequest handlePut( HttpServletRequest request, HttpServletResponse response )
        throws DirectMemoryCacheException, IOException
    {
        String expiresInHeader = request.getHeader( DirectMemoryHttpConstants.EXPIRES_IN_HTTP_HEADER );
        int expiresIn = StringUtils.isEmpty( expiresInHeader ) ? 0 : Integer.valueOf( expiresInHeader );
        log.debug( "expiresIn: {} for header value: {}", expiresIn, expiresInHeader );
        return new DirectMemoryCacheRequest().setExpiresIn( expiresIn ).setCacheContent(
            IOUtils.toByteArray( request.getInputStream() ) );
    }
}
