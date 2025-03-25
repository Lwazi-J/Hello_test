output "lambda_function_name" {
  description = "Name of the Lambda function"
  value       = aws_lambda_function.genAI_lambda.function_name
}

output "lambda_function_arn" {
  description = "ARN of the Lambda function"
  value       = aws_lambda_function.genAI_lambda.arn
}

output "lambda_invoke_arn" {
  description = "Invoke ARN of the Lambda function"
  value       = aws_lambda_function.genAI_lambda.invoke_arn
}

output "lambda_role_arn" {
  description = "ARN of the IAM role used by the Lambda function"
  value       = aws_iam_role.lambda_role.arn
}

output "s3_bucket_name" {
  description = "Name of the S3 bucket where Lambda code is stored"
  value       = aws_s3_bucket.lambda_bucket.id
}

output "s3_bucket_arn" {
  description = "ARN of the S3 bucket"
  value       = aws_s3_bucket.lambda_bucket.arn
}

output "api_gateway_id" {
  description = "ID of the API Gateway REST API"
  value       = aws_api_gateway_rest_api.genai_api.id
}

output "api_gateway_root_resource_id" {
  description = "Resource ID of the API Gateway's root resource"
  value       = aws_api_gateway_rest_api.genai_api.root_resource_id
}

output "api_gateway_stage_name" {
  description = "Name of the API Gateway stage"
  value       = aws_api_gateway_stage.genai_stage.stage_name
}

output "api_gateway_invoke_url" {
  description = "URL to invoke the API Gateway"
  value       = "${aws_api_gateway_deployment.genai_deployment.invoke_url}${aws_api_gateway_stage.genai_stage.stage_name}/genai"
}

output "api_gateway_execution_arn" {
  description = "Execution ARN of the API Gateway"
  value       = aws_api_gateway_rest_api.genai_api.execution_arn
}

output "lambda_s3_object_key" {
  description = "S3 key of the Lambda code"
  value       = aws_s3_object.lambda_code.key
}

output "random_suffix" {
  description = "Random suffix generated for unique resource names"
  value       = random_string.suffix.result
}