const { request } = require('../../../utils/request.js');

Page({
  data: {
    formData: {
      userId: null,
      nickname: '',
      avatarUrl: '',
      phone: '',
      bio: ''
    },
    profile: {
      totalPostCount: 0,
      totalLikeCount: 0,
      totalCollectCount: 0,
      totalCommentCount: 0
    }
  },

  onLoad() {
    this.loadProfile();
  },

  loadProfile() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const userId = userInfo.id;

    request('/user/settings/profile', 'GET', { userId }).then(res => {
      const data = res.data || {};
      this.setData({
        formData: {
          userId: data.userId,
          nickname: data.nickname || '',
          avatarUrl: data.avatarUrl || '',
          phone: data.phone || '',
          bio: data.bio || ''
        },
        profile: {
          totalPostCount: data.totalPostCount || 0,
          totalLikeCount: data.totalLikeCount || 0,
          totalCollectCount: data.totalCollectCount || 0,
          totalCommentCount: data.totalCommentCount || 0
        }
      });
    }).catch(() => {
      this.setData({
        formData: {
          userId: userInfo.id,
          nickname: userInfo.nickname || '',
          avatarUrl: userInfo.avatarUrl || '',
          phone: userInfo.phone || '',
          bio: userInfo.bio || ''
        }
      });
    });
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  changeAvatar() {
    wx.showActionSheet({
      itemList: ['拍照', '从相册选择'],
      success: (res) => {
        const sourceType = res.tapIndex === 0 ? ['camera'] : ['album'];
        wx.chooseImage({
          count: 1,
          sourceType: sourceType,
          success: (res) => {
            const tempFilePath = res.tempFilePaths[0];
            this.uploadAvatar(tempFilePath);
          }
        });
      }
    });
  },

  uploadAvatar(filePath) {
    wx.showLoading({ title: '上传中...' });

    wx.uploadFile({
      url: 'https://your-api-domain.com/api/upload/avatar',
      filePath: filePath,
      name: 'file',
      header: {
        'Content-Type': 'multipart/form-data'
      },
      success: (res) => {
        wx.hideLoading();
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          this.setData({
            'formData.avatarUrl': data.data.url
          });
          wx.showToast({ title: '上传成功', icon: 'success' });
        } else {
          wx.showToast({ title: '上传失败', icon: 'none' });
        }
      },
      fail: () => {
        wx.hideLoading();
        this.setData({
          'formData.avatarUrl': filePath
        });
        wx.showToast({ title: '本地预览', icon: 'success' });
      }
    });
  },

  saveProfile() {
    const { formData } = this.data;

    if (!formData.nickname || formData.nickname.trim() === '') {
      wx.showToast({ title: '请输入昵称', icon: 'none' });
      return;
    }

    if (formData.nickname.length < 2) {
      wx.showToast({ title: '昵称至少2个字符', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    request('/user/settings/profile/update', 'POST', {
      userId: formData.userId,
      nickname: formData.nickname,
      avatarUrl: formData.avatarUrl,
      bio: formData.bio
    }).then(() => {
      wx.hideLoading();
      wx.showToast({ title: '保存成功', icon: 'success' });

      getApp().globalData.userInfo.nickname = formData.nickname;
      getApp().globalData.userInfo.avatarUrl = formData.avatarUrl;

      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '保存失败', icon: 'none' });
    });
  }
});
