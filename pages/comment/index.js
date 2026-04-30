const { request } = require('../../utils/request.js');

Page({
  data: {
    contentId: null,
    contentType: null,
    comments: [],
    newComment: '',
    placeholder: '写下你的评论...'
  },

  onLoad(options) {
    const { contentId, contentType } = options;
    if (contentId && contentType) {
      this.setData({
        contentId: parseInt(contentId),
        contentType: parseInt(contentType)
      });
      this.loadComments();
    }
  },

  loadComments() {
    const { contentId, contentType } = this.data;
    request('/comment/list', 'GET', {
      contentId,
      contentType,
      page: 1,
      size: 50
    }).then(data => {
      this.setData({ comments: data?.records || [] });
    }).catch(() => {
      this.setData({ comments: [] });
    });
  },

  onInput(e) {
    this.setData({ newComment: e.detail.value });
  },

  submitComment() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const { newComment, contentId, contentType } = this.data;
    if (!newComment || newComment.trim().length === 0) {
      wx.showToast({ title: '评论内容不能为空', icon: 'none' });
      return;
    }

    if (newComment.length > 500) {
      wx.showToast({ title: '评论内容过长', icon: 'none' });
      return;
    }

    request('/comment/create', 'POST', {
      userId: userInfo.id,
      contentId,
      contentType,
      content: newComment.trim(),
      isAnonymous: 1
    }).then(() => {
      wx.showToast({ title: '评论成功', icon: 'success' });
      this.setData({ newComment: '' });
      this.loadComments();
    }).catch(() => {
      wx.showToast({ title: '评论失败', icon: 'none' });
    });
  }
});