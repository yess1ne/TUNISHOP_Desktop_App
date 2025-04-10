package midlleware;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.prefs.Preferences;

public class TokenManager {
    private static final String TOKEN_KEY = "jwt_token";
    private static final String KEY_STORAGE = "jwt_secret_key";

    public static String generateJwtToken(int id, String email, String role) {
        long expirationTime = System.currentTimeMillis() + 3600000;  // 1 hour expiration

        // Generate a secure key
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String token = Jwts.builder()
                .setSubject("user")
                .claim("id", id)
                .claim("email", email)
                .claim("role", role)
                .setExpiration(new java.util.Date(expirationTime))
                .signWith(secretKey)
                .compact();

        // Encode and store key + token
        String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        storeTokenAndKey(token, base64EncodedKey);

        return token;
    }


    public static void saveToken(String token) {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.put(TOKEN_KEY, token);
    }

    public static String getToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get(TOKEN_KEY, null);
    }

    public static boolean hasToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get(TOKEN_KEY, null) != null;
    }

    public static void clearToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.remove(TOKEN_KEY);
        prefs.remove(KEY_STORAGE);
    }

    public static boolean verifToken() {
        return getToken() != null;
    }

    public static boolean verifIfAdmin() {
        String token = getToken();
        if (token == null) return false;

        try {
            byte[] decodedKey = Base64.getDecoder().decode(getStoredKey());
            Key secretKey = Keys.hmacShaKeyFor(decodedKey);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            Object isAdminObj = body.get("isAdmin");

            if (isAdminObj instanceof Integer) {
                return (Integer) isAdminObj != 0;
            } else if (isAdminObj instanceof Boolean) {
                return (Boolean) isAdminObj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int decodeId() {
        String token = getToken();
        if (token == null) return -1;

        try {
            byte[] decodedKey = Base64.getDecoder().decode(getStoredKey());
            Key secretKey = Keys.hmacShaKeyFor(decodedKey);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Object idObj = claimsJws.getBody().get("id");
            if (idObj != null) {
                return (int) idObj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static void storeTokenAndKey(String token, String key) {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        prefs.put(TOKEN_KEY, token);
        prefs.put(KEY_STORAGE, key);
    }

    private static String getStoredKey() {
        Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
        return prefs.get(KEY_STORAGE, null);
    }

    public static String getRoleFromToken() {
        String token = getToken();
        if (token != null) {
            try {
                byte[] decodedKey = Base64.getDecoder().decode(getStoredKey());
                Key secretKey = Keys.hmacShaKeyFor(decodedKey);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);

                return claimsJws.getBody().get("role", String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
