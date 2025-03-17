variable "AWS_ACCESS_KEY_ID" {}
variable "AWS_SECRET_ACCESS_KEY" {}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "spring-lambda-app"
}

variable "jar_file" {
  description = "Path to the JAR file"
  type        = string
  default     = "C:/Users/user/Documents/Hello_test/target/hello-api-test-0.0.1-SNAPSHOT.jar"
}

variable "lambda_handler" {
  description = "Lambda handler path"
  type        = string
  default     = "com.example.Handler::handleRequest"
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