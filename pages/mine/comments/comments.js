const { request } = require('../../../utils/request.js');

Page({
  data: {
    commentList: [],
    hasComments: false,
    loading: false,
    error: false
  },

  onLoad() {
    this.loadComments();
  },

  onShow() {
    // 每次显示时刷新数据
    if (this.data.hasComments || this.data.error) {
      this.loadComments();
    }
  },

  loadComments() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    this.setData({ loading: true, error: false });

    request('/user/comments', 'GET', { userId: userInfo.id }).then(data => {
      const result = data || {};
      const list = result.data || [];
      
      this.setData({
        commentList: list,
        hasComments: list.length > 0,
        loading: false
      });
    }).catch(() => {
      this.setData({
        commentList: [],
        hasComments: false,
        loading: false,
        error: true
      });
      wx.showToast({
        title: '加载失败',
        icon: 'none',
        duration: 2000
      });
    });
  },

  goDetail(e) {
    const { id, type } = e.currentTarget.dataset;
    if (!id || !type) return;

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

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});