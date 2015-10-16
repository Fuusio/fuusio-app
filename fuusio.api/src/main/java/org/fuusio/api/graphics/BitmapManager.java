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
package org.fuusio.api.graphics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import org.fuusio.api.util.L;
import org.fuusio.api.util.StringToolkit;
import org.fuusio.api.util.UIToolkit;

public class BitmapManager {

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

    private  HashMap<String, LruCache<String, Bitmap>> mBitmapCaches;


    public BitmapManager() {
        addCache(NAME_DEFAULT_CACHE);
        mBitmapCaches = new HashMap<>();
    }

    public Bitmap getBitmap(final int pResId) {
        return getBitmap(NAME_DEFAULT_CACHE, pResId);
    }

    public Bitmap getBitmap(final String pCacheName, final int pResId) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);
        final String key = Integer.toString(pResId);
        return cache.get(key);
    }

    public Bitmap getBitmap(final String pKey) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(NAME_DEFAULT_CACHE);
        return cache.get(pKey);
    }

    public LruCache<String, Bitmap> addCache(final String pCacheName) {
        return addCache(pCacheName, DEFAULT_CACHE_SIZE);
    }

    public LruCache<String, Bitmap> addCache(final String pCacheName, final int pCacheSize) {
        LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);

        assert (cache == null);

        cache = new LruCache<String, Bitmap>(pCacheSize);
        mBitmapCaches.put(pCacheName, cache);
        return cache;
    }

    public void clearCache() {
        clearCache(NAME_DEFAULT_CACHE);
    }

    public void clearCache(final String pCacheName) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);
        final int cacheSize = cache.maxSize();

        assert (cache != null);

        cache.trimToSize(0);
        cache.trimToSize(cacheSize);
    }

    public int getCacheSize() {
        return getCacheSize(NAME_DEFAULT_CACHE);
    }

    public int getCacheSize(final String pCacheName) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);

        assert (cache != null);

        return cache.maxSize();
    }

    public void resizeCache(final String pCacheName, final int pNewSize) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);

        assert (cache != null);

        cache.trimToSize(pNewSize);
    }

    public void removeCache(final String pCacheName) {
        final LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);

        assert (cache != null);

        mBitmapCaches.remove(pCacheName);
    }

    public void clearAllCaches() {
        for (final String key : mBitmapCaches.keySet()) {
            clearCache(key);
        }
    }

    public void dispose() {
        clearAllCaches();
        mBitmapCaches.clear();
    }

    public void addBitmap(final int pResId, final Bitmap pBitmap) {
        addBitmap(pResId, NAME_DEFAULT_CACHE, pBitmap);
    }

    public void addBitmap(final String pKey, final Bitmap pBitmap) {
        addBitmap(pKey, NAME_DEFAULT_CACHE, pBitmap);
    }

    public void addBitmap(final int pResId, final String pCacheName, final Bitmap pBitmap) {
        final String key = Integer.toString(pResId);
        addBitmap(key, pCacheName, pBitmap, false);
    }

    public void addBitmap(final String pKey, final String pCacheName, final Bitmap pBitmap) {
        addBitmap(pKey, pCacheName, pBitmap, true);
    }

    private void addBitmap(final String pKey, final String pCacheName, final Bitmap pBitmap,
            final boolean pUseFileCaching) {
        LruCache<String, Bitmap> cache = mBitmapCaches.get(pCacheName);

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
                L.wtf(BitmapManager.class, "", e.toString());
            }
        }
    }
}
