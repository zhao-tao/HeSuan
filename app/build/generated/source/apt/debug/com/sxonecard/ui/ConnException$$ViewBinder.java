// Generated code from Butter Knife. Do not modify!
package com.sxonecard.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ConnException$$ViewBinder<T extends com.sxonecard.ui.ConnException> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492955, "field 'conExcp'");
    target.conExcp = finder.castView(view, 2131492955, "field 'conExcp'");
  }

  @Override public void unbind(T target) {
    target.conExcp = null;
  }
}
