const { request } = require('../../../utils/request.js');

Page({
  data: {
    salary: null,
    isLiked: false,
    isCollected: false,
    comments: [],
    commentContent: ''
  },

  onLoad(options) {
    const id = options.id;
    if (id) {
      this.loadDetail(id);
      this.loadComments(id);
    }
  },

  loadDetail(id) {
    request('/salary/detail/' + id).then(data => {
      const salaryData = data;
      if (salaryData && salaryData.totalPackage) {
        const num = Number(salaryData.totalPackage);
        salaryData.formattedSalary = !isNaN(num) ? (num / 10000).toFixed(0) : '0';
      } else {
        salaryData.formattedSalary = '0';
      }
      this.setData({ salary: salaryData });
    }).catch(err => {
      wx.showToast({ title: '加载失败', icon: 'none' });
    });
  },

  loadComments(id) {
    request('/comment/list', 'GET', { contentId: id, contentType: 2 }).then(data => {
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
    const salary = this.data.salary;
    if (!salary || !salary.id) {
      wx.showToast({ title: '数据异常', icon: 'none' });
      return;
    }
    request('/like/toggle', 'POST', {
      userId,
      contentType: 2,
      contentId: salary.id
    }).then(data => {
      if (data && data.hasLiked !== undefined) {
        this.setData({
          isLiked: data.hasLiked,
          ['salary.likeCount']: (salary.likeCount || 0) + (data.hasLiked ? 1 : -1)
        });
      }
    }).catch(err => {
      console.error('点赞失败:', err);
      wx.showToast({ title: '操作失败', icon: 'none' });
    });
  },

  handleCollect() {
    const userId = getApp().globalData.userInfo?.id;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    const salary = this.data.salary;
    if (!salary || !salary.id) {
      wx.showToast({ title: '数据异常', icon: 'none' });
      return;
    }
    request('/collect/toggle', 'POST', {
      userId,
      contentType: 2,
      contentId: salary.id
    }).then(data => {
      if (data && data.hasCollected !== undefined) {
        this.setData({ isCollected: data.hasCollected });
        wx.showToast({ title: data.hasCollected ? '收藏成功' : '已取消收藏', icon: 'none' });
      }
    }).catch(err => {
      console.error('收藏失败:', err);
      wx.showToast({ title: '操作失败', icon: 'none' });
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
      contentId: this.data.salary.id,
      contentType: 2,
      content: content,
      isAnonymous: 1
    }).then(() => {
      wx.showToast({ title: '评论成功', icon: 'success' });
      this.setData({ commentContent: '' });
      this.loadComments(this.data.salary.id);
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
      title: this.data.salary?.title,
      path: '/pages/content/salary/salary?id=' + this.data.salary?.id
    };
  }
});