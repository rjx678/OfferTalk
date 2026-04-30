const { request } = require('../../../utils/request.js');

Page({
  data: {
    commentList: [],
    hasComments: false
  },

  onLoad() {
    this.loadComments();
  },

  loadComments() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    request('/user/comments', 'GET', { userId: userInfo.id }).then(data => {
      const list = data || [];
      this.setData({
        commentList: list,
        hasComments: list.length > 0
      });
    }).catch(() => {
      this.setData({
        commentList: [],
        hasComments: false
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