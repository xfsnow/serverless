S3 bucket policy
其实这是用于原图做静态网站对外可访问的。通常做缩略图时原图都不想公开，所以不用配。
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AddPerm",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::snowpeak-share/*"
        }
    ]
}
