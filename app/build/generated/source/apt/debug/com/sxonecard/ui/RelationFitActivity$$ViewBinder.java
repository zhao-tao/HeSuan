// Generated code from Butter Knife. Do not modify!
package com.sxonecard.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RelationFitActivity$$ViewBinder<T extends com.sxonecard.ui.RelationFitActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492977, "field 'fitChart'");
    target.fitChart = finder.castView(view, 2131492977, "field 'fitChart'");
    view = finder.findRequiredView(source, 2131492978, "field 'index'");
    target.index = finder.castView(view, 2131492978, "field 'index'");
    view = finder.findRequiredView(source, 2131492979, "field 'logarithmFun'");
    target.logarithmFun = finder.castView(view, 2131492979, "field 'logarithmFun'");
    view = finder.findRequiredView(source, 2131492980, "field 'lineFun'");
    target.lineFun = finder.castView(view, 2131492980, "field 'lineFun'");
    view = finder.findRequiredView(source, 2131492981, "field 'saveBtn'");
    target.saveBtn = finder.castView(view, 2131492981, "field 'saveBtn'");
    view = finder.findRequiredView(source, 2131492982, "field 'exportBtn'");
    target.exportBtn = finder.castView(view, 2131492982, "field 'exportBtn'");
    view = finder.findRequiredView(source, 2131492983, "field 'backBtn'");
    target.backBtn = finder.castView(view, 2131492983, "field 'backBtn'");
  }

  @Override public void unbind(T target) {
    target.fitChart = null;
    target.index = null;
    target.logarithmFun = null;
    target.lineFun = null;
    target.saveBtn = null;
    target.exportBtn = null;
    target.backBtn = null;
  }
}
