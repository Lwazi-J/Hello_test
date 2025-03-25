output "api_endpoint" {
  description = "API Gateway endpoint URL"
  value       = aws_apigatewayv2_stage.lambda_stage.invoke_url
}

output "function_name" {
  description = "Lambda function name"
  value       = aws_lambda_function.app.function_name
}

output "function_arn" {
  description = "Lambda function ARN"
  value       = aws_lambda_function.app.arn
}

output "s3_bucket" {
  description = "S3 bucket for Lambda artifacts"
  value       = aws_s3_bucket.lambda_bucket.id
}

output "zip_file_path" {
  description = "Path to the generated ZIP file"
  value       = data.archive_file.lambda_zip.output_path
}