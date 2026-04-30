const { request } = require('../../utils/request.js');

Page({
  data: {
    tabBarCurrent: 0,
    currentCategory: 'interview',
    currentSort: 'latest',
    currentRecruit: null,
    contents: [],
    page: 1,
    loading: false,
    hasMore: true,
    keyword: '',
    searchValue: ''
  },

  onLoad() {
    this.loadContents();
    this.checkVersion();
  },

  checkVersion() {
    const version = wx.getSystemInfoSync().SDKVersion;
    console.log('SDK Version:', version);
  },

  loadContents(isRefresh = false) {
    if (this.data.loading) return;
    if (isRefresh) {
      this.setData({ page: 1, contents: [], hasMore: true });
    }
    if (!this.data.hasMore) return;

    this.setData({ loading: true });

    const apiMap = {
      'interview': '/post/list',
      'salary': '/salary/list',
      'review': '/review/list'
    };

    const api = apiMap[this.data.currentCategory] || '/post/list';

    const params = {
      sortBy: this.data.currentSort,
      keyword: this.data.keyword,
      page: this.data.page,
      size: 10
    };

    if (this.data.currentRecruit) {
      params.recruitType = parseInt(this.data.currentRecruit);
    }

    console.log('[Category Filter] Current category:', this.data.currentCategory);
    console.log('[Category Filter] Request API:', api);
    console.log('[Category Filter] Request params:', params);

    request(api, 'GET', params).then(data => {
      console.log('[Category Filter] Response:', data);
      const records = data?.records || [];
      const newContents = records.map(item => this.formatContent(api,item));
      this.setData({
        contents: isRefresh ? newContents : [...this.data.contents, ...newContents],
        page: this.data.page + 1,
        hasMore: records.length === 10,
        loading: false
      });
      console.log('[Category Filter] Loaded', newContents.length, 'items for category', this.data.currentCategory);
    }).catch((error) => {
      console.error('[Category Filter] Error:', error);
      this.setData({ loading: false });
    });
  },

  formatContent(api,item) {
    let type = 'interview';
    let typeName = '面经';

    if (api === '/salary/list') {
      type = 'salary';
      typeName = '薪资';
    } else if (api === '/review/list') {
      type = 'review';
      typeName = '评价';
    }

    const safeSubstring = (str, len) => {
      return (str && typeof str === 'string') ? str.substring(0, len) : '';
    };

    const safeFormatSalary = (value) => {
      if (!value) return '0';
      var num = Number(value);
      if (isNaN(num)) return '0';
      return (num / 10000).toFixed(0);
    };

    return {
      id: item.id,
      type: type,
      typeName: typeName,
      companyName: item.companyName || item.company?.name || '某不知名小公司',
      companyLogo: item.companyLogo || item.company?.logoUrl || '/images/default-avatar.jpeg',
      jobTitle: item.jobCategory || item.title?.split('-')[0] || '研发',
      title: item.title,
      contentPreview: safeSubstring(item.experienceText, 80) || safeSubstring(item.addText, 80) || safeSubstring(item.prosText, 80) || '',
      totalPackage: safeFormatSalary(item.totalPackage),
      city: item.city || '',
      level: item.level || '',
      status: item.status || '',
      workExperience: item.workExperience || '',
      rating: item.ratingTotal || 0,
      recruitType: item.recruitType === 1 ? '在职' : item.recruitType === 2 ? '离职' : '面试过',
      truthScore: item.truthScore || 5.0,
      likeCount: item.likeCount || 0,
      commentCount: item.commentCount || 0,
      viewCount: item.viewCount || 0,
      createTime: this.formatTime(item.createTime)
    };
  },

  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    const now = new Date();
    const diff = now - date;
    if (diff < 60000) return '刚刚';
    if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前';
    if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前';
    if (diff < 604800000) return Math.floor(diff / 86400000) + '天前';
    return timeStr.substring(0, 10);
  },

  switchCategory(e) {
    const category = e.currentTarget.dataset.category;
    this.setData({ currentCategory: category, page: 1, contents: [], hasMore: true });
    this.loadContents(true);
  },

  switchSort(e) {
    const sort = e.currentTarget.dataset.sort;
    this.setData({ currentSort: sort, page: 1, contents: [], hasMore: true });
    this.loadContents(true);
  },

  switchRecruit(e) {
    const recruit = e.currentTarget.dataset.recruit;
    const newRecruit = this.data.currentRecruit === recruit ? null : recruit;
    this.setData({ currentRecruit: newRecruit, page: 1, contents: [], hasMore: true });
    this.loadContents(true);
  },

  onSearch(e) {
    const keyword = e.detail.value || this.data.searchValue;
    this.setData({ keyword, searchValue: keyword, page: 1, hasMore: true });
    this.loadContents(true);
  },

  loadMore() {
    this.loadContents();
  },

  toDetail(e) {
    const id = e.currentTarget.dataset.id;
    const type = e.currentTarget.dataset.type;

    let url = '';
    switch (type) {
      case 'interview':
        url = `/pages/content/interview/interview?id=${id}`;
        break;
      case 'salary':
        url = `/pages/content/salary/salary?id=${id}`;
        break;
      case 'review':
        url = `/pages/content/review/review?id=${id}`;
        break;
      default:
        url = `/pages/content/interview/interview?id=${id}`;
    }

    wx.navigateTo({
      url: url
    });
  },

  toCompanyList() {
    wx.navigateTo({ url: '/pages/company/list' });
  },

  onPullDownRefresh() {
    this.loadContents(true);
    wx.stopPullDownRefresh();
  },

  onShareAppMessage() {
    return {
      title: 'OfferTalk求职爆料 - 互联网研发面经薪资分享',
      path: '/pages/index/index'
    };
  }
});