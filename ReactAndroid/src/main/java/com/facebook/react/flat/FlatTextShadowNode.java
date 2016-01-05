/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.react.flat;

import android.text.SpannableStringBuilder;

import com.facebook.react.uimanager.ReactShadowNode;

/**
 * Base class for RCTVirtualText and RCTRawText.
 */
/* package */ abstract class FlatTextShadowNode extends FlatShadowNode {

  // these 2 are only used between collectText() and applySpans() calls.
  private int mTextBegin;
  private int mTextEnd;

  /**
   * Propagates changes up to RCTText without dirtying current node.
   */
  protected void notifyChanged(boolean shouldRemeasure) {
    ReactShadowNode parent = getParent();
    if (parent instanceof FlatTextShadowNode) {
      ((FlatTextShadowNode) parent).notifyChanged(shouldRemeasure);
    }
  }

  @Override
  public boolean isVirtual() {
    return true;
  }

  /**
   * Recursively visits FlatTextShadowNode and its children,
   * appending text to SpannableStringBuilder.
   */
  /* package */ final void collectText(SpannableStringBuilder builder) {
    mTextBegin = builder.length();
    performCollectText(builder);
    mTextEnd = builder.length();
  }

  /**
   * Recursively visits FlatTextShadowNode and its children,
   * applying spans to SpannableStringBuilder.
   */
  /* package */ final void applySpans(SpannableStringBuilder builder) {
    if (mTextBegin != mTextEnd) {
      performApplySpans(builder, mTextBegin, mTextEnd);
    }
  }

  protected abstract void performCollectText(SpannableStringBuilder builder);
  protected abstract void performApplySpans(SpannableStringBuilder builder, int begin, int end);
  protected abstract void performCollectAttachDetachListeners(StateBuilder stateBuilder);
}