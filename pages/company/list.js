const { request } = require('../../utils/request.js');

Page({
  data: {
    tabBarCurrent: 1,
    companies: [],
    loading: false,
    keyword: '',
    hasMore: true,
    page: 1
  },

  onLoad() {
    this.loadCompanies();
  },

  loadCompanies(isRefresh = false) {
    if (this.data.loading) return;

    const page = isRefresh ? 1 : this.data.page;
    if (!isRefresh && !this.data.hasMore) return;

    this.setData({ loading: true });

    request('/company/list', 'GET', {
      keyword: this.data.keyword,
      page: page,
      size: 20
    }).then(data => {
      const records = data?.records || [];
      const total = data?.total || 0;
      this.setData({
        companies: isRefresh ? records : [...this.data.companies, ...records],
        loading: false,
        hasMore: records.length === 20,
        page: page + 1
      });
    }).catch(() => {
      this.setData({ loading: false });
    });
  },

  onSearch(e) {
    this.setData({ keyword: e.detail.value, page: 1, hasMore: true });
    this.loadCompanies(true);
  },

  loadMore() {
    if (!this.data.loading && this.data.hasMore) {
      this.loadCompanies();
    }
  },

  toCompanyDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/company/detail/detail?id=${id}`
    });
  },

  onPullDownRefresh() {
    this.setData({ page: 1, hasMore: true });
    this.loadCompanies(true);
    wx.stopPullDownRefresh();
  }
});