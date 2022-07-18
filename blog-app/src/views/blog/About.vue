<template>
  <div id="about" class="me-view-body" v-title :data-title="title">
    <el-container class="me-view-container">
      <el-main>
        <div class="me-view-card">
          <h1 class="me-view-title">{{article.title}}</h1>

          <div class="me-view-content">
            <markdown-editor :editor=article.editor></markdown-editor>
          </div>

        </div>
      </el-main>

    </el-container>
  </div>
</template>

<script>
import MarkdownEditor from '@/components/markdown/MarkdownEditor'
import {viewAbout} from '@/api/article'


export default {
  name: 'About',
  created() {
    this.getAbout()
  },
  data() {
    return {
      article: {
        id: '',
        title: '',
        editor: {
          value: '',
          toolbarsFlag: false,
          subfield: false,
          defaultOpen: 'preview'
        }
      }
    }
  },
  computed: {
    title() {
      return `${this.article.title} - 关于我`
    }
  },
  methods: {
    getAbout() {
      let that = this
      viewAbout().then(data => {
        Object.assign(that.article, data.data)
        that.article.editor.value = data.data.body.content
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '文章加载失败', showClose: true})
        }
      })
    },
  },
  components: {
    'markdown-editor': MarkdownEditor,
  },
}
</script>

<style>
.me-view-body {
  margin: 100px auto 140px;
}

.me-view-container {
  width: 800px;
}

.el-main {
  overflow: hidden;
}

.me-view-title {
  font-size: 34px;
  font-weight: 800;
  line-height: 1.3;
}

.me-view-meta {
  font-size: 12px;
  color: #969696;
}

.v-show-content {
  padding: 8px 25px 15px 30px !important;
}

.v-note-wrapper .v-note-panel {
  box-shadow: none !important;
}

.v-note-wrapper .v-note-panel .v-note-show .v-show-content, .v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  background: #fff !important;
}


</style>
