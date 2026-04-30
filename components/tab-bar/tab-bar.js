Component({
  properties: {
    current: {
      type: Number,
      value: 0
    }
  },

  data: {
    tabs: [
      { id: 0, label: '首页', path: '/pages/index/index' },
      { id: 1, label: '公司', path: '/pages/company/list' },
      { id: 2, label: '发布', path: '/pages/publish/index' },
      { id: 3, label: '我的', path: '/pages/mine/index' }
    ]
  },

  methods: {
    onTabTap(e) {
      const id = e.currentTarget.dataset.id;
      if (id === this.data.current) return;

      const path = this.data.tabs[id].path;

      if (id === 2) {
        wx.switchTab({ url: path });
      } else {
        wx.switchTab({ url: path });
      }
    }
  }
});