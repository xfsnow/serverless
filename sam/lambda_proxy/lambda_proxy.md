## 操作步骤

把下面命令中的 <bucket-name> 等变量值替换成你的具体的值。

在当前目录下执行以下命令，把文件上传到S3并打包成可用于 CloudFormation 的模板文件。
```
aws cloudformation package \
    --template-file lambda_proxy.yaml \
    --s3-bucket <bucket-name> \
    --output-template-file packaged_lambda_proxy.yaml
```

确认已经生成 packaged_lambda_proxy.yaml 文件，可以打开它查看一下。
```
cat packaged_lambda_proxy.yaml
```

执行以下命令
```
aws cloudformation deploy \
   --template-file packaged_lambda_proxy.yaml \
   --stack-name lambdaProxy \
   --capabilities CAPABILITY_IAM
```

顺利地话，会看到逐渐输出的返回结果
```
Waiting for changeset to be created..
Waiting for stack create/update to complete
Successfully created/updated stack - lambdaProxy
```

这时到 CloudFormation 的控制台已经创建出一个 lambdaProxy，整个过程大约持续 1 到 2 分钟。
然后到 API Gateway 控制台，可以看到创建出的 lambdaProxy 的 API，点击其 Stages 下的 Prod 链接，可以看到形如下面的调用  URL:
Invoke URL: [https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod](https://xxxxxxxxx.execute-api.us-west-2.amazonaws.com/Prod)

点击它，打开一个新窗口，显示
```
{"bucketName":"bucket"}
```
表示已经部署成功。
