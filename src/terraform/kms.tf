resource "aws_kms_key" "encryption_key" {
  description             = "KMS key for encrypting sensitive data in ${var.environment}"
  deletion_window_in_days = 10
  enable_key_rotation     = true
  
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "Enable IAM User Permissions"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        }
        Action   = "kms:*"
        Resource = "*"
      }
    ]
  })
  
  tags = {
    Environment = var.environment
    Name        = "greeting-api-kms-${var.environment}"
  }
}

resource "aws_kms_alias" "encryption_key_alias" {
  name          = "alias/greeting-api-${var.environment}"
  target_key_id = aws_kms_key.encryption_key.key_id
}

# Get current AWS account ID
data "aws_caller_identity" "current" {}