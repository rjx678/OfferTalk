// 注意：真机调试时，不能使用 localhost，必须使用开发机的局域网 IP (如 192.168.x.x)
const BASE_URL = 'http://192.168.0.100:8080/api';
const IMG_BASE_URL = 'http://192.168.0.100:8080';
// 开发环境标识（微信小程序不支持 process.env）
const isDevelopment = true;

// 城市数据常量
const MOCK_CITIES = [
  { cityCode: 'BJ', cityName: '北京', pinyin: 'beijing' },
  { cityCode: 'SH', cityName: '上海', pinyin: 'shanghai' },
  { cityCode: 'SZ', cityName: '深圳', pinyin: 'shenzhen' },
  { cityCode: 'GZ', cityName: '广州', pinyin: 'guangzhou' },
  { cityCode: 'HZ', cityName: '杭州', pinyin: 'hangzhou' },
  { cityCode: 'CD', cityName: '成都', pinyin: 'chengdu' },
  { cityCode: 'WH', cityName: '武汉', pinyin: 'wuhan' },
  { cityCode: 'XA', cityName: '西安', pinyin: 'xian' },
  { cityCode: 'NJ', cityName: '南京', pinyin: 'nanjing' },
  { cityCode: 'CQ', cityName: '重庆', pinyin: 'chongqing' },
  { cityCode: 'TJ', cityName: '天津', pinyin: 'tianjin' },
  { cityCode: 'SU', cityName: '苏州', pinyin: 'suzhou' },
  { cityCode: 'Xiamen', cityName: '厦门', pinyin: 'xiamen' },
  { cityCode: 'QD', cityName: '青岛', pinyin: 'qingdao' },
  { cityCode: 'DL', cityName: '大连', pinyin: 'dalian' },
  { cityCode: 'CS', cityName: '长沙', pinyin: 'changsha' },
  { cityCode: 'SY', cityName: '沈阳', pinyin: 'shenyang' },
  { cityCode: 'JN', cityName: '济南', pinyin: 'jinan' },
  { cityCode: 'ZZ', cityName: '郑州', pinyin: 'zhengzhou' },
  { cityCode: 'KM', cityName: '昆明', pinyin: 'kunming' },
  { cityCode: 'HRB', cityName: '哈尔滨', pinyin: 'haerbin' },
  { cityCode: 'CC', cityName: '长春', pinyin: 'changchun' },
  { cityCode: 'HF', cityName: '合肥', pinyin: 'hefei' },
  { cityCode: 'FJ', cityName: '福州', pinyin: 'fuzhou' },
  { cityCode: 'NC', cityName: '南昌', pinyin: 'nanchang' },
  { cityCode: 'NN', cityName: '南宁', pinyin: 'nanning' },
  { cityCode: 'GY', cityName: '贵阳', pinyin: 'guiyang' },
  { cityCode: 'LZ', cityName: '兰州', pinyin: 'lanzhou' },
  { cityCode: 'YC', cityName: '银川', pinyin: 'yinchuan' },
  { cityCode: 'XJ', cityName: '西宁', pinyin: 'xining' },
  { cityCode: 'Lhasa', cityName: '拉萨', pinyin: 'lasa' },
  { cityCode: 'Urumqi', cityName: '乌鲁木齐', pinyin: 'wulumuqi' },
  { cityCode: 'Hohhot', cityName: '呼和浩特', pinyin: 'huhehaote' }
];

// 城市配置
const MOCK_CITY_CONFIGS = {
  'BJ': { cityCode: 'BJ', cityName: '北京', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.5, housingFundRatio: 12.0 },
  'SH': { cityCode: 'SH', cityName: '上海', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.5, housingFundRatio: 7.0 },
  'SZ': { cityCode: 'SZ', cityName: '深圳', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.3, housingFundRatio: 5.0 },
  'HZ': { cityCode: 'HZ', cityName: '杭州', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.5, housingFundRatio: 12.0 },
  'GZ': { cityCode: 'GZ', cityName: '广州', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.2, housingFundRatio: 5.0 },
  'CD': { cityCode: 'CD', cityName: '成都', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.4, housingFundRatio: 6.0 },
  'WH': { cityCode: 'WH', cityName: '武汉', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.5, housingFundRatio: 8.0 },
  'XA': { cityCode: 'XA', cityName: '西安', pensionRatio: 8.0, medicalRatio: 2.0, unemploymentRatio: 0.5, housingFundRatio: 5.0 }
};

/**
 * 获取完整的图片URL
 * @param {string} path - 图片相对路径或完整URL
 * @returns {string} 完整的图片URL
 */
const getFullImageUrl = (path) => {
  if (!path) return '';
  if (path.startsWith('http')) return path;
  if (path.startsWith('/')) {
    return `${IMG_BASE_URL}${path}`;
  }
  return `${IMG_BASE_URL}/${path}`;
};

const request = (url, method = 'GET', data = {}) => {
  console.log(`[Request] ${method} ${url}`, data);
  return new Promise((resolve, reject) => {
    wx.request({
      url: url.startsWith('http') ? url : `${BASE_URL}${url}`,
      method,
      data,
      header: {
        'content-type': 'application/json'
      },
      success: (res) => {
        console.log(`[Response] ${url}:`, res.statusCode, res.data);
        if (res.statusCode >= 200 && res.statusCode < 300) {
          const responseData = res.data;
          if (responseData && responseData.code === 200) {
            resolve(responseData.data);
          } else if (responseData && responseData.code !== 200) {
            wx.showToast({
              title: responseData.message || '请求失败',
              icon: 'none'
            });
            reject(responseData);
          } else {
            resolve(responseData);
          }
        } else {
          wx.showToast({
            title: `请求失败(${res.statusCode})`,
            icon: 'none'
          });
          reject(res);
        }
      },
      fail: (err) => {
        console.error(`[Response Fail] ${url}:`, err);
        if (isDevelopment) {
           console.log('Request failed, returning mock data for:', url);
           if (url.includes('/post/list')) {
             resolve({
               records: [
                 { id: 1, companyName: '腾讯', jobTitle: '前端开发', title: '腾讯前端面经分享', truthScore: 4.5, likeCount: 120, commentCount: 45, createTime: '2024-03-20' },
                 { id: 2, companyName: '字节跳动', jobTitle: '后端开发', title: '字节跳动三面面经', truthScore: 4.8, likeCount: 88, commentCount: 12, createTime: '2024-03-21' }
               ]
             });
             return;
           }
           if (url.includes('/post/') && !url.includes('/list')) {
             const id = url.split('/').pop();
             resolve({
               id,
               title: '深度解析大厂面试流程',
               companyName: '某大厂',
               jobTitle: '核心开发',
               createTime: '2024-03-22',
               timeline: '3月1日 投递\n3月5日 一面\n3月10日 二面\n3月15日 三面/HR面',
               content: '这里是详细的面试内容描述...\n包含了算法题、项目经验和系统设计等多个维度的考察。',
               likeCount: 50,
               commentCount: 10
             });
             return;
           }
           if (url.includes('/company/list')) {
             resolve([
               { id: 1, name: '腾讯', industry: '互联网', ratingTotal: 4.5, ratingInterview: 4.2, ratingOvertime: 3.8 },
               { id: 2, name: '字节跳动', industry: '互联网', ratingTotal: 4.7, ratingInterview: 4.5, ratingOvertime: 4.2 },
               { id: 3, name: '阿里巴巴', industry: '互联网', ratingTotal: 4.3, ratingInterview: 4.0, ratingOvertime: 4.0 }
             ]);
             return;
           }
           if (url.includes('/salary-calculator/cities') && !url.includes('/cities/')) {
             resolve(MOCK_CITIES);
             return;
           }
           if (url.includes('/salary-calculator/cities/')) {
             const cityCode = url.split('/').pop();
             resolve(MOCK_CITY_CONFIGS[cityCode] || MOCK_CITY_CONFIGS['BJ']);
             return;
           }
           if (url.includes('/salary-calculator/calculate')) {
             resolve({
               grossAnnualSalary: 360000,
               netAnnualSalary: 268000,
               grossMonthlySalary: 30000,
               netMonthlySalary: 22300,
               annualTax: 38000,
               annualSocialInsurance: 43200,
               annualHousingFund: 43200,
               rsuValue: 0,
               monthlyDetail: {
                 grossSalary: 30000,
                 pension: 2400,
                 medical: 600,
                 unemployment: 150,
                 housingFund: 3600,
                 tax: 3167,
                 netSalary: 20083
               },
               composition: {
                 baseRatio: 100,
                 bonusRatio: 0,
                 rsuRatio: 0,
                 socialInsuranceRatio: 12,
                 housingFundRatio: 12,
                 taxRatio: 10.6
               }
             });
             return;
           }
           if (url.includes('/comment/like')) {
             resolve({ hasLiked: true });
             return;
           }
        }
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

/**
 * 上传文件
 * @param {string} url - 上传接口地址
 * @param {string} filePath - 文件临时路径
 * @param {object} formData - 表单数据
 */
const uploadFile = (url, filePath, formData = {}) => {
  console.log(`[Upload File] ${url}`, filePath, formData);
  return new Promise((resolve, reject) => {
    const uploadTask = wx.uploadFile({
      url: url.startsWith('http') ? url : `${BASE_URL}${url}`,
      filePath,
      name: 'file',
      formData,
      success: (res) => {
        console.log(`[Upload Response] ${url}:`, res.statusCode, res.data);
        try {
          const data = JSON.parse(res.data);
          if (res.statusCode >= 200 && res.statusCode < 300) {
            if (data && data.code === 200) {
              resolve(data.data);
            } else if (data && data.code !== 200) {
              wx.showToast({
                title: data.message || '上传失败',
                icon: 'none'
              });
              reject(data);
            } else {
              resolve(data);
            }
          } else {
            wx.showToast({
              title: `上传失败(${res.statusCode})`,
              icon: 'none'
            });
            reject(res);
          }
        } catch (e) {
          wx.showToast({
            title: '解析响应失败',
            icon: 'none'
          });
          reject(e);
        }
      },
      fail: (err) => {
        console.error(`[Upload Fail] ${url}:`, err);
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
        reject(err);
      }
    });

    // 返回上传任务，可以用于监听进度
    return uploadTask;
  });
};

module.exports = {
  request,
  uploadFile,
  getFullImageUrl
};