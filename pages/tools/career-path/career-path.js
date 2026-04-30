const { request } = require('../../../utils/request.js');

Page({
  data: {
    positions: [],
    positionNames: [],
    currentPositionId: '',
    currentPosition: null,
    currentPositionName: '',
    targetPositionId: '',
    targetPosition: null,
    targetPositionName: '',
    planMonths: 24,
    timeOptions: [
      { label: '6个月', value: 6 },
      { label: '1年', value: 12 },
      { label: '2年', value: 24 },
      { label: '3年', value: 36 }
    ],
    careerPath: null,
    missingSkills: [],
    toImproveSkills: []
  },

  onLoad() {
    this.loadPositions();
  },

  loadPositions() {
    request('/career-path/positions', 'GET').then(res => {
      const positions = res.data;
      const names = positions.map(p => p.positionName);
      this.setData({
        positions,
        positionNames: names
      });
    }).catch(() => {
      const mockPositions = [
        { positionId: 'junior-dev', positionName: '初级开发工程师', level: '初级', requiredSkills: ['Java/Python基础', 'SQL基础', 'Git协作'] },
        { positionId: 'junior-fe', positionName: '初级前端工程师', level: '初级', requiredSkills: ['HTML/CSS/JS', 'Vue/React基础', '响应式设计'] },
        { positionId: 'mid-dev', positionName: '中级开发工程师', level: '中级', requiredSkills: ['Java/Python进阶', '分布式系统', '微服务架构'] },
        { positionId: 'mid-fe', positionName: '中级前端工程师', level: '中级', requiredSkills: ['Vue/React进阶', '工程化实践', '性能优化'] },
        { positionId: 'senior-dev', positionName: '高级开发工程师', level: '高级', requiredSkills: ['系统架构设计', '高并发处理', '技术选型'] },
        { positionId: 'tech-lead', positionName: '技术负责人', level: 'Lead', requiredSkills: ['技术战略', '团队管理', '项目管理'] },
        { positionId: 'tech-manager', positionName: '技术经理', level: '管理', requiredSkills: ['团队管理', '项目管理', '预算管理'] },
        { positionId: 'cto', positionName: 'CTO', level: '高管', requiredSkills: ['技术战略', '商业洞察', '行业视野'] }
      ];
      this.setData({
        positions: mockPositions,
        positionNames: mockPositions.map(p => p.positionName)
      });
    });
  },

  onCurrentPositionChange(e) {
    const index = parseInt(e.detail.value);
    const position = this.data.positions[index];
    this.setData({
      currentPositionId: position.positionId,
      currentPosition: position,
      currentPositionName: position.positionName
    });
  },

  onTargetPositionChange(e) {
    const index = parseInt(e.detail.value);
    const position = this.data.positions[index];
    this.setData({
      targetPositionId: position.positionId,
      targetPosition: position,
      targetPositionName: position.positionName
    });
  },

  selectTime(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({ planMonths: value });
  },

  generatePath() {
    const { currentPositionId, targetPositionId, planMonths } = this.data;
    
    if (!currentPositionId || !targetPositionId) {
      wx.showToast({ title: '请选择当前职位和目标职位', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '生成路径中...' });
    request('/career-path/generate', 'POST', {
      currentPositionId,
      targetPositionId,
      planMonths
    }).then(res => {
      wx.hideLoading();
      const data = res.data;
      const missingSkills = data.skillGap && data.skillGap.missingSkills ? data.skillGap.missingSkills : [];
      const toImproveSkills = data.skillGap && data.skillGap.toImproveSkills ? data.skillGap.toImproveSkills : [];
      this.setData({ 
        careerPath: data,
        missingSkills,
        toImproveSkills
      });
    }).catch(() => {
      wx.hideLoading();
      wx.showToast({ title: '生成失败', icon: 'none' });
    });
  }
});