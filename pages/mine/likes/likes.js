const { request } = require('../../../utils/request.js');

Page({
  data: {
    likeList: [],
    hasLikes: false
  },

  onLoad() {
    this.loadLikes();
  },

  loadLikes() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    request('/user/likes', 'GET', { userId: userInfo.id }).then(data => {
      const list = data || [];
      this.setData({
        likeList: list,
        hasLikes: list.length > 0
      });
    }).catch(() => {
      this.setData({
        likeList: [],
        hasLikes: false
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

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});