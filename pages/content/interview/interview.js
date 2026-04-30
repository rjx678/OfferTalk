const { request } = require('../../../utils/request.js');

Page({
  data: {
    post: null,
    isLiked: false,
    isCollected: false,
    comments: [],
    page: 1,
    loading: false,
    commentContent: '',
    showQuickNav: false,
    scrollTop: 0,
    commentSectionTop: 0
  },

  onLoad(options) {
    const id = options.id;
    if (id) {
      this.loadDetail(id);
      this.loadComments(id);
    }
  },

  onReady() {
    this.calculateCommentPosition();
  },

  calculateCommentPosition() {
    const query = wx.createSelectorQuery();
    query.select('#comment-section').boundingClientRect(rect => {
      if (rect) {
        this.setData({ commentSectionTop: rect.top });
      }
    }).exec();
  },

  onScroll(e) {
    const scrollTop = e.detail.scrollTop;
    const showQuickNav = scrollTop > 500;
    this.setData({ showQuickNav });
  },

  scrollToComment() {
    this.setData({ scrollTop: this.data.commentSectionTop - 100 });
  },

  onScrollToTop() {
    this.setData({ scrollTop: 0 });
  },

  loadDetail(id) {
    request('/post/interview/' + id).then(data => {
      this.setData({ post: data });
      this.checkLikeStatus(id);
      this.checkCollectStatus(id);
    }).catch(err => {
      wx.showToast({ title: '加载失败', icon: 'none' });
    });
  },

  loadComments(id) {
    request('/comment/list', 'GET', {
      contentId: id,
      contentType: 1,
      page: this.data.page,
      size: 20
    }).then(data => {
      this.setData({ comments: data?.records || [] });
    });
  },

  checkLikeStatus(id) {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) return;
    request('/like/check', 'GET', {
      userId,
      contentType: 1,
      contentId: id
    }).then(data => {
      this.setData({ isLiked: data.hasLiked });
    });
  },

  checkCollectStatus(id) {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) return;
    request('/collect/check', 'GET', {
      userId,
      contentType: 1,
      contentId: id
    }).then(data => {
      this.setData({ isCollected: data.hasCollected });
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
      contentType: 1,
      contentId: this.data.post.id
    }).then(data => {
      this.setData({
        isLiked: data.hasLiked,
        ['post.likeCount']: this.data.post.likeCount + (data.hasLiked ? 1 : -1)
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
      contentType: 1,
      contentId: this.data.post.id
    }).then(data => {
      this.setData({ isCollected: data.hasCollected });
      wx.showToast({ title: data.hasCollected ? '收藏成功' : '已取消收藏', icon: 'none' });
    });
  },

  handleComment() {
    wx.navigateTo({
      url: '/pages/comment/index?contentId=' + this.data.post.id + '&contentType=1'
    });
  },

  handleReport() {
    wx.showModal({
      title: '举报',
      content: '确定要举报这条内容吗？',
      success: res => {
        if (res.confirm) {
          wx.showToast({ title: '举报成功', icon: 'success' });
        }
      }
    });
  },

  parseTimeline(timelineData) {
    if (!timelineData) return [];
    if (typeof timelineData === 'string') {
      try {
        return JSON.parse(timelineData);
      } catch (e) {
        return [];
      }
    }
    return timelineData;
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
      title: this.data.post?.title,
      path: '/pages/content/interview/interview?id=' + this.data.post?.id
    };
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
    
    if (!this.data.commentContent.trim()) {
      wx.showToast({ title: '请输入评论内容', icon: 'none' });
      return;
    }

    request('/comment/create', 'POST', {
      userId,
      contentId: this.data.post.id,
      contentType: 1,
      content: this.data.commentContent
    }).then(data => {
      if (data.success !== false) {
        wx.showToast({ title: '评论成功', icon: 'success' });
        this.setData({ commentContent: '' });
        this.loadComments(this.data.post.id);
      } else {
        wx.showToast({ title: '评论失败', icon: 'none' });
      }
    }).catch(err => {
      wx.showToast({ title: '评论失败', icon: 'none' });
    });
  }
});