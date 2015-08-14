/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {

    public enum CacheOption {

        NOT_CACHED,
        LRU_CACHED,
        PERMANENTLY_CACHED;

        public boolean isCached() {
            return (this != NOT_CACHED);
        }

        public boolean isLruCached() {
            return (this != LRU_CACHED);
        }

        public boolean isPermanentlyCached() {
            return (this != PERMANENTLY_CACHED);
        }
    }

    public static final int DEFAULT_CACHE_SIZE = 50;

    public static final String NAME_DEFAULT_CACHE = "_DefaultCache";

    private static HashMap<String, LruCache<String, Bitmap>> sBitmapCaches = new HashMap<>();

    private static final boolean sUseFileCaching = false;

    static {
        addCache(NAME_DEFAULT_CACHE);
    }

    public static Bitmap getBitmap(final int pResId) {
        return getBitmap(NAME_DEFAULT_CACHE, pResId);
    }

    public static Bitmap getBitmap(final String pCacheName, final int pResId) {
        final LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);
        final String key = Integer.toString(pResId);
        return cache.get(key);
    }

    public static LruCache<String, Bitmap> addCache(final String pCacheName) {
        return addCache(pCacheName, DEFAULT_CACHE_SIZE);
    }

    public static LruCache<String, Bitmap> addCache(final String pCacheName, final int pCacheSize) {
        LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);

        assert (cache == null);

        cache = new LruCache<String, Bitmap>(pCacheSize);
        sBitmapCaches.put(pCacheName, cache);
        return cache;
    }

    public static void clearCache() {
        clearCache(NAME_DEFAULT_CACHE);
    }

    public static void clearCache(final String pCacheName) {
        final LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);
        final int cacheSize = cache.maxSize();

        assert (cache != null);

        cache.trimToSize(0);
        cache.trimToSize(cacheSize);
    }

    public static int getCacheSize() {
        return getCacheSize(NAME_DEFAULT_CACHE);
    }

    public static int getCacheSize(final String pCacheName) {
        final LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);

        assert (cache != null);

        return cache.maxSize();
    }

    public static void resizeCache(final String pCacheName, final int pNewSize) {
        final LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);

        assert (cache != null);

        cache.trimToSize(pNewSize);
    }

    public static void removeCache(final String pCacheName) {
        final LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);

        assert (cache != null);

        sBitmapCaches.remove(pCacheName);
    }

    public static void clearAllCaches() {
        for (final String key : sBitmapCaches.keySet()) {
            clearCache(key);
        }
    }

    public void dispose() {
        clearAllCaches();
        sBitmapCaches.clear();
    }

    public static void addBitmap(final int pResId, final Bitmap pBitmap) {
        addBitmap(pResId, NAME_DEFAULT_CACHE, pBitmap);
    }

    public static void addBitmap(final int pResId, final String pCacheName, final Bitmap pBitmap) {
        final String key = Integer.toString(pResId);
        addBitmap(key, pCacheName, pBitmap, false);
    }

    public static void addBitmap(final String pKey, final String pCacheName, final Bitmap pBitmap) {
        addBitmap(pKey, pCacheName, pBitmap, sUseFileCaching);
    }

    private static void addBitmap(final String pKey, final String pCacheName, final Bitmap pBitmap,
            final boolean pUseFileCaching) {
        LruCache<String, Bitmap> cache = sBitmapCaches.get(pCacheName);

        if (cache == null) {
            cache = addCache(pCacheName);
        }

        cache.put(pKey, pBitmap);

        if (pUseFileCaching) {
            final File applicationDirectory = UIToolkit.getApplicationDirectory();
            final StringBuilder path = new StringBuilder(applicationDirectory.getAbsolutePath());
            path.append("/");
            path.append(StringToolkit.encodeFileName(pCacheName));

            final File cacheDirectory = new File(path.toString());

            if (!cacheDirectory.exists()) {
                cacheDirectory.mkdirs();
            }

            assert (cacheDirectory.exists() && cacheDirectory.canWrite());

            path.append("/");
            path.append(StringToolkit.encodeFileName(pKey));

            try {
                final FileOutputStream outputStream = new FileOutputStream(path.toString());
                pBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            } catch (final Exception e) {
                L.wtf(BitmapCache.class, "", e.toString());
            }
        }
    }

}
