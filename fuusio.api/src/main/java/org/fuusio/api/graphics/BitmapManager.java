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

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import org.fuusio.api.util.L;
import org.fuusio.api.util.StringToolkit;
import org.fuusio.api.util.UIToolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public interface BitmapManager {

    Bitmap getBitmap(int pResId);

    Bitmap getBitmap(String pCacheName, int pResId);

    Bitmap getBitmap(String pKey);

    LruCache<String, Bitmap> addCache(String pCacheName);

    LruCache<String, Bitmap> addCache(String pCacheName, int pCacheSize);

    void clearCache();

    void clearCache(String pCacheName);

    int getCacheSize();

    int getCacheSize(String pCacheName);

    void resizeCache(String pCacheName, int pNewSize);

    void removeCache(String pCacheName);

    void clearAllCaches();

    void dispose();
    
    void addBitmap(int pResId, Bitmap pBitmap);

    void addBitmap(String pKey, Bitmap pBitmap);
    
    void addBitmap(int pResId, String pCacheName, Bitmap pBitmap);

    void addBitmap(String pKey, String pCacheName, Bitmap pBitmap);
}
