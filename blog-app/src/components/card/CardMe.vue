<template>
  <el-card>
    <h1 class="me-author-name">一乐</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;广东&揭阳</span>
      <span><i class="me-icon-job"></i> &nbsp;分享java后端技术</span>
    </div>
    <div class="me-author-tool">
      <span class="myself">脚踏实地&nbsp;&nbsp;有始有终&nbsp;&nbsp;实践出真知</span>
    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {

      }
    },
    methods: {
      showTool(tool) {
        this.$message({
          duration: 0,
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: '<strong>' + tool.message + '</strong>'
        });
      }
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
  .myself{
    font-size: 12px;
    font-weight: bold;
    color:rgb(65, 184, 131);
  }
</style>
