# AI 和机器学习相关内容

## SageMaker_Batch.py
AWS SageMaker 训练完成模型以后，使用推理的方法之一就是批量转换任务，即把需要推理的数据批量提交给托管的计算实例进行处理。由于原数据都是存储在 S3 上的，除了手工创建批量转换任务之外，还可以方便地使用 Lambda 函数，绑定 S3 上创建对象事件，当 S3 上传了新的原数据文件，触发式的自动执行创建批量转换任务。

### 为 Lambda 函数创建一个 IAM 服务角色
比如命名为 lambda_full_sagemaker，在权限部分附加策略选择内置的 AmazonSageMakerFullAccess

### 创建 Lambda 函数
Runtime 选 Python 3.7
Execution role 选前述创建的 IAM 服务角色 lambda_full_sagemaker。
代码使用 import json.py 的内容
代码中有注释的部分请根据自己的环境，如 Sagemaker 的模型名称、s3 桶目录结构等自行调整。
调试建议先用 Lambda 开发环境中的 Amazon S3 Put 事件模板，先把单次执行 Lambda 函数调试通。最后再配置事件触发。


### S3 桶配置事件
在桶的 Properties 页选择 Events
点击 Add notification
Events 选择 All object create events
Prefix 输入用于存放推理原文件的前缀，比如 sagemaker-batch
Suffix 输入 .manifest
Send to 选刚才创建的 Lambda 函数即可。
