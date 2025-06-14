resource "aws_wafv2_web_acl" "api_waf" {
  count = var.enable_waf ? 1 : 0
  
  name        = "greeting-api-waf-${var.environment}"
  description = "WAF for Greeting API"
  scope       = "CLOUDFRONT"
  provider    = aws.us-east-1 # CloudFront requires WAF in us-east-1

  default_action {
    allow {}
  }

  # Rule to block common SQL injection patterns
  rule {
    name     = "SQLInjectionRule"
    priority = 1

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesSQLiRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "SQLInjectionRule"
      sampled_requests_enabled   = true
    }

    override_action {
      none {}
    }
  }

  # Rule to block common XSS patterns
  rule {
    name     = "XSSRule"
    priority = 2

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesCommonRuleSet"
        vendor_name = "AWS"

        rule_action_override {
          action_to_use {
            block {}
          }
          name = "CrossSiteScripting_BODY"
        }
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "XSSRule"
      sampled_requests_enabled   = true
    }

    override_action {
      none {}
    }
  }

  # Rate limiting rule
  rule {
    name     = "RateLimitRule"
    priority = 3

    statement {
      rate_based_statement {
        limit              = 1000
        aggregate_key_type = "IP"
      }
    }

    action {
      block {}
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "RateLimitRule"
      sampled_requests_enabled   = true
    }
  }

  visibility_config {
    cloudwatch_metrics_enabled = true
    metric_name                = "greeting-api-waf"
    sampled_requests_enabled   = true
  }
}

# Associate WAF with API Gateway
resource "aws_wafv2_web_acl_association" "api_waf_association" {
  count = var.enable_waf ? 1 : 0
  
  resource_arn = aws_apigatewayv2_stage.default_stage.arn
  web_acl_arn  = aws_wafv2_web_acl.api_waf[0].arn
}