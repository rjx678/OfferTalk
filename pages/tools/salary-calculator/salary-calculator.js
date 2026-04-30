const { request } = require('../../../utils/request.js');

Page({
  data: {
    cities: [
      { cityCode: 'BJ', cityName: '北京' },
      { cityCode: 'SH', cityName: '上海' },
      { cityCode: 'SZ', cityName: '深圳' },
      { cityCode: 'HZ', cityName: '杭州' },
      { cityCode: 'GZ', cityName: '广州' },
      { cityCode: 'CD', cityName: '成都' },
      { cityCode: 'WH', cityName: '武汉' },
      { cityCode: 'XA', cityName: '西安' }
    ],
    selectedCity: 'BJ',
    currentConfig: {
      pensionRatio: 8.0,
      medicalRatio: 2.0,
      unemploymentRatio: 0.5,
      housingFundRatio: 12.0
    },
    salaryMonthOptions: ['13薪', '14薪', '15薪', '16薪'],
    selectedSalaryMonth: 0,
    formData: {
      monthlyBase: '',
      annualBonus: '',
      rsuCount: '',
      rsuPrice: ''
    },
    result: null
  },

  onLoad() {
    this.loadCities();
  },

  loadCities() {
    request('/salary-calculator/cities', 'GET').then(cities => {
      console.log('城市数据返回:', cities);
      if (cities && Array.isArray(cities) && cities.length > 0) {
        this.setData({
          cities: cities
        });
      }
    }).catch(err => {
      console.log('城市API调用失败，使用默认数据', err);
    });
  },

  selectCity(e) {
    const cityCode = e.currentTarget.dataset.code;
    console.log('选择城市:', cityCode);
    this.setData({ selectedCity: cityCode });
    request('/salary-calculator/cities/' + cityCode, 'GET').then(config => {
      console.log('城市配置返回:', config);
      if (config) {
        this.setData({
          currentConfig: {
            pensionRatio: config.pensionRatio || 8.0,
            medicalRatio: config.medicalRatio || 2.0,
            unemploymentRatio: config.unemploymentRatio || 0.5,
            housingFundRatio: config.housingFundRatio || 12.0
          }
        });
      }
    }).catch(err => {
      console.log('城市配置API调用失败', err);
    });
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`formData.${field}`]: e.detail.value
    });
  },

  onSalaryMonthChange(e) {
    this.setData({
      selectedSalaryMonth: parseInt(e.detail.value) + 1
    });
  },

  onSliderChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = parseFloat(e.detail.value);
    this.setData({
      [`currentConfig.${field}`]: value
    });
  },

  calculate() {
    const { formData, selectedCity, selectedSalaryMonth, currentConfig } = this.data;

    if (!formData.monthlyBase || parseInt(formData.monthlyBase) <= 0) {
      wx.showToast({ title: '请输入月薪', icon: 'none' });
      return;
    }

    const requestData = {
      monthlyBase: parseInt(formData.monthlyBase),
      annualBonus: formData.annualBonus ? parseInt(formData.annualBonus) : 0,
      salaryMonths: selectedSalaryMonth || 12,
      rsuCount: formData.rsuCount ? parseInt(formData.rsuCount) : 0,
      rsuPrice: formData.rsuPrice ? parseInt(formData.rsuPrice) : 0,
      cityCode: selectedCity,
      pensionRatio: currentConfig.pensionRatio,
      medicalRatio: currentConfig.medicalRatio,
      unemploymentRatio: currentConfig.unemploymentRatio,
      housingFundRatio: currentConfig.housingFundRatio,
      calculateType: 1
    };

    wx.showLoading({ title: '计算中...' });
    request('/salary-calculator/calculate', 'POST', requestData).then(result => {
      wx.hideLoading();
      console.log('计算结果：', result);
      this.setData({ result });
    }).catch(err => {
      wx.hideLoading();
      console.error('计算失败：', err);
      wx.showToast({ title: '计算失败，请稍后重试', icon: 'none' });
    });
  },

  formatNumber(num) {
    if (!num && num !== 0) return '0';
    const n = Number(num);
    if (isNaN(n)) return '0';
    return n.toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }
});