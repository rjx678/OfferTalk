const { request } = require('../../../utils/request.js');

Page({
  data: {
    collectList: [],
    hasCollections: false
  },

  onLoad() {
    this.loadCollections();
  },

  loadCollections() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    request('/user/collections', 'GET', { userId: userInfo.id }).then(data => {
      const list = data || [];
      this.setData({
        collectList: list,
        hasCollections: list.length > 0
      });
    }).catch(() => {
      this.setData({
        collectList: [],
        hasCollections: false
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

  handleCancel(e) {
    const id = e.currentTarget.dataset.id;
    const that = this;

    wx.showModal({
      title: '提示',
      content: '确定取消收藏吗？',
      success: function(res) {
        if (res.confirm) {
          request('/user/collections/cancel', 'POST', { collectId: id }).then(() => {
            that.loadCollections();
            wx.showToast({ title: '取消成功', icon: 'success' });
          }).catch(() => {
            wx.showToast({ title: '取消失败', icon: 'none' });
          });
        }
      }
    });
  },

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});