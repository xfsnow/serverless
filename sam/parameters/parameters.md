## 操作步骤

把下面命令中的 ```<bucket-name>``` 等变量值替换成你的具体的值。

在当前目录下执行以下命令，把文件上传到S3并打包成可用于 CloudFormation 的模板文件。
```
aws cloudformation package \
    --template-file parameters.yaml \
    --s3-bucket <bucket-name> \
    --output-template-file output_parameters.yaml
```

确认已经生成 output_parameters.yaml 文件，可以打开它查看一下。
```
cat output_parameters.yaml
```

执行以下命令。--parameter-overrides MyEnvironment=prod 表示部署时为 CloudFormation 的模板参数指定值为 prod。
```
aws cloudformation deploy \
   --template-file output_parameters.yaml \
   --stack-name parameters \
   --capabilities CAPABILITY_IAM \
   --parameter-overrides MyEnvironment=prod
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
{"bucketName":"prod"}
```
表示已经部署成功。

再执行一次 aws cloudformation deploy，把 MyEnvironment 参数变成 testing
```
aws cloudformation deploy \
   --template-file output_parameters.yaml \
   --stack-name parameters \
   --capabilities CAPABILITY_IAM \
   --parameter-overrides MyEnvironment=testing
```
等待执行完毕后，刷新刚才的调用 URL，可以看到内容已经更新成了
```
{"bucketName":"testing"}
````
