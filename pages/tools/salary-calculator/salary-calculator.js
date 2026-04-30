// pages/tools/salary-calculator/salary-calculator.js
const { request } = require('../../../utils/request.js');

const DEFAULT_CITIES = [
  { cityCode: 'BJ', cityName: '北京' },
  { cityCode: 'SH', cityName: '上海' },
  { cityCode: 'SZ', cityName: '深圳' },
  { cityCode: 'HZ', cityName: '杭州' },
  { cityCode: 'GZ', cityName: '广州' },
  { cityCode: 'CD', cityName: '成都' },
  { cityCode: 'WH', cityName: '武汉' },
  { cityCode: 'XA', cityName: '西安' }
];

Page({
  data: {
    cities: DEFAULT_CITIES,
    selectedCity: 'BJ',
    selectedCityName: '北京',
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
    result: null,
    showResult: false,
    formattedResult: {
      grossAnnualSalary: '0',
      netAnnualSalary: '0',
      netMonthlySalary: '0',
      annualTax: '0',
      annualSocialInsurance: '0',
      annualHousingFund: '0',
      monthlyDetail: {
        grossSalary: '0',
        pension: '0',
        medical: '0',
        unemployment: '0',
        housingFund: '0',
        tax: '0',
        netSalary: '0'
      }
    }
  },

  onLoad() {
    console.log('薪资计算器页面加载');
    this.loadCities();
  },

  openCitySelector() {
    wx.navigateTo({
      url: `/pages/tools/city-selector/city-selector?selectedCityCode=${this.data.selectedCity}&selectedCityName=${this.data.selectedCityName || ''}`
    });
  },

  onCitySelected(city) {
    console.log('收到城市选择结果:', city);
    
    if (city && city.cityCode) {
      this.setData({
        selectedCity: city.cityCode,
        selectedCityName: city.cityName
      });
      
      this.loadCityConfig(city.cityCode);
      
      wx.showToast({
        title: `已选择${city.cityName}`,
        icon: 'success',
        duration: 1500
      });
    }
  },

  loadCities() {
    console.log('开始加载城市数据...');
    
    request('/salary-calculator/cities', 'GET').then(cities => {
      console.log('城市数据返回:', cities);
      
      let validCities = [];
      
      if (cities && Array.isArray(cities) && cities.length > 0) {
        validCities = cities.map(city => ({
          cityCode: city.cityCode || city.code,
          cityName: city.cityName || city.name
        })).filter(city => city.cityCode && city.cityName);
      }
      
      if (validCities.length === 0) {
        console.log('API返回的城市数据无效，使用默认数据');
        validCities = DEFAULT_CITIES;
      }
      
      this.setData({
        cities: validCities
      });
      
    }).catch(err => {
      console.log('城市API调用失败，使用默认数据', err);
      this.setData({
        cities: DEFAULT_CITIES
      });
    });
  },

  loadCityConfig(cityCode) {
    request('/salary-calculator/cities/' + cityCode, 'GET').then(config => {
      console.log('城市配置返回:', config);
      if (config) {
        this.setData({
          currentConfig: {
            pensionRatio: config.pensionRatio !== undefined ? config.pensionRatio : 8.0,
            medicalRatio: config.medicalRatio !== undefined ? config.medicalRatio : 2.0,
            unemploymentRatio: config.unemploymentRatio !== undefined ? config.unemploymentRatio : 0.5,
            housingFundRatio: config.housingFundRatio !== undefined ? config.housingFundRatio : 12.0
          }
        });
      }
    }).catch(err => {
      console.log('城市配置API调用失败，保持当前配置', err);
    });
  },

  selectCity(e) {
    const cityCode = e.currentTarget.dataset.code;
    const city = this.data.cities.find(c => c.cityCode === cityCode);
    const cityName = city ? city.cityName : '';
    
    this.setData({ 
      selectedCity: cityCode,
      selectedCityName: cityName
    });
    
    this.loadCityConfig(cityCode);
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
      
      if (result) {
        // 格式化数据
        const formattedResult = this.formatResult(result);
        
        this.setData({
          result: result,
          formattedResult: formattedResult,
          showResult: true
        });
        
        // 滚动到结果区域
        setTimeout(() => {
          wx.pageScrollTo({
            selector: '.result-section',
            duration: 300
          });
        }, 100);
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('计算失败：', err);
      wx.showToast({ title: '计算失败，请稍后重试', icon: 'none' });
    });
  },

  /**
   * 格式化结果数据
   */
  formatResult(result) {
    const formatNum = (num) => {
      if (!num && num !== 0) return '0';
      const n = Number(num);
      if (isNaN(n)) return '0';
      return n.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    };
    
    return {
      grossAnnualSalary: formatNum(result.grossAnnualSalary),
      netAnnualSalary: formatNum(result.netAnnualSalary),
      netMonthlySalary: formatNum(result.netMonthlySalary),
      annualTax: formatNum(result.annualTax),
      annualSocialInsurance: formatNum(result.annualSocialInsurance),
      annualHousingFund: formatNum(result.annualHousingFund),
      monthlyDetail: {
        grossSalary: formatNum(result.monthlyDetail?.grossSalary || 0),
        pension: formatNum(result.monthlyDetail?.pension || 0),
        medical: formatNum(result.monthlyDetail?.medical || 0),
        unemployment: formatNum(result.monthlyDetail?.unemployment || 0),
        housingFund: formatNum(result.monthlyDetail?.housingFund || 0),
        tax: formatNum(result.monthlyDetail?.tax || 0),
        netSalary: formatNum(result.monthlyDetail?.netSalary || 0)
      }
    };
  },

  formatNumber(num) {
    if (!num && num !== 0) return '0';
    const n = Number(num);
    if (isNaN(n)) return '0';
    return n.toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }
});