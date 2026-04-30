const { request } = require('../../../utils/request.js');

Page({
  data: {
    collectList: [],
    hasCollections: false,
    loading: false,
    error: false,
    cancelLoading: false
  },

  onLoad() {
    this.loadCollections();
  },

  onShow() {
    // 每次显示时刷新数据
    if (this.data.hasCollections || this.data.error) {
      this.loadCollections();
    }
  },

  loadCollections() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    this.setData({ loading: true, error: false });

    request('/user/collections', 'GET', { userId: userInfo.id }).then(data => {
      const list = data || [];
      
      // 格式化显示内容
      list.forEach(item => {
        if (!item.title) {
          if (item.type === 1) {
            item.title = '面经分享';
          } else if (item.type === 2) {
            item.title = '薪资爆料';
          } else if (item.type === 3) {
            item.title = '公司评价';
          }
        }
        if (!item.typeName) {
          if (item.type === 1) {
            item.typeName = '面经';
          } else if (item.type === 2) {
            item.typeName = '薪资';
          } else if (item.type === 3) {
            item.typeName = '评价';
          }
        }
      });
      
      this.setData({
        collectList: list,
        hasCollections: list.length > 0,
        loading: false
      });
    }).catch(() => {
      this.setData({
        collectList: [],
        hasCollections: false,
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