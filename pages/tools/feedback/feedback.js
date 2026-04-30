const { request } = require('../../../utils/request.js');

Page({
  data: {
    feedbackType: 1,
    content: '',
    images: [],
    contact: '',
    historyList: []
  },

  onLoad() {
    this.loadHistory();
  },

  onShow() {
    this.loadHistory();
  },

  selectType(e) {
    const type = parseInt(e.currentTarget.dataset.type);
    this.setData({ feedbackType: type });
  },

  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  onContactInput(e) {
    this.setData({ contact: e.detail.value });
  },

  chooseImage() {
    wx.chooseImage({
      count: 3 - this.data.images.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const images = this.data.images.concat(res.tempFilePaths);
        this.setData({ images });
      }
    });
  },

  removeImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.images.filter((_, i) => i !== index);
    this.setData({ images });
  },

  submitFeedback() {
    const { feedbackType, content, images, contact } = this.data;
    
    if (!content || content.trim().length === 0) {
      wx.showToast({ title: '请输入反馈内容', icon: 'none' });
      return;
    }

    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const requestData = {
      type: feedbackType,
      content: content.trim(),
      images: images,
      contact: contact.trim(),
      userId: userInfo.id
    };

    wx.showLoading({ title: '提交中...' });
    request('/feedback/create', 'POST', requestData).then(res => {
      wx.hideLoading();
      wx.showToast({ title: '提交成功', icon: 'success' });
      this.setData({
        content: '',
        images: [],
        contact: ''
      });
      this.loadHistory();
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '提交失败', icon: 'none' });
    });
  },

  loadHistory() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }

    request('/feedback/list', 'GET', { userId: userInfo.id, page: 1, size: 10 }).then(res => {
      const records = res.data?.records || [];
      this.setData({ historyList: records });
    }).catch(() => {
      // 使用模拟数据
      this.setData({
        historyList: [
          { id: 1, type: 1, content: '希望能增加一个薪资计算器功能', status: 2, createTime: '2026-04-28 10:00', replyContent: '感谢您的建议，该功能已在开发中' },
          { id: 2, type: 2, content: '公司对比功能很好用', status: 0, createTime: '2026-04-27 15:30', replyContent: '' },
          { id: 3, type: 3, content: 'App有时加载较慢', status: 1, createTime: '2026-04-26 09:15', replyContent: '我们正在优化性能' }
        ]
      });
    });
  },

  viewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '反馈详情',
      content: '反馈ID: ' + id,
      showCancel: false
    });
  },

  getTypeName(type) {
    const types = { 1: '问题反馈', 2: '功能建议', 3: 'Bug报告' };
    return types[type] || '未知';
  },

  getStatusName(status) {
    const statuses = { 0: '待处理', 1: '处理中', 2: '已回复', 3: '已关闭' };
    return statuses[status] || '未知';
  }
});