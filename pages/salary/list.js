Page({
  data: {
    salaries: [
      {
        id: 1,
        companyName: '腾讯',
        totalPackage: '45W',
        jobTitle: '后台开发',
        city: '深圳',
        baseSalary: '24K',
        signBonus: '3W',
        housingSubsidy: '1250/月'
      },
      {
        id: 2,
        companyName: '字节跳动',
        totalPackage: '50W',
        jobTitle: '前端开发',
        city: '北京',
        baseSalary: '26K',
        signBonus: '5W',
        housingSubsidy: '1500/月'
      }
    ]
  },

  onLoad() {
    // 实际应从后端接口加载
  }
});
