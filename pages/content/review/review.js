const { request } = require('../../../utils/request.js');

Page({
  data: {
    review: null,
    isLiked: false,
    isCollected: false,
    comments: [],
    commentContent: '',
    showQuickNav: false,
    scrollToView: ''
  },

  onLoad(options) {
    const id = options.id;
    if (id) {
      this.loadDetail(id);
      this.loadComments(id);
    }
  },

  onScroll(e) {
    const scrollTop = e.detail.scrollTop;
    const showQuickNav = scrollTop > 300;
    this.setData({ showQuickNav });
  },

  scrollToComment() {
    this.setData({ scrollToView: 'comment-section' });
    setTimeout(() => {
      this.setData({ scrollToView: '' });
    }, 500);
  },

  scrollToTop() {
    this.setData({ scrollToView: 'top' });
    setTimeout(() => {
      this.setData({ scrollToView: '' });
    }, 500);
  },

  loadDetail(id) {
    request('/review/detail/' + id).then(data => {
      this.setData({ review: data });
    }).catch(err => {
      wx.showToast({ title: '加载失败', icon: 'none' });
    });
  },

  loadComments(id) {
    request('/comment/list', 'GET', { contentId: id, contentType: 3 }).then(data => {
      this.setData({ comments: data?.records || [] });
    }).catch(() => {
      this.setData({ comments: [] });
    });
  },

  handleLike() {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    request('/like/toggle', 'POST', {
      userId,
      contentType: 3,
      contentId: this.data.review.id
    }).then(data => {
      this.setData({
        isLiked: data.hasLiked,
        ['review.likeCount']: this.data.review.likeCount + (data.hasLiked ? 1 : -1)
      });
    });
  },

  handleCollect() {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    request('/collect/toggle', 'POST', {
      userId,
      contentType: 3,
      contentId: this.data.review.id
    }).then(data => {
      this.setData({ isCollected: data.hasCollected });
      wx.showToast({ title: data.hasCollected ? '收藏成功' : '已取消收藏', icon: 'none' });
    });
  },

  onCommentInput(e) {
    this.setData({ commentContent: e.detail.value });
  },

  handleSubmitComment() {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    const content = this.data.commentContent.trim();
    if (!content) {
      wx.showToast({ title: '请输入评论内容', icon: 'none' });
      return;
    }
    request('/comment/create', 'POST', {
      userId,
      contentId: this.data.review.id,
      contentType: 3,
      content: content,
      isAnonymous: 1
    }).then(() => {
      wx.showToast({ title: '评论成功', icon: 'success' });
      this.setData({ commentContent: '' });
      this.loadComments(this.data.review.id);
    }).catch(() => {
      wx.showToast({ title: '评论失败', icon: 'none' });
    });
  },

  handleCommentLike(e) {
    const commentId = e.currentTarget.dataset.id;
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    request('/comment/like', 'POST', { commentId, userId }).then(data => {
      const comments = this.data.comments.map(c => {
        if (c.id === commentId) {
          c.likeCount = (c.likeCount || 0) + (data.hasLiked ? 1 : -1);
        }
        return c;
      });
      this.setData({ comments });
    });
  },

  toCompanyDetail(e) {
    const companyId = e.currentTarget.dataset.id;
    if (companyId) {
      wx.navigateTo({
        url: '/pages/company/detail/detail?id=' + companyId
      });
    }
  },

  onShareAppMessage() {
    return {
      title: this.data.review?.title,
      path: '/pages/content/review/review?id=' + this.data.review?.id
    };
  }
});