<template>
  <div id="home">
    <Head-photo v-show="isShow && ($store.state.token == null) && $route.path=='/' && focusMode"
                @mainPageClick="showMainPage"/>

    <el-container :class="{'active':focusMode}">
      <base-header :activeIndex="activeIndex" @click="showMainPage"></base-header>
      <router-view class="me-container"/>
      <base-footer v-show="footerShow"></base-footer>
    </el-container>

  </div>

</template>

<script>
import BaseFooter from '@/components/BaseFooter'
import BaseHeader from '@/views/BaseHeader'
import HeadPhoto from "./views/HeadPhoto";

export default {
  name: 'Home',
  data() {
    return {
      activeIndex: '/',
      footerShow: true,
      isShow: true,
      focusMode: true
    }
  },
  components: {
    HeadPhoto,
    'base-header': BaseHeader,
    'base-footer': BaseFooter
  },
  mounted() {
    if (this.getIsPhone()) {
      this.focusMode = false
    }
  },
  methods: {
    showMainPage() {
      this.isShow = false;
    },
    //检查是否为手机端
    getIsPhone() {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      return flag;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.activeIndex = to.path
    })
  },
  beforeRouteUpdate(to, from, next) {
    if (to.path == '/') {
      this.footerShow = true
    } else {
      this.footerShow = false
    }
    this.activeIndex = to.path
    next()
  }
}
</script>

<style>

.active {
  min-height: 100vh;
  background-image: url("https://niu.yilele.site/siye.jpg") !important;;
  background-repeat: repeat-x;
  background-attachment: fixed;
  background-size: cover;
}

.me-container {
  margin: 100px auto 140px;
}
</style>
