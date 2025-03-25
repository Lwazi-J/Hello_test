provider "aws" {
  region = var.aws_region
}

# Create S3 bucket for Lambda code
resource "aws_s3_bucket" "lambda_bucket" {
  bucket = "genai-lambda-buck-lwazi"
}

# Create random string for unique bucket name
resource "random_string" "suffix" {
  length  = 8
  special = false
  upper   = false
}

# Upload JAR file to S3
resource "aws_s3_object" "lambda_code" {
  bucket = aws_s3_bucket.lambda_bucket.id
  key    = "code/lambda_function.jar"
  source = "source = data.archive_file.lambda_zip.output_path"
}

resource "aws_lambda_function" "genAI_lambda" {
  function_name = "genAI_lambda"
  role          = aws_iam_role.lambda_role.arn
  handler       = "com.example.Handler::handleRequest"
  runtime       = "java17"
  memory_size   = 512
  timeout       = 30

  s3_bucket = aws_s3_bucket.lambda_bucket.id
  s3_key    = aws_s3_object.lambda_code.key

  depends_on = [aws_s3_bucket.lambda_bucket]
}

resource "aws_iam_role" "lambda_role" {
  name = "genai_lambda_role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

# Add necessary permissions for Lambda to access S3
resource "aws_iam_role_policy_attachment" "lambda_s3" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Add S3 read permissions to Lambda role
resource "aws_iam_role_policy" "lambda_s3_policy" {
  name = "lambda_s3_policy"
  role = aws_iam_role.lambda_role.id

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject"
      ],
      "Resource": [
        "${aws_s3_bucket.lambda_bucket.arn}/*"
      ]
    }
  ]
}
EOF
}

# Create API Gateway
resource "aws_api_gateway_rest_api" "genai_api" {
  name        = "genai-api"
  description = "API Gateway for GenAI Lambda"
}

# Create API Gateway resource
resource "aws_api_gateway_resource" "genai_resource" {
  rest_api_id = aws_api_gateway_rest_api.genai_api.id
  parent_id   = aws_api_gateway_rest_api.genai_api.root_resource_id
  path_part   = "genai"
}

# Create API Gateway method
resource "aws_api_gateway_method" "genai_method" {
  rest_api_id   = aws_api_gateway_rest_api.genai_api.id
  resource_id   = aws_api_gateway_resource.genai_resource.id
  http_method   = "POST"
  authorization = "NONE"
}

# Create API Gateway integration with Lambda
resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id = aws_api_gateway_rest_api.genai_api.id
  resource_id = aws_api_gateway_resource.genai_resource.id
  http_method = aws_api_gateway_method.genai_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.genAI_lambda.invoke_arn
}

# Add permission for API Gateway to invoke Lambda
resource "aws_lambda_permission" "api_gateway" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.genAI_lambda.function_name
  principal     = "apigateway.amazonaws.com"

  # Allow invocation from any stage of the API
  source_arn = "${aws_api_gateway_rest_api.genai_api.execution_arn}/*/*"
}

# Create API Gateway deployment
resource "aws_api_gateway_deployment" "genai_deployment" {
  rest_api_id = aws_api_gateway_rest_api.genai_api.id

  depends_on = [
    aws_api_gateway_integration.lambda_integration
  ]
}

# Create API Gateway stage
resource "aws_api_gateway_stage" "genai_stage" {
  deployment_id = aws_api_gateway_deployment.genai_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.genai_api.id
  stage_name    = "dev"
}

