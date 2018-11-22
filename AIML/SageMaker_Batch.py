import json
import boto3
import os
from time import gmtime, strftime

def lambda_handler(event, context):
    bucket = event['Records'][0]['s3']['bucket']['name']
    folder = os.path.dirname(event['Records'][0]['s3']['object']['key'])
    obj = event['Records'][0]['s3']['object']['key']
    print("obj: ", obj)
    if ".manifest" in obj:
        # 批量转换任务名
        batch_job_name = 'Batch-Transform-' + strftime("%Y-%m-%d-%H-%M-%S", gmtime())
        # 输入文件的完整路径，比如 s3://sagemaker/batch/20181122.manifest
        input_location = 's3://{}/{}'.format(bucket, obj)
        print("input_location: ", input_location)
        # 输出文件的路径，比如 s3://sagemaker/batch/Batch-Transform-2018-11-22-07-25-03
        output_location = 's3://{}/{}/{}'.format(bucket, folder, batch_job_name)
        # 在 sagemaker 中训练好的模型名称
        model_name = 'kmeans-2018-11-21-12-23-04-321'

        ### Create a transform job
        sm = boto3.client('sagemaker')

        request = \
        {
            "TransformJobName": batch_job_name,
            "ModelName": model_name,
            "MaxConcurrentTransforms": 4,
            "MaxPayloadInMB": 6,
            "BatchStrategy": "MultiRecord",
            "TransformOutput": {
                "S3OutputPath": output_location
            },
            "TransformInput": {
                "DataSource": {
                    "S3DataSource": {
                        "S3DataType": "ManifestFile",
                        "S3Uri": input_location
                    }
                },
                "ContentType": "application/x-recordio-protobuf",
                "SplitType": "RecordIO",
                "CompressionType": "None"
            },
            "TransformResources": {
                    "InstanceType": "ml.m4.xlarge",
                    "InstanceCount": 1
            }
        }
        sm.create_transform_job(**request)

        print("Created Transform job with name: ", batch_job_name)
    else:
        print("Not a job.")
    return {
        'statusCode': 200,
        'body': json.dumps('Done.')
    }
