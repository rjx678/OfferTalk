const { request } = require('../../../utils/request.js');

Page({
  data: {
    postList: [],
    hasPosts: false
  },

  onLoad() {
    this.loadPosts();
  },

  loadPosts() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    request('/user/posts', 'GET', { userId: userInfo.id }).then(data => {
      const list = data || [];
      this.setData({
        postList: list,
        hasPosts: list.length > 0
      });
    }).catch(() => {
      this.setData({
        postList: [],
        hasPosts: false
      });
    });
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    if (!id) return;

    const type = e.currentTarget.dataset.type;
    let url = '';
    if (type === 1) {
      url = `/pages/content/interview/interview?id=${id}`;
    } else if (type === 2) {
      url = `/pages/content/salary/salary?id=${id}`;
    } else if (type === 3) {
      url = `/pages/content/review/review?id=${id}`;
    }

    if (url) {
      wx.navigateTo({ url });
    }
  },

  goPublish() {
    wx.navigateTo({ url: '/pages/publish/publish' });
  },

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});