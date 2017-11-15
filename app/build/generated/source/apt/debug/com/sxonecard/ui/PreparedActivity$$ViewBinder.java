// Generated code from Butter Knife. Do not modify!
package com.sxonecard.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PreparedActivity$$ViewBinder<T extends com.sxonecard.ui.PreparedActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492975, "field 'startCheck'");
    target.startCheck = finder.castView(view, 2131492975, "field 'startCheck'");
    view = finder.findRequiredView(source, 2131492976, "field 'resultQuery'");
    target.resultQuery = finder.castView(view, 2131492976, "field 'resultQuery'");
  }

  @Override public void unbind(T target) {
    target.startCheck = null;
    target.resultQuery = null;
  }
}
