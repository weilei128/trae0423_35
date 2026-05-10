<template>
  <div class="app">
    <h1>快递驿站管理系统</h1>
    
    <div class="tab-container">
      <button 
        :class="{ active: activeTab === 'store' }" 
        @click="activeTab = 'store'"
      >
        包裹入库
      </button>
      <button 
        :class="{ active: activeTab === 'pickup' }" 
        @click="activeTab = 'pickup'"
      >
        取件管理
      </button>
      <button 
        :class="{ active: activeTab === 'report' }" 
        @click="activeTab = 'report'"
      >
        运营报表
      </button>
    </div>

    <!-- 包裹入库表单 -->
    <div v-if="activeTab === 'store'" class="form-container">
      <h2>包裹入库</h2>
      <form @submit.prevent="handleStore">
        <div class="form-group">
          <label>快递条码：</label>
          <input type="text" v-model="storeForm.barcode" required />
        </div>
        <div class="form-group">
          <label>收件人姓名：</label>
          <input type="text" v-model="storeForm.recipientName" required />
        </div>
        <div class="form-group">
          <label>收件人电话：</label>
          <input type="text" v-model="storeForm.recipientPhone" required />
        </div>
        <div class="form-group">
          <label>快递员姓名：</label>
          <input type="text" v-model="storeForm.courierName" required />
        </div>
        <div class="form-group">
          <label>快递员电话：</label>
          <input type="text" v-model="storeForm.courierPhone" required />
        </div>
        <button type="submit" class="submit-btn">提交入库</button>
      </form>
      
      <div v-if="storeResult" class="result">
        <h3>入库成功</h3>
        <p>取件码：{{ storeResult.pickupCode }}</p>
        <p>货架位：{{ storeResult.shelfPosition }}</p>
      </div>
    </div>

    <!-- 取件管理表单 -->
    <div v-if="activeTab === 'pickup'" class="form-container">
      <h2>取件管理</h2>
      <form @submit.prevent="handlePickup">
        <div class="form-group">
          <label>取件码：</label>
          <input type="text" v-model="pickupForm.pickupCode" required />
        </div>
        <button type="submit" class="submit-btn">确认取件</button>
      </form>
      
      <div v-if="pickupResult" class="result">
        <h3 v-if="pickupResult">取件成功</h3>
        <h3 v-else>取件失败，未找到包裹</h3>
      </div>
    </div>

    <!-- 运营报表 -->
    <div v-if="activeTab === 'report'" class="form-container">
      <h2>运营报表</h2>
      <div class="report-header">
        <div class="form-group">
          <label>选择日期：</label>
          <input type="date" v-model="reportDate" @change="loadReport" />
        </div>
        <button type="button" class="submit-btn" @click="loadReport">刷新数据</button>
      </div>
      
      <div v-if="loading" class="loading">加载中...</div>
      
      <div v-else-if="reportData" class="report-content">
        <div class="report-card">
          <h3>日收件量</h3>
          <p class="report-value">{{ reportData.dailyStorageCount }}</p>
        </div>
        <div class="report-card">
          <h3>日取件量</h3>
          <p class="report-value">{{ reportData.dailyPickupCount }}</p>
        </div>
        <div class="report-card">
          <h3>取件率</h3>
          <p class="report-value">{{ reportData.pickupRate.toFixed(2) }}%</p>
        </div>
        <div class="report-card">
          <h3>滞留件</h3>
          <p class="report-value">{{ reportData.overdueCount }}</p>
        </div>
        <div class="report-card">
          <h3>总包裹数</h3>
          <p class="report-value">{{ reportData.totalPackageCount }}</p>
        </div>
        <div class="report-card">
          <h3>总取件数</h3>
          <p class="report-value">{{ reportData.totalPickupCount }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeTab: 'store',
      storeForm: {
        barcode: '',
        recipientName: '',
        recipientPhone: '',
        courierName: '',
        courierPhone: ''
      },
      pickupForm: {
        pickupCode: ''
      },
      storeResult: null,
      pickupResult: null,
      reportDate: new Date().toISOString().split('T')[0],
      reportData: null,
      loading: false
    }
  },
  methods: {
    async handleStore() {
      try {
        const response = await fetch('http://localhost:10025/api/package/store', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            barcode: this.storeForm.barcode,
            recipientName: this.storeForm.recipientName,
            recipientPhone: this.storeForm.recipientPhone,
            courierName: this.storeForm.courierName,
            courierPhone: this.storeForm.courierPhone
          })
        })
        
        if (response.ok) {
          this.storeResult = await response.json()
          // 重置表单
          Object.keys(this.storeForm).forEach(key => {
            this.storeForm[key] = ''
          })
        }
      } catch (error) {
        console.error('入库失败:', error)
      }
    },
    async handlePickup() {
      try {
        const response = await fetch('http://localhost:10025/api/package/pickup', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            pickupCode: this.pickupForm.pickupCode
          })
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.code) {
            // 处理错误响应
            alert(result.message)
            this.pickupResult = null
          } else {
            // 取件成功
            this.pickupResult = result
            alert('取件成功！')
          }
          // 重置表单
          this.pickupForm.pickupCode = ''
        }
      } catch (error) {
        console.error('取件失败:', error)
        alert('取件失败，请稍后重试')
      }
    },
    async loadReport() {
      this.loading = true
      try {
        const response = await fetch(`http://localhost:10025/api/report/daily?date=${this.reportDate}`)
        if (response.ok) {
          this.reportData = await response.json()
        }
      } catch (error) {
        console.error('加载报表失败:', error)
        alert('加载报表失败，请稍后重试')
      } finally {
        this.loading = false
      }
    }
  },
  mounted() {
    // 页面加载时自动加载当天的报表数据
    this.loadReport()
  }
}
</script>

<style>
.app {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #333;
}

.tab-container {
  display: flex;
  margin: 20px 0;
  border-bottom: 1px solid #ddd;
}

.tab-container button {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 16px;
  border-bottom: 3px solid transparent;
}

.tab-container button.active {
  border-bottom-color: #4CAF50;
  color: #4CAF50;
}

.form-container {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

h2 {
  color: #333;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.submit-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.submit-btn:hover {
  background: #45a049;
}

.result {
  margin-top: 20px;
  padding: 15px;
  background: #e8f5e8;
  border-radius: 4px;
  border-left: 4px solid #4CAF50;
}

.result h3 {
  color: #2e7d32;
}

.result p {
  margin: 5px 0;
}

/* 运营报表样式 */
.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.report-header .form-group {
  flex: 1;
  margin-right: 20px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.report-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.report-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  text-align: center;
}

.report-card h3 {
  color: #333;
  margin-bottom: 10px;
  font-size: 16px;
}

.report-value {
  font-size: 24px;
  font-weight: bold;
  color: #4CAF50;
  margin: 0;
}
</style>