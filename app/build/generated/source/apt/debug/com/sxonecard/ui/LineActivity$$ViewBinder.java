// Generated code from Butter Knife. Do not modify!
package com.sxonecard.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LineActivity$$ViewBinder<T extends LineFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492959, "field 'chart'");
    target.chart = finder.castView(view, 2131492959, "field 'chart'");
    view = finder.findRequiredView(source, 2131492967, "field 'temperature'");
    target.temperature = finder.castView(view, 2131492967, "field 'temperature'");
    view = finder.findRequiredView(source, 2131492963, "field 'shiguanGrid'");
    target.shiguanGrid = finder.castView(view, 2131492963, "field 'shiguanGrid'");
    view = finder.findRequiredView(source, 2131492968, "field 'deviceStatus'");
    target.deviceStatus = finder.castView(view, 2131492968, "field 'deviceStatus'");
    view = finder.findRequiredView(source, 2131492974, "field 'relationFitting'");
    target.relationFitting = finder.castView(view, 2131492974, "field 'relationFitting'");
    view = finder.findRequiredView(source, 2131492960, "field 'upmove'");
    target.upmove = finder.castView(view, 2131492960, "field 'upmove'");
    view = finder.findRequiredView(source, 2131492961, "field 'downMove'");
    target.downMove = finder.castView(view, 2131492961, "field 'downMove'");
    view = finder.findRequiredView(source, 2131492965, "field 'concentrationValues'");
    target.concentrationValues = finder.castView(view, 2131492965, "field 'concentrationValues'");
    view = finder.findRequiredView(source, 2131492966, "field 'resultValues'");
    target.resultValues = finder.castView(view, 2131492966, "field 'resultValues'");
    view = finder.findRequiredView(source, 2131492964, "field 'sampleValues'");
    target.sampleValues = finder.castView(view, 2131492964, "field 'sampleValues'");
    view = finder.findRequiredView(source, 2131492962, "field 'numberPicker'");
    target.numberPicker = finder.castView(view, 2131492962, "field 'numberPicker'");
    view = finder.findRequiredView(source, 2131492969, "field 'duibi'");
    target.duibi = finder.castView(view, 2131492969, "field 'duibi'");
    view = finder.findRequiredView(source, 2131492970, "field 'huanyuan'");
    target.huanyuan = finder.castView(view, 2131492970, "field 'huanyuan'");
    view = finder.findRequiredView(source, 2131492971, "field 'fazhiGuding'");
    target.fazhiGuding = finder.castView(view, 2131492971, "field 'fazhiGuding'");
    view = finder.findRequiredView(source, 2131492972, "field 'back'");
    target.back = finder.castView(view, 2131492972, "field 'back'");
    view = finder.findRequiredView(source, 2131492973, "field 'startSendRequest'");
    target.startSendRequest = finder.castView(view, 2131492973, "field 'startSendRequest'");
  }

  @Override public void unbind(T target) {
    target.chart = null;
    target.temperature = null;
    target.shiguanGrid = null;
    target.deviceStatus = null;
    target.relationFitting = null;
    target.upmove = null;
    target.downMove = null;
    target.concentrationValues = null;
    target.resultValues = null;
    target.sampleValues = null;
    target.numberPicker = null;
    target.duibi = null;
    target.huanyuan = null;
    target.fazhiGuding = null;
    target.back = null;
    target.startSendRequest = null;
  }
}
