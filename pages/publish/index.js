const { request } = require('../../utils/request.js');

Page({
  data: {
    tabBarCurrent: 2,
    publishType: 1,
    types: [
      { id: 1, name: '面试经验' },
      { id: 2, name: '薪资爆料' },
      { id: 3, name: '公司评价' }
    ],
    recruitTypes: [
      { id: 1, name: '校招' },
      { id: 2, name: '社招' },
      { id: 3, name: '实习' }
    ],
    companies: [],
    selectedCompany: null,
    formData: {
      title: '',
      companyId: '',
      recruitType: 1,
      jobCategory: '',
      city: '',
      content: '',
      tags: '',
      workExperience: ''
    },
    timeline: [],
    salaryData: {
      monthlyBase: '',
      totalPackage: '',
      signingBonus: '',
      annualBonus: '',
      level: '',
      overtimeSituation: ''
    },
    reviewData: {
      prosText: '',
      consText: '',
      ratingTotal: 5,
      ratingSalary: 5,
      ratingCulture: 5,
      ratingOvertime: 5,
      ratingPromotion: 5
    },
    isAnonymous: true,
    submitting: false
  },

  onLoad() {
    this.loadCompanies();
  },

  onShow() {
    this.loadCompanies();
  },

  loadCompanies() {
    request('/company/search', 'GET', { keyword: '' }).then(data => {
      this.setData({ companies: data || [] });
    }).catch(() => {
      this.setData({ companies: [] });
    });
  },

  switchType(e) {
    const id = e.currentTarget.dataset.id;
    this.setData({ publishType: id });
  },

  switchRecruitType(e) {
    const id = e.currentTarget.dataset.id;
    this.setData({
      'formData.recruitType': id
    });
  },

  selectCompany(e) {
    const index = e.detail.value;
    const company = this.data.companies[index];
    if (company) {
      this.setData({
        'formData.companyId': company.id,
        selectedCompany: company
      });
    }
  },

  onInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      ['formData.' + field]: e.detail.value
    });
  },

  onSalaryInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      ['salaryData.' + field]: e.detail.value
    });
  },

  onReviewInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      ['reviewData.' + field]: e.detail.value
    });
  },

  setRating(e) {
    const field = e.currentTarget.dataset.field;
    const value = parseInt(e.currentTarget.dataset.value);
    this.setData({
      ['reviewData.' + field]: value
    });
  },

  addTimelineItem() {
    const timeline = this.data.timeline;
    timeline.push({
      stage: '',
      date: '',
      result: ''
    });
    this.setData({ timeline });
  },

  removeTimelineItem(e) {
    const index = e.currentTarget.dataset.index;
    const timeline = this.data.timeline;
    timeline.splice(index, 1);
    this.setData({ timeline });
  },

  onTimelineChange(e) {
    const index = e.currentTarget.dataset.index;
    const field = e.currentTarget.dataset.field;
    const timeline = this.data.timeline;
    timeline[index][field] = e.detail.value;
    this.setData({ timeline });
  },

  addTag(e) {
    const tag = e.currentTarget.dataset.tag;
    const currentTags = this.data.formData.tags;
    if (currentTags) {
      const tagsArray = currentTags.split(',').map(t => t.trim()).filter(t => t);
      if (!tagsArray.includes(tag)) {
        tagsArray.push(tag);
        this.setData({
          'formData.tags': tagsArray.join(',')
        });
      }
    } else {
      this.setData({
        'formData.tags': tag
      });
    }
  },

  toggleAnonymous() {
    this.setData({ isAnonymous: !this.data.isAnonymous });
  },

  checkSensitiveWords(content) {
    const sensitiveWords = ['习近平', '彭丽媛', '江泽民'];
    for (const word of sensitiveWords) {
      if (content.includes(word)) {
        return true;
      }
    }
    return false;
  },

  validateForm() {
    const { title, companyId, jobCategory, city, content, recruitType, workExperience } = this.data.formData;

    if (!companyId) {
      wx.showToast({ title: '请选择公司', icon: 'none' });
      return false;
    }

    if (!title || title.trim().length < 5) {
      wx.showToast({ title: '标题至少5个字符', icon: 'none' });
      return false;
    }

    if (!jobCategory) {
      wx.showToast({ title: '请填写岗位类别', icon: 'none' });
      return false;
    }

    if (!city) {
      wx.showToast({ title: '请填写工作城市', icon: 'none' });
      return false;
    }

    if (recruitType === 2 && !workExperience) {
      wx.showToast({ title: '请填写工作年限', icon: 'none' });
      return false;
    }

    if (this.publishType === 1 && (!content || content.trim().length < 10)) {
      wx.showToast({ title: '面试经验详情至少10个字符', icon: 'none' });
      return false;
    }

    if (this.checkSensitiveWords(title)) {
      wx.showToast({ title: '标题包含敏感词', icon: 'none' });
      return false;
    }

    if (this.checkSensitiveWords(content)) {
      wx.showToast({ title: '内容包含敏感词', icon: 'none' });
      return false;
    }

    return true;
  },

  checkLogin() {
    const userInfo = getApp().globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showModal({
        title: '请先登录',
        content: '发布内容需要先登录',
        success: (res) => {
          if (res.confirm) {
            wx.switchTab({ url: '/pages/mine/index' });
          }
        }
      });
      return false;
    }
    return true;
  },

  clearForm() {
    this.setData({
      formData: {
        title: '',
        companyId: '',
        recruitType: 1,
        jobCategory: '',
        city: '',
        content: '',
        tags: '',
        workExperience: ''
      },
      timeline: [],
      salaryData: {
        monthlyBase: '',
        totalPackage: '',
        signingBonus: '',
        annualBonus: '',
        level: '',
        overtimeSituation: ''
      },
      reviewData: {
        prosText: '',
        consText: '',
        ratingTotal: 5,
        ratingSalary: 5,
        ratingCulture: 5,
        ratingOvertime: 5,
        ratingPromotion: 5
      },
      isAnonymous: true,
      selectedCompany: null
    });
  },

  submitForm() {
    if (this.data.submitting) return;

    if (!this.checkLogin()) return;

    if (!this.validateForm()) return;

    this.setData({ submitting: true });

    const { formData, publishType, isAnonymous, timeline, salaryData, reviewData } = this.data;

    const userInfo = getApp().globalData.userInfo;

    let payload = {
      ...formData,
      userId: userInfo.id,
      isAnonymous: isAnonymous ? 1 : 0,
      auditStatus: 0
    };

    if (publishType === 1) {
      payload.timelineData = JSON.stringify(timeline);
      payload.experienceText = formData.content;
    } else if (publishType === 2) {
      payload = { ...payload, ...salaryData };
    } else if (publishType === 3) {
      payload = { ...payload, ...reviewData };
      payload.prosText = reviewData.prosText;
      payload.consText = reviewData.consText;
    }

    const apiMap = {
      1: '/post/interview/create',
      2: '/salary/create',
      3: '/review/create'
    };

    request(apiMap[publishType], 'POST', payload).then(() => {
      wx.hideLoading();
      this.setData({ submitting: false });
      wx.showToast({ title: '发布成功', icon: 'success' });
      this.clearForm();
      setTimeout(() => {
        wx.switchTab({ url: '/pages/index/index' });
      }, 1500);
    }).catch(() => {
      wx.hideLoading();
      this.setData({ submitting: false });
      wx.showToast({ title: '发布失败，请重试', icon: 'none' });
    });
  }
});