const { request, uploadFile, getFullImageUrl } = require('../../../utils/request.js');

Page({
  data: {
    formData: {
      userId: null,
      nickname: '',
      avatarUrl: '',
      phone: '',
      bio: ''
    },
    displayAvatarUrl: '',
    profile: {
      totalPostCount: 0,
      totalLikeCount: 0,
      totalCollectCount: 0,
      totalCommentCount: 0
    },
    uploading: false
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

    request('/user/settings/profile', 'GET', { userId }).then(data => {
      const avatarUrl = data.avatarUrl || '';
      this.setData({
        formData: {
          userId: data.userId,
          nickname: data.nickname || '',
          avatarUrl: avatarUrl,
          phone: data.phone || '',
          bio: data.bio || ''
        },
        displayAvatarUrl: getFullImageUrl(avatarUrl),
        profile: {
          totalPostCount: data.totalPostCount || 0,
          totalLikeCount: data.totalLikeCount || 0,
          totalCollectCount: data.totalCollectCount || 0,
          totalCommentCount: data.totalCommentCount || 0
        }
      });
    }).catch(() => {
      const avatarUrl = userInfo.avatarUrl || '';
      this.setData({
        formData: {
          userId: userInfo.id,
          nickname: userInfo.nickname || '',
          avatarUrl: avatarUrl,
          phone: userInfo.phone || '',
          bio: userInfo.bio || ''
        },
        displayAvatarUrl: getFullImageUrl(avatarUrl)
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
          sizeType: ['compressed'],
          success: (res) => {
            const tempFilePath = res.tempFilePaths[0];
            console.log('选择图片:', tempFilePath);
            this.compressImage(tempFilePath);
          }
        });
      }
    });
  },

  compressImage(filePath) {
    console.log('开始压缩图片...');
    wx.compressImage({
      src: filePath,
      quality: 80,
      success: (res) => {
        console.log('压缩成功:', res.tempFilePath);
        this.previewAndUpload(res.tempFilePath);
      },
      fail: () => {
        console.log('压缩失败，直接上传原图');
        this.previewAndUpload(filePath);
      }
    });
  },

  previewAndUpload(filePath) {
    // 先预览
    this.setData({
      'formData.avatarUrl': filePath
    });

    // 确认上传
    wx.showModal({
      title: '确认上传',
      content: '确定使用这张图片作为头像吗？',
      success: (res) => {
        if (res.confirm) {
          this.uploadAvatar(filePath);
        }
      }
    });
  },

  uploadAvatar(filePath) {
    if (this.data.uploading) {
      return;
    }

    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    this.setData({ uploading: true });
    wx.showLoading({ title: '上传中...' });

    uploadFile('/user/avatar/upload', filePath, {
      userId: userInfo.id
    }).then(data => {
      console.log('上传成功:', data);
      wx.hideLoading();
      const avatarUrl = data.avatarUrl;
      this.setData({
        'formData.avatarUrl': avatarUrl,
        uploading: false
      });
      wx.showToast({ title: '上传成功', icon: 'success' });

      // 更新全局用户信息
      getApp().globalData.userInfo.avatarUrl = avatarUrl;
    }).catch(err => {
      console.error('上传失败:', err);
      wx.hideLoading();
      this.setData({ uploading: false });
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
      phone: formData.phone,
      bio: formData.bio
    }).then(() => {
      wx.hideLoading();
      wx.showToast({ title: '保存成功', icon: 'success' });

      getApp().globalData.userInfo.nickname = formData.nickname;
      getApp().globalData.userInfo.avatarUrl = formData.avatarUrl;
      getApp().globalData.userInfo.bio = formData.bio;
      getApp().globalData.userInfo.phone = formData.phone;

      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '保存失败', icon: 'none' });
    });
  }
});