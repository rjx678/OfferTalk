const { request } = require('../../../utils/request.js');

Page({
  data: {
    appVersion: '1.0.0'
  },

  onLoad() {
    this.loadAppVersion();
  },

  loadAppVersion() {
    request('/user/settings/app/version', 'GET').then(res => {
      const versionData = res.data;
      this.setData({
        appVersion: versionData.versionName || '1.0.0'
      });
    }).catch(() => {});
  },

  navigateToUserAgreement() {
    wx.showModal({
      title: '用户协议',
      content: 'OfferTalk用户协议内容\n\n1. 服务条款\n2. 用户行为规范\n3. 隐私保护\n4. 知识产权\n5. 免责声明',
      showCancel: false
    });
  },

  navigateToPrivacyPolicy() {
    wx.showModal({
      title: '隐私政策',
      content: 'OfferTalk隐私政策\n\n1. 信息收集\n2. 信息使用\n3. 信息共享\n4. 信息安全\n5. 用户权利',
      showCancel: false
    });
  },

  navigateToFeedback() {
    wx.navigateTo({
      url: '/pages/tools/feedback/feedback'
    });
  },

  checkForUpdate() {
    wx.showLoading({ title: '检查中...' });

    request('/user/settings/app/version', 'GET').then(res => {
      wx.hideLoading();
      const versionData = res.data;
      this.setData({
        appVersion: versionData.versionName || '1.0.0'
      });

      if (versionData.hasUpdate) {
        wx.showModal({
          title: '发现新版本',
          content: versionData.updateContent || '有新版本可用，是否立即更新？',
          confirmText: '更新',
          cancelText: '稍后',
          success: (res) => {
            if (res.confirm && versionData.downloadUrl) {
              wx.setStorageSync('appVersion', versionData);
            }
          }
        });
      } else {
        wx.showToast({ title: '已是最新版本', icon: 'success' });
      }
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '检查失败', icon: 'none' });
    });
  }
});
