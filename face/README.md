# PHP on Lambda


This is a demo framework to show Serverless App coded with Lambda Node.js spawn to PHP executable. All API Gateway environment parameters have been transformed to PHP environment variables. PHP libraries required to use AWS SDK are built into lambda package. API Gateway is configured to respond with standard HTML pages to demonstrate a classical website can be built with serverless technologies.

## Deploy
```
aws cloudformation package \
	--template-file face.yaml \
	--s3-bucket <YOUR_BUCKET> \
	--output-template-file packaged_face.yaml

```

```
aws cloudformation deploy \
	--template-file packaged_face.yaml \
	--stack-name phpOnLambda \
	--capabilities CAPABILITY_IAM
	--parameter-overrides AWSKEY=<YOUR_ACCESS_KEY> AWSSECRET=<YOUR_SECRET_KEY>
```
