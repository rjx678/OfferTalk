const { request } = require('../../../utils/request.js');

Page({
  data: {
    selectedCompanies: [],
    companyList: [],
    searchKeyword: '',
    weights: {
      salaryWeight: 0.3,
      cultureWeight: 0.2,
      workLifeWeight: 0.2,
      developmentWeight: 0.2,
      welfareWeight: 0.1
    },
    weightPercent: {
      salaryWeight: 30,
      cultureWeight: 20,
      workLifeWeight: 20,
      developmentWeight: 20,
      welfareWeight: 10
    },
    compareResult: null
  },

  onLoad() {
    this.loadCompanies();
  },

  loadCompanies(keyword = '') {
    request('/company-compare/search', 'GET', { keyword }).then(companies => {
      this.setData({
        companyList: companies || []
      });
    }).catch(() => {
      this.setData({
        companyList: [
          { companyId: 1, companyName: '阿里巴巴', logoUrl: '', industry: '互联网', stage: '上市公司', tags: ['高薪', '推荐'] },
          { companyId: 2, companyName: '腾讯', logoUrl: '', industry: '互联网', stage: '上市公司', tags: ['高薪', 'WLB友好'] },
          { companyId: 3, companyName: '字节跳动', logoUrl: '', industry: '互联网', stage: '独角兽', tags: ['高薪', '强度较大'] },
          { companyId: 4, companyName: '美团', logoUrl: '', industry: '生活服务', stage: '上市公司', tags: ['推荐'] },
          { companyId: 5, companyName: '京东', logoUrl: '', industry: '电商', stage: '上市公司', tags: ['WLB友好'] },
          { companyId: 6, companyName: '百度', logoUrl: '', industry: '互联网', stage: '上市公司', tags: ['推荐'] },
          { companyId: 7, companyName: '华为', logoUrl: '', industry: '通信', stage: '上市公司', tags: ['高薪', '强度较大'] },
          { companyId: 8, companyName: '小米', logoUrl: '', industry: '智能硬件', stage: '上市公司', tags: ['WLB友好'] }
        ]
      });
    });
  },

  onCompanyPickerChange(e) {
    const index = parseInt(e.detail.value);
    const selectedCompany = this.data.companyList[index];
    if (!selectedCompany) return;

    const { selectedCompanies } = this.data;

    if (selectedCompanies.length >= 3) {
      wx.showToast({ title: '最多选择3家公司', icon: 'none' });
      return;
    }

    const exists = selectedCompanies.some(c => c.companyId === selectedCompany.companyId);
    if (exists) {
      wx.showToast({ title: '已添加该公司', icon: 'none' });
      return;
    }

    selectedCompanies.push(selectedCompany);
    this.setData({ selectedCompanies });
  },

  removeCompany(e) {
    const id = e.currentTarget.dataset.id;
    const selectedCompanies = this.data.selectedCompanies.filter(c => c.companyId !== id);
    this.setData({
      selectedCompanies,
      compareResult: null
    });
  },

  onWeightChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = parseFloat(e.detail.value) / 10;
    const percent = Math.round(value * 100);
    this.setData({
      [`weights.${field}`]: value,
      [`weightPercent.${field}`]: percent
    });
  },

  compare() {
    const { selectedCompanies, weights } = this.data;

    if (selectedCompanies.length < 2) {
      wx.showToast({ title: '请至少选择2家公司', icon: 'none' });
      return;
    }

    const companyIds = selectedCompanies.map(c => c.companyId);

    wx.showLoading({ title: '对比中...' });
    request('/company-compare/compare', 'POST', { companyIds, weights }).then(result => {
      wx.hideLoading();
      this.setData({ compareResult: result });
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '对比失败', icon: 'none' });
    });
  },

  formatNumber(num) {
    if (!num) return '0';
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }
});