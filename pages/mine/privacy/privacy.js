const { request } = require('../../../utils/request.js');

Page({
  data: {
    privacySettings: {
      anonymousDefault: true,
      showCollections: true,
      showPosts: true,
      personalizedRecommend: true
    }
  },

  onLoad() {
    this.loadPrivacySettings();
  },

  loadPrivacySettings() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    request('/user/settings/privacy', 'GET', { userId: userInfo.id }).then(data => {
      this.setData({
        privacySettings: data || {}
      });
    }).catch(() => {});
  },

  onSettingChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    const userId = getApp().globalData.userInfo?.id;

    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    this.setData({
      [`privacySettings.${field}`]: value
    });

    request('/user/settings/privacy/update', 'POST', {
      userId,
      [field]: value
    }).then(() => {
      wx.showToast({ title: '设置已保存', icon: 'success', duration: 1000 });
    }).catch(() => {
      wx.showToast({ title: '保存失败', icon: 'none' });
    });
  }
});