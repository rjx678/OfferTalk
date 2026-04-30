const { request } = require('../../utils/request.js');

Page({
  data: {
    post: null,
    isLiked: false,
    isCollected: false
  },

  onLoad(options) {
    const id = options.id;
    this.loadDetail(id);
  },

  loadDetail(id) {
    request(`/post/interview/${id}`).then(res => {
      this.setData({ post: res.data });
    });
  },

  handleLike() {
    this.setData({ 
      isLiked: !this.data.isLiked,
      ['post.likeCount']: this.data.isLiked ? this.data.post.likeCount - 1 : this.data.post.likeCount + 1
    });
    wx.showToast({ title: this.data.isLiked ? '点赞成功' : '已取消', icon: 'none' });
  },

  handleCollect() {
    this.setData({ isCollected: !this.data.isCollected });
    wx.showToast({ title: this.data.isCollected ? '收藏成功' : '已取消', icon: 'none' });
  },

  onShareAppMessage() {
    return {
      title: this.data.post.title,
      path: `/pages/content/index?id=${this.data.post.id}`
    };
  }
});
