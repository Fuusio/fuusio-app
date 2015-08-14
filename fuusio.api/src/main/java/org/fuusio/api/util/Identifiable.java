// ============================================================================
// Floxp.com : Java Interface Source File
// ============================================================================
//
// Interface: Executable
// Package: FloXP.com Android Application (com.floxp) -
// Runtime API (com.floxp.runtime)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2009-2011. All Rights Reserved.
//
// This software is the proprietary information of Marko Salmela.
// Use is subject to license terms. This software is protected by
// copyright and distributed under licenses restricting its use,
// copying, distribution, and decompilation. No part of this software
// or associated documentation may be reproduced in any form by any
// means without prior written authorization of Marko Salmela.
//
// Disclaimer:
// -----------
//
// This software is provided by the author 'as is' and any express or implied
// warranties, including, but not limited to, the implied warranties of
// merchantability and fitness for a particular purpose are disclaimed.
// In no event shall the author be liable for any direct, indirect,
// incidental, special, exemplary, or consequential damages (including, but
// not limited to, procurement of substitute goods or services, loss of use,
// data, or profits; or business interruption) however caused and on any
// theory of liability, whether in contract, strict liability, or tort
// (including negligence or otherwise) arising in any way out of the use of
// this software, even if advised of the possibility of such damage.
// ============================================================================

package org.fuusio.api.util;

public interface Identifiable {

    public static final String NAME_UNTITLED = "untitled";

    int getId();

    void setId(int pId);

    String getName();

    void setName(String pName);

}
