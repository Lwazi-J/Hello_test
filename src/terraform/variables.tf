variable "region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Deployment environment (dev, uat, prod)"
  type        = string
  default     = "dev"
  
  validation {
    condition     = contains(["dev", "uat", "prod"], var.environment)
    error_message = "Environment must be one of: dev, uat, prod"
  }
}

variable "kms_key_arn" {
  description = "ARN of KMS key for encryption"
  type        = string
  default     = null
}

variable "allowed_origin" {
  description = "Allowed origin for CORS"
  type        = string
  default     = "*"
}

variable "enable_waf" {
  description = "Enable WAF protection"
  type        = bool
  default     = true
}