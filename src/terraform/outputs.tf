output "api_gateway_url" {
  description = "URL of the API Gateway"
  value       = aws_apigatewayv2_api.greeting_api.api_endpoint
}

output "api_domain_name" {
  description = "Custom domain name for API"
  value       = "https://api.buildingqualitysoftware.com"
}

output "cloudfront_domain_name" {
  description = "CloudFront distribution domain name"
  value       = aws_cloudfront_distribution.api_distribution.domain_name
}

output "cloudfront_distribution_id" {
  description = "CloudFront distribution ID"
  value       = aws_cloudfront_distribution.api_distribution.id
}

output "nameservers" {
  description = "Nameservers for the Route 53 zone"
  value       = aws_route53_zone.main.name_servers
}