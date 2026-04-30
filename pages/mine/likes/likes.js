const { request } = require('../../../utils/request.js');

Page({
  data: {
    likeList: [],
    hasLikes: false,
    loading: false,
    error: false
  },

  onLoad() {
    this.loadLikes();
  },

  onShow() {
    // 每次显示时刷新数据
    if (this.data.hasLikes || this.data.error) {
      this.loadLikes();
    }
  },

  loadLikes() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    this.setData({ loading: true, error: false });

    request('/user/likes', 'GET', { userId: userInfo.id }).then(data => {
      const result = data || {};
      const list = result.data || [];
      
      // 格式化显示内容
      list.forEach(item => {
        if (!item.typeName) {
          if (item.contentType === 1) {
            item.typeName = '面经';
          } else if (item.contentType === 2) {
            item.typeName = '薪资';
          } else if (item.contentType === 3) {
            item.typeName = '评价';
          }
        }
      });
      
      this.setData({
        likeList: list,
        hasLikes: list.length > 0,
        loading: false
      });
    }).catch(() => {
      this.setData({
        likeList: [],
        hasLikes: false,
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

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料',
      path: '/pages/index/index'
    };
  }
});