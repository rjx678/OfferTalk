// 注意：真机调试时，不能使用 localhost，必须使用开发机的局域网 IP (如 192.168.x.x)
const BASE_URL = 'http://192.168.0.100:8080/api';
const IMG_BASE_URL = 'http://192.168.0.100:8080';
// 开发环境标识（微信小程序不支持 process.env）
const isDevelopment = true;

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