resource "aws_apigatewayv2_authorizer" "api_authorizer" {
  api_id           = aws_apigatewayv2_api.greeting_api.id
  authorizer_type  = "JWT"
  identity_sources = ["$request.header.Authorization"]
  name             = "jwt-authorizer"

  jwt_configuration {
    audience = ["api.buildingqualitysoftware.com"]
    issuer   = "https://cognito-idp.${var.region}.amazonaws.com/${aws_cognito_user_pool.api_users.id}"
  }
}

resource "aws_cognito_user_pool" "api_users" {
  name = "api-users-${var.environment}"
  
  password_policy {
    minimum_length    = 12
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
    require_uppercase = true
  }
  
  mfa_configuration = "OPTIONAL"
  
  account_recovery_setting {
    recovery_mechanism {
      name     = "verified_email"
      priority = 1
    }
  }
  
  admin_create_user_config {
    allow_admin_create_user_only = true
  }
}

resource "aws_cognito_user_pool_client" "api_client" {
  name                         = "api-client-${var.environment}"
  user_pool_id                 = aws_cognito_user_pool.api_users.id
  generate_secret              = true
  refresh_token_validity       = 30
  prevent_user_existence_errors = "ENABLED"
  explicit_auth_flows = [
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_PASSWORD_AUTH"
  ]
  
  allowed_oauth_flows = ["client_credentials"]
  allowed_oauth_flows_user_pool_client = true
  allowed_oauth_scopes = ["api.buildingqualitysoftware.com/api.read", "api.buildingqualitysoftware.com/api.write"]
  
  token_validity_units {
    access_token  = "minutes"
    id_token      = "minutes"
    refresh_token = "days"
  }
  
  access_token_validity  = 60
  id_token_validity      = 60
}