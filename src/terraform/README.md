# AWS Deployment Architecture

This Terraform configuration deploys a secure API architecture with the following components:

## Architecture Overview

```
                                  ┌─────────────┐
                                  │             │
                                  │  Route 53   │
                                  │             │
                                  └──────┬──────┘
                                         │
                                         ▼
┌─────────────┐               ┌─────────────────┐
│             │               │                 │
│     WAF     │───────────────►   CloudFront    │
│             │               │                 │
└─────────────┘               └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │                 │
                              │   API Gateway   │
                              │                 │
                              └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │                 │
                              │     Lambda      │
                              │                 │
                              └─────────────────┘
```

## Components

1. **Route 53**: DNS management for `buildingqualitysoftware.com`
2. **CloudFront**: Content delivery network with HTTPS support
3. **WAF**: Web Application Firewall for security
4. **API Gateway**: RESTful API interface
5. **Lambda**: Serverless compute for the API backend

## Deployment Instructions

1. Ensure your AWS credentials are configured
2. Run `terraform init` to initialize the Terraform configuration
3. Run `terraform plan` to preview the changes
4. Run `terraform apply` to deploy the infrastructure

## Post-Deployment Steps

1. Update your domain registrar's nameservers to point to the Route 53 nameservers (see outputs)
2. Verify the SSL certificate is validated
3. Test the API endpoint at `https://api.buildingqualitysoftware.com`

## Security Features

- HTTPS encryption with TLS 1.2
- WAF protection against common attacks
- CloudFront edge caching and DDoS protection
- Least privilege IAM policies
- API Gateway throttling