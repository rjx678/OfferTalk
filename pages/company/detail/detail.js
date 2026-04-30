const { request } = require('../../../utils/request.js');

Page({
  data: {
    company: null,
    currentTab: 0,
    interviews: [],
    salaries: [],
    reviews: [],
    page: 1,
    loading: false,
    hasMore: true,
    counts: {
      interview: 0,
      salary: 0,
      review: 0
    }
  },

  onLoad(options) {
    const id = options.id;
    if (id) {
      this.loadCompanyDetail(id);
      this.loadContent();
    }
  },

  loadCompanyDetail(id) {
    request('/company/detail/' + id).then(data => {
      this.setData({ company: data });
      this.loadContent();
      this.loadCompanyCounts(id);
    }).catch(err => {
      console.error('加载公司详情失败:', err);
    });
  },

  loadCompanyCounts(companyId) {
    Promise.all([
      request('/post/list', 'GET', { companyId, page: 1, size: 1 }),
      request('/salary/list', 'GET', { companyId, page: 1, size: 1 }),
      request('/review/list', 'GET', { companyId, page: 1, size: 1 })
    ]).then(([interviewData, salaryData, reviewData]) => {
      const interviewCount = interviewData?.total || 0;
      const salaryCount = salaryData?.total || 0;
      const reviewCount = reviewData?.total || 0;
      
      this.setData({
        counts: {
          interview: interviewCount,
          salary: salaryCount,
          review: reviewCount
        }
      });
    }).catch(err => {
      console.error('加载统计数据失败:', err);
    });
  },

  loadContent() {
    const companyId = this.data.company?.id;
    if (!companyId) return;

    this.setData({ page: 1, interviews: [], salaries: [], reviews: [], hasMore: true });

    if (this.data.currentTab === 0) {
      this.loadInterviews();
    } else if (this.data.currentTab === 1) {
      this.loadSalaries();
    } else if (this.data.currentTab === 2) {
      this.loadReviews();
    }
  },

  loadInterviews() {
    request('/post/list', 'GET', {
      companyId: this.data.company.id,
      page: this.data.page,
      size: 10
    }).then(data => {
      const records = data?.records || [];
      this.setData({
        interviews: records,
        hasMore: records.length === 10
      });
    });
  },

  loadSalaries() {
    request('/salary/list', 'GET', {
      companyId: this.data.company.id,
      page: this.data.page,
      size: 10
    }).then(data => {
      const records = (data?.records || []).map(item => {
        const num = Number(item.totalPackage);
        item.formattedSalary = !isNaN(num) ? (num / 10000).toFixed(0) : '0';
        return item;
      });
      this.setData({
        salaries: records,
        hasMore: records.length === 10
      });
    });
  },

  loadReviews() {
    request('/review/list', 'GET', {
      companyId: this.data.company.id,
      page: this.data.page,
      size: 10
    }).then(data => {
      const records = data?.records || [];
      this.setData({
        reviews: records,
        hasMore: records.length === 10
      });
    });
  },

  switchTab(e) {
    const id = parseInt(e.currentTarget.dataset.id);
    if (id === this.data.currentTab) return;
    this.setData({ currentTab: id });
    this.loadContent();
  },

  toInterviewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/content/interview/interview?id=' + id });
  },

  toSalaryDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/content/salary/salary?id=' + id });
  },

  toReviewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/content/review/review?id=' + id });
  },

  onShareAppMessage() {
    return {
      title: this.data.company?.name + ' - 公司详情',
      path: '/pages/company/detail/detail?id=' + this.data.company?.id
    };
  }
});