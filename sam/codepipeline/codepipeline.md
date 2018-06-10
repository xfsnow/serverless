## 简介
本例使用 github 存储库存放源码，先创建 CodeBuild 项目，再使用 CodePipeline 实现管道来自动化构建和部署。最终实现代码向 github 上 push 之后，CodePipeline 自动拉取更新、重新构建以及部署。

index.js 文件中以下这行，表示需要依赖库 time。
```javascript
var time = require('time');
```

buildspec.yml 中
```yaml
install:
    commands:
      - npm install time
```
表示在构建的安装步骤把 time 库安装进来。

## 操作步骤
总的操作流程主要是以下几步：

1. 在 github 上创建一个库存放源文件。

2. 创建一个 CodeBuild 项目，用于构建无服务器应用。

3. 创建一个 IAM 角色，用于 CloudFormation 部署无服务器应用。

4. 创建一个 CodePipeline 项目，把上述若干步骤和资源组建成管道。

### 把源文件存放在 github 上
新建一个存储库，名为 __serverlessCodepipepline__。

把 https://github.com/xfsnow/serverless 这个项目中当前例子的路径 /serverless/sam/codepipeline/ 下的文件，放在我们自已的 serverlessCodepipepline 库的根目录下。

把 buildspec.yml 中的 ```<bucket>``` 更新成自己的桶名称，再 commit 到 git 库中。

### 配置 CodeBuild 项目
打开 CodeBuild 控制台，点击 Create project。

* 在 Configure your project 页

    Project name 输入 serverlessCodepipeline

    Source provider 选择 Github

    Repository 选择 Use a repository in my account

    Repository URL 输入我们自己的库的路径，比如 ``` https://github.com/xfsnow/serverlessCodepipepline ```

* Environment image 保持选择 Use an image managed by AWS CodeBuild

    Operating system 选择 Ubuntu

    Runtime 选 Node.js

    Version 选择 aws/codebuild/nodejs:4.3.2

    Build specification 保持选中 Use the buildspec.yml in the source code root directory

    Artifacts type 选 Amazon S3

    Bucket name 还选择我们在 buildspec.yml 中指定的 S3 桶名称

    确认 Create a service role in your account 已选中

    Role name 输入 serverlessCodebuild，点击右下角 Continue 按钮。

* 在 Review 页点击右下方的 Save and build 按钮。
    
    创建成功后前进到 Build projects 列表页，此时刚刚新建的项目应该是选中的状态。点击左上角 Start build 按钮。
    
    在 Start new build 页，直接点击右下角 Start build 按钮。

* 在 Build 页可以查看构建进行的进度信息。注意看 Phase details 下面的输出内容。

    构建成功完成以后，可以到我们的 S3 桶中查看结果，可以看到创建出一个 serverlessCodepipeline 目录，里面就是构建的成果—— output-codepipeline.yaml 文件。

    我们可以把它下载到本地看一下，后续我们继续配置 CodePipeline 就是用它来做无服务器资源的部署。

### 配置 IAM 角色
登录 AWS 管理控制台，并通过以下网址打开 IAM 控制台 https://console.aws.amazon.com/iam/。
点击左侧导航链接中的 Roles，点击 Create new role 按钮。

* 在 Select role type 页选择 AWS Service Role，找到  AWS Cloudformation Role 点击其右边的 Select 按钮。
* 在 Attach Policy 页，选择 AWSLambdaExecute。点击 Next Step 按钮。
* 在 Set role name and review 页，
  Role name 输入 cloudformation-lambda-execution，然后点击 Create role 按钮。
* 打开刚才创建的角色，在 Permissions 选项卡下，点击 Inline Policies 展开之，然后选择 click here 链接。

    选择 Custom Policy，然后选择 Select。

    在 Policy Name 中，输入 cloudformation-deploy-lambda ，然后将以下内容中的 __region__ 和 __account_id__ 替换成你自己的值，粘贴到 Policy Document 字段中：  
```
{
    "Statement": [
        {
            "Action": [
                "s3:GetObject",
                "s3:GetObjectVersion",
                "s3:GetBucketVersioning"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "s3:PutObject"
            ],
            "Resource": [
                "arn:aws:s3:::codepipeline*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "lambda:*"
            ],
            "Resource": [
                "arn:aws:lambda:region:account-id:function:*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "apigateway:*"
            ],
            "Resource": [
                "arn:aws:apigateway:region::*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "iam:GetRole",
                "iam:CreateRole",
                "iam:DeleteRole"
            ],
            "Resource": [
                "arn:aws:iam::account-id:role/*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "iam:AttachRolePolicy",
                "iam:DetachRolePolicy"
            ],
            "Resource": [
                "arn:aws:iam::account-id:role/*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "iam:PassRole"
            ],
            "Resource": [
                "*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "cloudformation:CreateChangeSet"
            ],
            "Resource": [
                "arn:aws:cloudformation:region:aws:transform/Serverless-2016-10-31"
            ],
            "Effect": "Allow"
        }
    ],
    "Version": "2012-10-17"
}    
```

点击 Validate Policy，然后点击 Apply Policy。 

### 配置 CodePipeline 项目
打开 CodePipeline 控制台，点击 Create project。

* 在 Step 1: Name 页

    Project name 输入 serverlessCodepipeline，点击“Next Step” 按钮。

* 在 Step 2: Source 页
    
    Source provider 选择 Github，然后点击 Connect to GitHub 按钮，关联 GitHub 账号。按提示完成关联操作。

    回到 AWS 页面后，Repository 选择前述我们自己创建的存储库。
    
    Branch 选择 master ，点击“Next Step” 按钮。

* 在 Step 3: Build 页
    
    Build provider 选择 AWS CodeBuild
    
    Configure your project 保持选中 Select an existing build project。
    
    Project name 在下拉列表中选择我们前面创建的 serverlessCodepipeline 项目。

* 在 Step 4: Deploy 页
    
    Deployment provider 选择 AWS CloudFormation。
    
    Action mode 选择 Create or replace a change set。
    
    Stack name 输入 serverlessCodepipeline。
    
    Change set name 输入 serverlessCodepipelineChangeSet。
    
    Template file 输入 buildspec.yml 中指定的构建结果文件名 output-codepipeline.yaml。
    
    Capabilities 选择 CAPABILITY_IAM。
    
    Role name 选择我们前面创建的 IAM 角色 cloudformation-lambda-execution。
点击 Next Step 按钮。

* 在 Step 5: Service Role 页 
    
    点击 Create Role 按钮，在弹出的 IAM AWS CodePipeline is requesting permission to use resources in your account  页面，直接点击右下角 Allow 按钮，返回后点击 Next Step 按钮。

* 在 Step 6: Review 页面，直接点击右下角
    点击右下角的 Create Pipeline 按钮。最后来到 serverlessCodepipeline 项目详情页。

* 增加测试部署阶段
    在 serverlessCodepipeline 详情页点击 Edit 按钮。

    在 Staging 阶段下面点击 +Stage 链接。

    在 Stage name 栏输入 Beta，然后点击其下面的 +Action 按钮。

    在 Action category 中，选择Deploy。

    在 Action name 中，输入 executeChangeSet。

    在 Deployment provider 中，选择 AWS CloudFormation。

    在 Action mode: 中，选择 execute a changeset。前一步我们已经创建了 ChangeSet，将 SAM 模板转换为完整的 AWS CloudFormation 格式，现在我们要使用 deployChangeSet 部署 AWS CloudFormation 模板了。

    在 Stack name：中，输入 serverlessCodepipeline。

    在 Change set name：中，输入 serverlessCodepipelineChangeSet。

    选择 Add Action。

    回到页面顶部点击 Save pipeline changes。

    选择 Save and continue。


## 查看结果
我们在 serverlessCodepipeline 项目详情页稍等10秒左右，Pipeline 会自动开始第一次部署。可以随时查看到各个步骤的执行情况，比如：

    Source
    GitHub
    Succeeded 2 min ago
    d24ff81

最后等到 Beta 步骤也完成，这时到 CloudFormation 的控制台查看已经创建出一个 serverlessCodepipeline ，整个过程大约持续 3到5 分钟。

然后到 API Gateway 控制台，可以看到创建出的 serverlessCodepipeline 的 API，点击其 Stages 下的 Prod 链接，可以看到形如下面的调用  URL:
Invoke URL: [https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod](https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod)

复制此 URL，打开一个新窗口，粘贴进地址栏，然后在后面再输入 /time，组成形如

[https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/time](https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/time)

的链接再访问之，显示
```
"The time in Los Angeles is Mon Aug 07 2017 03:31:39 GMT-0700 (PDT)"
```
表示已经部署成功。

然后我们模拟代码更新，把你自己的 github 存储库中的 README.md 文件编辑一下，然后 git commit 到 github 上去。 然后再回到 serverlessCodepipeline 详情页，稍等一会我们会看到从 Source 开始整个管道会再次执行一遍。

执行到每一步时，我们都可以点击 Detail 链接到相关服务的详情页查看具体进度。比如 CloudFormation 会创建出一个新的 serverlessCodepipelineChangeSet 来执行变更。

最后到 API Gateway 的 serverlessCodepipeline API，选择一个 Stage ，再点选    Deployment History 可以看到 Deployment date 更新了时间。
