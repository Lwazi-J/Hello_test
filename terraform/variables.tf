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

variable "deployment_package" {
  description = "Path to the ZIP deployment package"
  type        = string
  default     = "../lambda/lambda-package.zip"
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