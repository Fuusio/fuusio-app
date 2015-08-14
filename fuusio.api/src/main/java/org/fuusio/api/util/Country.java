package org.fuusio.api.util;

public class Country {
    
    private final String mCode;
    private final String mIso;
    private final String mName;

    protected Country(final String pIso, final String pCode, final String pName) {
        mIso = pIso;
        mCode = pCode;
        mName = pName;
    }

    public final String getCode() {
        return mCode;
    }

    public final String getIso() {
        return mIso;
    }

    public final String getName() {
        return mName;
    }   
}
