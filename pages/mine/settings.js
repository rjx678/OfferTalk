const { request } = require('../../utils/request.js');

Page({
  data: {
    profile: {
      userId: null,
      nickname: '',
      avatarUrl: '',
      phone: '',
      bio: '',
      totalPostCount: 0,
      totalLikeCount: 0
    },
    privacySettings: {
      anonymousDefault: true,
      showCollections: true,
      showPosts: true,
      personalizedRecommend: true
    },
    notificationSettings: {
      commentNotify: true,
      likeNotify: true,
      atNotify: true,
      systemNotify: true,
      weeklyReport: false
    },
    appVersion: '1.0.0'
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const userId = userInfo.id;

    this.loadProfile(userId);
    this.loadPrivacySettings(userId);
    this.loadNotificationSettings(userId);
  },

  loadProfile(userId) {
    request('/user/settings/profile', 'GET', { userId }).then(data => {
      this.setData({
        profile: data || {}
      });
    }).catch(() => {
      const userInfo = getApp().globalData.userInfo;
      this.setData({
        profile: {
          userId: userInfo.id,
          nickname: userInfo.nickname || '',
          avatarUrl: userInfo.avatarUrl || '',
          phone: userInfo.phone || '',
          bio: userInfo.bio || ''
        }
      });
    });
  },

  loadPrivacySettings(userId) {
    request('/user/settings/privacy', 'GET', { userId }).then(data => {
      this.setData({
        privacySettings: data || {}
      });
    }).catch(() => {});
  },

  loadNotificationSettings(userId) {
    request('/user/settings/notification', 'GET', { userId }).then(data => {
      this.setData({
        notificationSettings: data || {}
      });
    }).catch(() => {});
  },

  onPrivacyChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    const userId = this.data.profile.userId;

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
  },

  onNotificationChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    const userId = this.data.profile.userId;

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
  },

  navigateToProfileEdit() {
    const userId = this.data.profile.userId;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({
      url: '/pages/mine/profile-edit/profile-edit'
    });
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

    request('/user/settings/app/version', 'GET').then(data => {
      wx.hideLoading();
      const versionData = data;
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