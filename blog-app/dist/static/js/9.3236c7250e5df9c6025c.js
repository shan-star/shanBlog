webpackJsonp([9],{aE3A:function(e,t){},tgdm:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=r("woOf"),a=r.n(o),s=r("33ZO"),i=r("xVlr"),l=r("viA7"),n=r("s8Ph"),c=r("iNxE"),m={name:"BlogWrite",mounted:function(){this.$route.params.id&&this.getArticleById(this.$route.params.id),this.getCategorysAndTags(),this.editorToolBarToFixedWrapper=this.$_.throttle(this.editorToolBarToFixed,200),window.addEventListener("scroll",this.editorToolBarToFixedWrapper,!1)},beforeDestroy:function(){window.removeEventListener("scroll",this.editorToolBarToFixedWrapper,!1)},data:function(){return{publishVisible:!1,categorys:[],tags:[],articleForm:{id:"",title:"",summary:"",category:"",tags:[],editor:{value:"",ref:"",default_open:"edit",toolbars:{bold:!0,italic:!0,header:!0,underline:!0,strikethrough:!0,mark:!0,superscript:!0,subscript:!0,quote:!0,ol:!0,ul:!0,imagelink:!0,code:!0,fullscreen:!0,readmodel:!0,help:!0,undo:!0,redo:!0,trash:!0,navigation:!0,preview:!0}}},rules:{summary:[{required:!0,message:"请输入摘要",trigger:"blur"},{max:80,message:"不能大于80个字符",trigger:"blur"}],category:[{required:!0,message:"请选择文章分类",trigger:"change"}],tags:[{type:"array",required:!0,message:"请选择标签",trigger:"change"}]}}},computed:{title:function(){return"写文章 - 一乐博客"}},methods:{getArticleById:function(e){var t=this,r=this;Object(l.a)(e).then(function(e){a()(r.articleForm,e.data),r.articleForm.editor.value=e.data.body.content;var o=t.articleForm.tags.map(function(e){return e.id});t.articleForm.tags=o}).catch(function(e){"error"!==e&&r.$message({type:"error",message:"文章加载失败",showClose:!0})})},publishShow:function(){this.articleForm.title?this.articleForm.title.length>30?this.$message({message:"标题不能大于30个字符",type:"warning",showClose:!0}):this.articleForm.editor.ref.d_render?this.publishVisible=!0:this.$message({message:"内容不能为空哦",type:"warning",showClose:!0}):this.$message({message:"标题不能为空哦",type:"warning",showClose:!0})},publish:function(e){var t=this,r=this;this.$refs[e].validate(function(e){if(!e)return!1;var o=t.articleForm.tags.map(function(e){return{id:e}}),a={id:t.articleForm.id,title:t.articleForm.title,summary:t.articleForm.summary,category:t.articleForm.category,tags:o,body:{content:t.articleForm.editor.value,contentHtml:t.articleForm.editor.ref.d_render}};t.publishVisible=!1;var s=t.$loading({lock:!0,text:"发布中，请稍后..."});Object(l.h)(a,t.$store.state.token).then(function(e){e.success?(s.close(),r.$message({message:"发布成功啦",type:"success",showClose:!0}),r.$router.push({path:"/view/"+e.data.id})):r.$message({message:error,type:"发布文章失败:"+e.msg,showClose:!0})}).catch(function(e){s.close(),"error"!==e&&r.$message({message:e,type:"error",showClose:!0})})})},cancel:function(){var e=this;this.$confirm("文章将不会保存, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$router.push("/")})},getCategorysAndTags:function(){var e=this;Object(n.a)().then(function(t){t.success?e.categorys=t.data:e.$message({type:"error",message:"文章分类加载失败",showClose:!0})}).catch(function(t){"error"!==t&&e.$message({type:"error",message:"文章分类加载失败",showClose:!0})}),Object(c.a)().then(function(t){t.success?e.tags=t.data:e.$message({type:"error",message:"标签加载失败",showClose:!0})}).catch(function(t){"error"!==t&&e.$message({type:"error",message:"标签加载失败",showClose:!0})})},editorToolBarToFixed:function(){var e=document.querySelector(".v-note-op");(document.documentElement.scrollTop||document.body.scrollTop)>=160?(document.getElementById("placeholder").style.display="block",e.classList.add("me-write-toolbar-fixed")):(document.getElementById("placeholder").style.display="none",e.classList.remove("me-write-toolbar-fixed"))}},components:{"base-header":s.a,"markdown-editor":i.a},beforeRouteEnter:function(e,t,r){window.document.body.style.backgroundColor="#fff",r()},beforeRouteLeave:function(e,t,r){window.document.body.style.backgroundColor="#f5f5f5",r()}},u={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{directives:[{name:"title",rawName:"v-title"}],attrs:{id:"write","data-title":e.title}},[r("el-container",[r("base-header",{attrs:{simple:!0}},[r("el-col",{attrs:{span:4,offset:2}},[r("div",{staticClass:"me-write-info"},[e._v("写文章")])]),e._v(" "),r("el-col",{attrs:{span:4,offset:6}},[r("div",{staticClass:"me-write-btn"},[r("el-button",{attrs:{round:""},on:{click:e.publishShow}},[e._v("发布")]),e._v(" "),r("el-button",{attrs:{round:""},on:{click:e.cancel}},[e._v("取消")])],1)])],1),e._v(" "),r("el-container",{staticClass:"me-area me-write-box"},[r("el-main",{staticClass:"me-write-main"},[r("div",{staticClass:"me-write-title"},[r("el-input",{staticClass:"me-write-input",attrs:{resize:"none",type:"textarea",autosize:"",placeholder:"请输入标题"},model:{value:e.articleForm.title,callback:function(t){e.$set(e.articleForm,"title",t)},expression:"articleForm.title"}})],1),e._v(" "),r("div",{staticStyle:{visibility:"hidden",height:"89px",display:"none"},attrs:{id:"placeholder"}}),e._v(" "),r("markdown-editor",{staticClass:"me-write-editor",attrs:{editor:e.articleForm.editor}})],1)],1),e._v(" "),r("el-dialog",{attrs:{title:"摘要 分类 标签",visible:e.publishVisible,"close-on-click-modal":!1,"custom-class":"me-dialog"},on:{"update:visible":function(t){e.publishVisible=t}}},[r("el-form",{ref:"articleForm",attrs:{model:e.articleForm,rules:e.rules}},[r("el-form-item",{attrs:{prop:"summary"}},[r("el-input",{attrs:{type:"textarea",rows:6,placeholder:"请输入摘要"},model:{value:e.articleForm.summary,callback:function(t){e.$set(e.articleForm,"summary",t)},expression:"articleForm.summary"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"文章分类",prop:"category"}},[r("el-select",{attrs:{"value-key":"id",placeholder:"请选择文章分类"},model:{value:e.articleForm.category,callback:function(t){e.$set(e.articleForm,"category",t)},expression:"articleForm.category"}},e._l(e.categorys,function(e){return r("el-option",{key:e.id,attrs:{label:e.categoryName,value:e}})}),1)],1),e._v(" "),r("el-form-item",{attrs:{label:"文章标签",prop:"tags"}},[r("el-checkbox-group",{model:{value:e.articleForm.tags,callback:function(t){e.$set(e.articleForm,"tags",t)},expression:"articleForm.tags"}},e._l(e.tags,function(t){return r("el-checkbox",{key:t.id,attrs:{label:t.id,name:"tags"}},[e._v(e._s(t.tagName))])}),1)],1)],1),e._v(" "),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.publishVisible=!1}}},[e._v("取 消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.publish("articleForm")}}},[e._v("发布")])],1)],1)],1)],1)},staticRenderFns:[]};var d=r("VU/8")(m,u,!1,function(e){r("aE3A")},null,null);t.default=d.exports}});
//# sourceMappingURL=9.3236c7250e5df9c6025c.js.map