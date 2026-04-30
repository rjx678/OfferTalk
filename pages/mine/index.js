const { request, getFullImageUrl } = require('../../utils/request.js');

Page({
  data: {
    tabBarCurrent: 3,
    userInfo: null,
    hasLogin: false,
    displayAvatarUrl: '',
    contentItems: [
      { id: 'posts', name: '我的发布', icon: '📝', count: 0 },
      { id: 'collections', name: '我的收藏', icon: '⭐', count: 0 },
      { id: 'likes', name: '我的点赞', icon: '👍', count: 0 },
      { id: 'comments', name: '我的评论', icon: '💬', count: 0 }
    ],
    actionItems: [
      { id: 'history', name: '浏览历史', icon: '🔍' },
      { id: 'reports', name: '举报记录', icon: '🚨' }
    ],
    settings: [
      { id: 'privacy', name: '隐私设置', icon: '🔒' },
      { id: 'notification', name: '通知设置', icon: '🔔' },
      { id: 'agreement', name: '用户协议', icon: '📄' },
      { id: 'disclaimer', name: '免责声明', icon: '⚠️' },
      { id: 'feedback', name: '意见反馈', icon: '💡' },
      { id: 'about', name: '关于我们', icon: 'ℹ️' }
    ]
  },

  // 缓存对象
  cache: {
    stats: null,
    cacheTime: null,
    cacheDuration: 5 * 60 * 1000 // 5分钟缓存
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    this.cache.stats = null;
    this.cache.cacheTime = null;
    this.checkLoginStatus();
  },

  checkLoginStatus() {
    const userInfo = getApp().globalData.userInfo;
    if (userInfo && userInfo.id) {
      const avatarUrl = userInfo.avatarUrl || '';
      this.setData({
        userInfo,
        hasLogin: true,
        displayAvatarUrl: getFullImageUrl(avatarUrl)
      });
      this.loadUserStats(userInfo.id);
    } else {
      this.setData({
        userInfo: null,
        hasLogin: false,
        displayAvatarUrl: '',
        contentItems: [
          { id: 'posts', name: '我的发布', icon: '📝', count: 0 },
          { id: 'collections', name: '我的收藏', icon: '⭐', count: 0 },
          { id: 'likes', name: '我的点赞', icon: '👍', count: 0 },
          { id: 'comments', name: '我的评论', icon: '💬', count: 0 }
        ]
      });
    }
  },

  loadUserStats(userId) {
    // 检查缓存
    const now = Date.now();
    if (this.cache.stats && 
        this.cache.userId === userId && 
        this.cache.cacheTime && 
        now - this.cache.cacheTime < this.cache.cacheDuration) {
      // 使用缓存数据
      const contentItems = this.cache.stats;
      this.setData({ contentItems });
      return;
    }

    request('/user/settings/profile', 'GET', { userId }).then(data => {
      const contentItems = [
        { id: 'posts', name: '我的发布', icon: '📝', count: data.totalPostCount || 0 },
        { id: 'collections', name: '我的收藏', icon: '⭐', count: data.totalCollectCount || 0 },
        { id: 'likes', name: '我的点赞', icon: '👍', count: data.totalLikeCount || 0 },
        { id: 'comments', name: '我的评论', icon: '💬', count: data.totalCommentCount || 0 }
      ];
      this.setData({ contentItems });
      // 更新缓存
      this.cache.stats = contentItems;
      this.cache.userId = userId;
      this.cache.cacheTime = now;
    }).catch(() => {
      console.log('获取用户统计数据失败');
    });
  },

  doLogin() {
    wx.showLoading({ title: '登录中...' });
    wx.login({
      success: res => {
        if (res.code) {
          request('/user/login', 'POST', { code: res.code }).then(userData => {
          wx.hideLoading();
          const avatarUrl = userData.avatarUrl || '';
          userData.displayAvatarUrl = getFullImageUrl(avatarUrl);
          getApp().globalData.userInfo = userData;
          this.setData({
            userInfo: userData,
            hasLogin: true,
            displayAvatarUrl: userData.displayAvatarUrl
          });
          // 登录成功后立即加载用户统计数据
          this.loadUserStats(userData.id);
          wx.showToast({ title: '登录成功', icon: 'success' });
        }).catch(() => {
          wx.hideLoading();
          wx.showToast({ title: '登录失败', icon: 'none' });
        });
        } else {
          wx.hideLoading();
          wx.showToast({ title: '登录失败', icon: 'none' });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({ title: '登录失败', icon: 'none' });
      }
    });
  },

  getUserProfile() {
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: res => {
        const userInfo = res.userInfo;
        if (userInfo) {
          this.setData({
            'userInfo.nickname': userInfo.nickName,
            'userInfo.avatarUrl': userInfo.avatarUrl
          });
        }
      }
    });
  },

  navigateToMenu(e) {
    const id = e.currentTarget.dataset.id;
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const urlMap = {
      'posts': '/pages/mine/posts/posts',
      'collections': '/pages/mine/collections/collections',
      'likes': '/pages/mine/likes/likes',
      'comments': '/pages/mine/comments/comments',
      'history': '/pages/mine/history',
      'reports': '/pages/mine/reports'
    };

    if (urlMap[id]) {
      wx.navigateTo({ url: urlMap[id] });
    }
  },

  navigateToSetting(e) {
    const id = e.currentTarget.dataset.id;
    if (id === 'privacy') {
      this.navigateToPrivacy();
    } else if (id === 'notification') {
      this.navigateToNotification();
    } else if (id === 'feedback') {
      wx.navigateTo({ url: '/pages/tools/feedback/feedback' });
    } else if (id === 'about') {
      this.navigateToAbout();
    }
  },

  navigateToSalaryCalculator() {
    wx.navigateTo({ url: '/pages/tools/salary-calculator/salary-calculator' });
  },

  navigateToCompanyCompare() {
    wx.navigateTo({ url: '/pages/tools/company-compare/company-compare' });
  },

  navigateToCareerPath() {
    wx.navigateTo({ url: '/pages/tools/career-path/career-path' });
  },

  navigateToFeedback() {
    wx.navigateTo({ url: '/pages/tools/feedback/feedback' });
  },

  navigateToMyPosts() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/posts/posts' });
  },

  navigateToMyCollects() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/collections/collections' });
  },

  navigateToMyComments() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/comments/comments' });
  },

  navigateToMyLikes() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/likes/likes' });
  },

  navigateToProfile() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/profile/profile' });
  },

  navigateToPrivacy() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/privacy/privacy' });
  },

  navigateToNotification() {
    if (!this.data.hasLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/mine/notification/notification' });
  },

  navigateToAbout() {
    wx.navigateTo({ url: '/pages/mine/about/about' });
  },

  doLogout() {
    getApp().globalData.userInfo = null;
    this.setData({
      userInfo: null,
      hasLogin: false
    });
    wx.showToast({ title: '已退出登录', icon: 'none' });
  },

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});
