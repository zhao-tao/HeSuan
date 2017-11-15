// Generated code from Butter Knife. Do not modify!
package com.sxonecard.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DeviceException$$ViewBinder<T extends com.sxonecard.ui.DeviceException> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492956, "field 'deviceErr'");
    target.deviceErr = finder.castView(view, 2131492956, "field 'deviceErr'");
  }

  @Override public void unbind(T target) {
    target.deviceErr = null;
  }
}
