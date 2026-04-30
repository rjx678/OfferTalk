const { request } = require('../../../utils/request.js');

Page({
  data: {
    notificationSettings: {
      commentNotify: true,
      likeNotify: true,
      atNotify: true,
      systemNotify: true,
      weeklyReport: false
    }
  },

  onLoad() {
    this.loadNotificationSettings();
  },

  loadNotificationSettings() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    request('/user/settings/notification', 'GET', { userId: userInfo.id }).then(data => {
      this.setData({
        notificationSettings: data || {}
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
      [`notificationSettings.${field}`]: value
    });

    request('/user/settings/notification/update', 'POST', {
      userId,
      [field]: value
    }).then(() => {
      wx.showToast({ title: '设置已保存', icon: 'success', duration: 1000 });
    }).catch(() => {
      wx.showToast({ title: '保存失败', icon: 'none' });
    });
  }
});