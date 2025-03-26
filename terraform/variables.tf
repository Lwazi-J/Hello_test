variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "spr-lambda-pipe"
}

variable "jar_file" {
  description = "Path to the JAR file"
  type        = string
  default     = "https://spr-lambda-pipe-artifacts.s3.us-east-1.amazonaws.com/code/target/hello-api-test-0.0.1-SNAPSHOT-aws.jar"
}

variable "lambda_handler" {
  description = "Lambda handler path"
  type        = string
  default     = "com.example.demo.Handler::handleRequest"
}

variable "lambda_runtime" {
  description = "Lambda runtime"
  type        = string
  default     = "java17"
}

variable "lambda_memory" {
  description = "Lambda memory size"
  type        = number
  default     = 512
}

variable "lambda_timeout" {
  description = "Lambda timeout"
  type        = number
  default     = 30
}