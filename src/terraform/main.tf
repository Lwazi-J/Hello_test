provider "aws" {
  region = var.region
}

# CloudFront and ACM certificates must be in us-east-1
provider "aws" {
  alias  = "us-east-1"
  region = "us-east-1"
}

# IAM Role for Lambda with least privilege
resource "aws_iam_role" "lambda_role" {
  name = "greeting_lambda_role_${var.environment}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })
}

# Custom policy with least privilege instead of using the basic execution role
resource "aws_iam_policy" "lambda_policy" {
  name        = "greeting_lambda_policy_${var.environment}"
  description = "Policy for greeting lambda with least privilege"
  
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = [
          "arn:aws:logs:${var.region}:*:log-group:/aws/lambda/greeting_lambda_${var.environment}:*"
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_logs_policy" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_policy.lambda_policy.arn
}

# Lambda Function with security configurations
resource "aws_lambda_function" "greeting_lambda" {
  function_name = "greeting_lambda_${var.environment}"
  handler       = "com.example.GreetingHandler::handleRequest"
  runtime       = "java17"
  role          = aws_iam_role.lambda_role.arn
  filename      = "${path.module}/../target/greeting-api-1.0-SNAPSHOT.jar"
  source_code_hash = filebase64sha256("${path.module}/../target/hello-api-test-0.0.1-SNAPSHOT.jar")
  timeout       = 30
  memory_size   = 512
  
  environment {
    variables = {
      SPRING_PROFILES_ACTIVE = var.environment
      LOG_LEVEL              = var.environment == "prod" ? "INFO" : "DEBUG"
    }
  }
  
  # Enable tracing for better monitoring and debugging
  tracing_config {
    mode = "Active"
  }
  
  # Encrypt environment variables
  kms_key_arn = var.kms_key_arn
}

# API Gateway with security configurations
resource "aws_apigatewayv2_api" "greeting_api" {
  name          = "greeting_api_${var.environment}"
  protocol_type = "HTTP"
  body          = file("${path.module}/../../main/resources/hello-test.yaml")
  
  cors_configuration {
    allow_origins = ["https://api.buildingqualitysoftware.com", "https://buildingqualitysoftware.com"]
    allow_methods = ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
    allow_headers = ["Content-Type", "Authorization"]
    max_age       = 300
  }
}

# API Gateway stage with logging and throttling
resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.greeting_api.id
  name        = "$default"
  auto_deploy = true
  
  default_route_settings {
    throttling_burst_limit = 100
    throttling_rate_limit  = 50
  }
  
  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_logs.arn
    format = jsonencode({
      requestId      = "$context.requestId"
      ip             = "$context.identity.sourceIp"
      requestTime    = "$context.requestTime"
      httpMethod     = "$context.httpMethod"
      routeKey       = "$context.routeKey"
      status         = "$context.status"
      protocol       = "$context.protocol"
      responseLength = "$context.responseLength"
      userAgent      = "$context.identity.userAgent"
    })
  }
}

# CloudWatch Log Group for API Gateway
resource "aws_cloudwatch_log_group" "api_logs" {
  name              = "/aws/apigateway/${aws_apigatewayv2_api.greeting_api.name}"
  retention_in_days = 30
  
  # Encrypt logs
  kms_key_id = var.kms_key_arn
}

resource "aws_lambda_permission" "apigw_lambda" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.greeting_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.greeting_api.execution_arn}/*/*"
}