## 简介
本例在构建时安装 node.js 依赖库 time，加入到 Lambda 函数的包中。

index.js 文件中以下这行，表示需要依赖库 time。
```
var time = require('time');
```

buildspec.yml 中
```
install:
    commands:
      - npm install time
```
表示在构建的安装步骤把 time 库安装进来。


## 操作步骤
### 上传源文件
在当前目录下除了 md 文件的其它文件打包成 codebuild.zip，然后把这个 zip 文件上传我们的 S3 的桶中。

### 配置 CodeBuild 项目
打开 CodeBuild 控制台

点击 Create project。

在 Configure your project 页

Project name 输入 serverlessCodebuild

Source provider 选择 Amazon S3

Bucket 栏选择我们的刚才上传 zip 文件的 S3 桶名称

S3 object key 输入 codebuild.zip。

Environment image 保持选择 Use an image managed by AWS CodeBuild

Operating system 选择 Ubuntu

Runtime 选 Node.js

Version 选择 aws/codebuild/nodejs:4.3.2

Artifacts type 选 Amazon S3

Bucket name 还选择我们的刚才上传 zip 文件的 S3 桶名称

确认 Create a service role in your account 已选中

Role name 输入 serverlessCodebuild

点击右下角 Continue 按钮

在 Review 页点击右下方的 Save and build 按钮。

创建成功后前进到 Build projects 列表页，此时刚刚新建的项目应该是选中的状态。点击左上角 Start build 按钮。

在 Start new build 页，直接点击右下角 Start build 按钮。

在 Build 页可以查看构建进行的进度信息。注意看 Phase details 下面的输出内容。
构建成功完成以后，可以到我们的 S3 桶中查看结果，可以看到创建出一个 serverlessCodebuild 目录，里面就是构建的成果—— output_codebuild.yaml 文件。我们把它下载到本地，就可以用它再执行 CloudFormation 部署。

### 使用 CloudFormation 部署
执行以下命令
```
aws cloudformation deploy \
   --template-file output_codebuild.yaml \
   --stack-name serverlessCodebuild \
   --capabilities CAPABILITY_IAM
```

顺利地话，会看到逐渐输出的返回结果
```
Waiting for changeset to be created..
Waiting for stack create/update to complete
Successfully created/updated stack - serverlessCodebuild
```

这时到 CloudFormation 的控制台已经创建出一个 serverlessCodebuild ，整个过程大约持续 1 到 2 分钟。

然后到 API Gateway 控制台，可以看到创建出的 serverlessCodebuild 的 API，点击其 Stages 下的 Prod 链接，可以看到形如下面的调用 URL:
Invoke URL: [https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod](https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod)

点击它，打开一个新窗口，显示
```
"The time in Los Angeles is Mon Aug 07 2017 03:32:42 GMT-0700 (PDT)"
```
表示已经部署成功。
