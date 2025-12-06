package com.basiccode.generator.generator.csharp;

/**
 * C# JWT Authentication Generator - Phase 3
 */
public class CSharpJwtGenerator {
    
    public String generateJwtConfiguration(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.AspNetCore.Authentication.JwtBearer;\n");
        code.append("using Microsoft.IdentityModel.Tokens;\n");
        code.append("using System.Text;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Configuration\n");
        code.append("{\n");
        code.append("    public static class JwtConfiguration\n");
        code.append("    {\n");
        code.append("        public static void ConfigureJwt(this IServiceCollection services, IConfiguration configuration)\n");
        code.append("        {\n");
        code.append("            var jwtSettings = configuration.GetSection(\"JwtSettings\");\n");
        code.append("            var secretKey = jwtSettings[\"SecretKey\"];\n");
        code.append("            var key = Encoding.ASCII.GetBytes(secretKey);\n\n");
        
        code.append("            services.AddAuthentication(options =>\n");
        code.append("            {\n");
        code.append("                options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;\n");
        code.append("                options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;\n");
        code.append("            })\n");
        code.append("            .AddJwtBearer(options =>\n");
        code.append("            {\n");
        code.append("                options.TokenValidationParameters = new TokenValidationParameters\n");
        code.append("                {\n");
        code.append("                    ValidateIssuerSigningKey = true,\n");
        code.append("                    IssuerSigningKey = new SymmetricSecurityKey(key),\n");
        code.append("                    ValidateIssuer = true,\n");
        code.append("                    ValidIssuer = jwtSettings[\"Issuer\"],\n");
        code.append("                    ValidateAudience = true,\n");
        code.append("                    ValidAudience = jwtSettings[\"Audience\"],\n");
        code.append("                    ValidateLifetime = true,\n");
        code.append("                    ClockSkew = TimeSpan.Zero\n");
        code.append("                };\n");
        code.append("            });\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateJwtService(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.IdentityModel.Tokens;\n");
        code.append("using System.IdentityModel.Tokens.Jwt;\n");
        code.append("using System.Security.Claims;\n");
        code.append("using System.Text;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Services\n");
        code.append("{\n");
        code.append("    public interface IJwtService\n");
        code.append("    {\n");
        code.append("        string GenerateToken(Guid userId, string username, IList<string> roles);\n");
        code.append("        ClaimsPrincipal ValidateToken(string token);\n");
        code.append("        string GenerateRefreshToken();\n");
        code.append("    }\n\n");
        
        code.append("    public class JwtService : IJwtService\n");
        code.append("    {\n");
        code.append("        private readonly IConfiguration _configuration;\n");
        code.append("        private readonly ILogger<JwtService> _logger;\n\n");
        
        code.append("        public JwtService(IConfiguration configuration, ILogger<JwtService> logger)\n");
        code.append("        {\n");
        code.append("            _configuration = configuration;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");
        
        code.append("        public string GenerateToken(Guid userId, string username, IList<string> roles)\n");
        code.append("        {\n");
        code.append("            var jwtSettings = _configuration.GetSection(\"JwtSettings\");\n");
        code.append("            var secretKey = jwtSettings[\"SecretKey\"];\n");
        code.append("            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secretKey));\n");
        code.append("            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);\n\n");
        
        code.append("            var claims = new List<Claim>\n");
        code.append("            {\n");
        code.append("                new Claim(ClaimTypes.NameIdentifier, userId.ToString()),\n");
        code.append("                new Claim(ClaimTypes.Name, username),\n");
        code.append("                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),\n");
        code.append("                new Claim(JwtRegisteredClaimNames.Iat, DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString(), ClaimValueTypes.Integer64)\n");
        code.append("            };\n\n");
        
        code.append("            foreach (var role in roles)\n");
        code.append("            {\n");
        code.append("                claims.Add(new Claim(ClaimTypes.Role, role));\n");
        code.append("            }\n\n");
        
        code.append("            var token = new JwtSecurityToken(\n");
        code.append("                issuer: jwtSettings[\"Issuer\"],\n");
        code.append("                audience: jwtSettings[\"Audience\"],\n");
        code.append("                claims: claims,\n");
        code.append("                expires: DateTime.UtcNow.AddMinutes(double.Parse(jwtSettings[\"ExpiryMinutes\"])),\n");
        code.append("                signingCredentials: credentials\n");
        code.append("            );\n\n");
        
        code.append("            var tokenString = new JwtSecurityTokenHandler().WriteToken(token);\n");
        code.append("            _logger.LogInformation(\"JWT token generated for user {UserId}\", userId);\n");
        code.append("            return tokenString;\n");
        code.append("        }\n\n");
        
        code.append("        public ClaimsPrincipal ValidateToken(string token)\n");
        code.append("        {\n");
        code.append("            var jwtSettings = _configuration.GetSection(\"JwtSettings\");\n");
        code.append("            var secretKey = jwtSettings[\"SecretKey\"];\n");
        code.append("            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secretKey));\n\n");
        
        code.append("            var tokenHandler = new JwtSecurityTokenHandler();\n");
        code.append("            var validationParameters = new TokenValidationParameters\n");
        code.append("            {\n");
        code.append("                ValidateIssuerSigningKey = true,\n");
        code.append("                IssuerSigningKey = key,\n");
        code.append("                ValidateIssuer = true,\n");
        code.append("                ValidIssuer = jwtSettings[\"Issuer\"],\n");
        code.append("                ValidateAudience = true,\n");
        code.append("                ValidAudience = jwtSettings[\"Audience\"],\n");
        code.append("                ValidateLifetime = true,\n");
        code.append("                ClockSkew = TimeSpan.Zero\n");
        code.append("            };\n\n");
        
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var principal = tokenHandler.ValidateToken(token, validationParameters, out SecurityToken validatedToken);\n");
        code.append("                return principal;\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogWarning(\"Token validation failed: {Error}\", ex.Message);\n");
        code.append("                return null;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public string GenerateRefreshToken()\n");
        code.append("        {\n");
        code.append("            var randomBytes = new byte[32];\n");
        code.append("            using var rng = System.Security.Cryptography.RandomNumberGenerator.Create();\n");
        code.append("            rng.GetBytes(randomBytes);\n");
        code.append("            return Convert.ToBase64String(randomBytes);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateAuthController(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.AspNetCore.Authorization;\n");
        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Controllers\n");
        code.append("{\n");
        code.append("    [ApiController]\n");
        code.append("    [Route(\"api/[controller]\")]\n");
        code.append("    public class AuthController : ControllerBase\n");
        code.append("    {\n");
        code.append("        private readonly IJwtService _jwtService;\n");
        code.append("        private readonly IUserService _userService;\n");
        code.append("        private readonly ILogger<AuthController> _logger;\n\n");
        
        code.append("        public AuthController(IJwtService jwtService, IUserService userService, ILogger<AuthController> logger)\n");
        code.append("        {\n");
        code.append("            _jwtService = jwtService;\n");
        code.append("            _userService = userService;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");
        
        code.append("        [HttpPost(\"login\")]\n");
        code.append("        public async Task<ActionResult<LoginResponse>> Login([FromBody] LoginRequest request)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var user = await _userService.ValidateCredentialsAsync(request.Username, request.Password);\n");
        code.append("                if (user == null)\n");
        code.append("                {\n");
        code.append("                    return Unauthorized(new { message = \"Invalid credentials\" });\n");
        code.append("                }\n\n");
        
        code.append("                var roles = await _userService.GetUserRolesAsync(user.Id);\n");
        code.append("                var token = _jwtService.GenerateToken(user.Id, user.Username, roles);\n");
        code.append("                var refreshToken = _jwtService.GenerateRefreshToken();\n\n");
        
        code.append("                await _userService.SaveRefreshTokenAsync(user.Id, refreshToken);\n\n");
        
        code.append("                return Ok(new LoginResponse\n");
        code.append("                {\n");
        code.append("                    Token = token,\n");
        code.append("                    RefreshToken = refreshToken,\n");
        code.append("                    ExpiresAt = DateTime.UtcNow.AddMinutes(60),\n");
        code.append("                    User = new UserInfo { Id = user.Id, Username = user.Username }\n");
        code.append("                });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Login failed for user {Username}\", request.Username);\n");
        code.append("                return StatusCode(500, new { message = \"Login failed\" });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        [HttpPost(\"refresh\")]\n");
        code.append("        public async Task<ActionResult<LoginResponse>> RefreshToken([FromBody] RefreshTokenRequest request)\n");
        code.append("        {\n");
        code.append("            var isValid = await _userService.ValidateRefreshTokenAsync(request.RefreshToken);\n");
        code.append("            if (!isValid)\n");
        code.append("            {\n");
        code.append("                return Unauthorized(new { message = \"Invalid refresh token\" });\n");
        code.append("            }\n\n");
        
        code.append("            var user = await _userService.GetUserByRefreshTokenAsync(request.RefreshToken);\n");
        code.append("            var roles = await _userService.GetUserRolesAsync(user.Id);\n");
        code.append("            var newToken = _jwtService.GenerateToken(user.Id, user.Username, roles);\n");
        code.append("            var newRefreshToken = _jwtService.GenerateRefreshToken();\n\n");
        
        code.append("            await _userService.SaveRefreshTokenAsync(user.Id, newRefreshToken);\n\n");
        
        code.append("            return Ok(new LoginResponse\n");
        code.append("            {\n");
        code.append("                Token = newToken,\n");
        code.append("                RefreshToken = newRefreshToken,\n");
        code.append("                ExpiresAt = DateTime.UtcNow.AddMinutes(60)\n");
        code.append("            });\n");
        code.append("        }\n\n");
        
        code.append("        [HttpPost(\"logout\")]\n");
        code.append("        [Authorize]\n");
        code.append("        public async Task<IActionResult> Logout()\n");
        code.append("        {\n");
        code.append("            var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;\n");
        code.append("            if (Guid.TryParse(userId, out var id))\n");
        code.append("            {\n");
        code.append("                await _userService.RevokeRefreshTokenAsync(id);\n");
        code.append("            }\n");
        code.append("            return Ok(new { message = \"Logged out successfully\" });\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // DTOs
        code.append("    public class LoginRequest\n");
        code.append("    {\n");
        code.append("        [Required]\n");
        code.append("        public string Username { get; set; }\n\n");
        code.append("        [Required]\n");
        code.append("        public string Password { get; set; }\n");
        code.append("    }\n\n");
        
        code.append("    public class RefreshTokenRequest\n");
        code.append("    {\n");
        code.append("        [Required]\n");
        code.append("        public string RefreshToken { get; set; }\n");
        code.append("    }\n\n");
        
        code.append("    public class LoginResponse\n");
        code.append("    {\n");
        code.append("        public string Token { get; set; }\n");
        code.append("        public string RefreshToken { get; set; }\n");
        code.append("        public DateTime ExpiresAt { get; set; }\n");
        code.append("        public UserInfo User { get; set; }\n");
        code.append("    }\n\n");
        
        code.append("    public class UserInfo\n");
        code.append("    {\n");
        code.append("        public Guid Id { get; set; }\n");
        code.append("        public string Username { get; set; }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateJwtSettings() {
        return """
            {
              "JwtSettings": {
                "SecretKey": "your-super-secret-key-that-is-at-least-32-characters-long",
                "Issuer": "YourAppName",
                "Audience": "YourAppUsers",
                "ExpiryMinutes": 60
              }
            }
            """;
    }
    
    private String convertToNetNamespace(String javaPackage) {
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalize(parts[1]) + ".Application";
        }
        return capitalize(javaPackage.replace(".", "")) + ".Application";
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}