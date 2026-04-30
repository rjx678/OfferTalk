Component({
  data: {
    selected: 0,
    list: [
      {
        pagePath: '/pages/index/index',
        text: '首页',
        icon: '🏠',
        iconActive: '🏠'
      },
      {
        pagePath: '/pages/company/list',
        text: '公司',
        icon: '🏢',
        iconActive: '🏢'
      },
      {
        pagePath: '/pages/publish/index',
        text: '发布',
        icon: '➕',
        iconActive: '➕'
      },
      {
        pagePath: '/pages/mine/index',
        text: '我的',
        icon: '👤',
        iconActive: '👤'
      }
    ]
  },

  attached() {
    setTimeout(() => {
      try {
        this.updateSelected();
      } catch (e) {
        console.error('tabBar updateSelected error:', e);
      }
    }, 100);
  },

  methods: {
    switchTab(e) {
      try {
        const dataset = e.currentTarget?.dataset;
        if (!dataset) return;
        
        const path = dataset.path;
        const index = this.data.list.findIndex(item => item.pagePath === path);

        if (index !== -1) {
          this.setData({ selected: index });
          wx.switchTab({ 
            url: path,
            fail: (err) => {
              console.error('switchTab fail:', err);
            }
          });
        }
      } catch (e) {
        console.error('tabBar switchTab error:', e);
      }
    },

    updateSelected() {
      try {
        const pages = getCurrentPages?.();
        if (!pages || pages.length === 0) return;
        
        const currentPage = pages[pages.length - 1];
        if (!currentPage || typeof currentPage !== 'object') return;
        
        const route = currentPage.route;
        if (!route || typeof route !== 'string') return;
        
        const fullRoute = '/' + route;
        const index = this.data.list.findIndex(item => item.pagePath === fullRoute);
        
        if (index !== -1 && index !== this.data.selected) {
          this.setData({ selected: index });
        }
      } catch (e) {
        console.error('tabBar updateSelected error:', e);
      }
    }
  }
});
