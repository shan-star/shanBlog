# shanBlog
vue+springboot Project





### 数据库

blog.sql



### 后端项目 blog-api（在blog-parent 目录下）: 

需要在application.properties 文件 手动配置数据库密码、七牛云账号和私钥

以及在WebConfig.java 配置跨域处理(配置允许访问的规则)



### 前端项目blog-app：

在配置文件config 目录下的 dev.env.js 配置BASE_API(请求后端的基本路径)

然后安装依赖[命令：npm install]，启动项目[命令：npm run dev]

打包项目命令：npm run build

