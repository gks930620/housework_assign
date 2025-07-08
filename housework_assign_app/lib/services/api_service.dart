// services/api_service.dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ApiService {
  static final _storage = FlutterSecureStorage();
  static const _baseUrl = 'http://10.0.2.2:8080';

  static Future<http.Response> _authorizedGet(String path) async {
    String? accessToken = await _storage.read(key: 'accessToken');
    final response = await http.get(
      Uri.parse('$_baseUrl$path'),
      headers: {'Authorization': 'Bearer $accessToken'},
    );
    if (response.statusCode == 401) {
      final ok = await _reissueToken();
      if (ok) {
        accessToken = await _storage.read(key: 'accessToken');
        return http.get(
          Uri.parse('$_baseUrl$path'),
          headers: {'Authorization': 'Bearer $accessToken'},
        );
      }
    }
    return response;
  }

  static Future<Map<String, dynamic>?> getMyInfo() async {
    final res = await _authorizedGet('/me');
    if (res.statusCode == 200) return jsonDecode(res.body);
    return null;
  }

  static Future<bool> _reissueToken() async {
    final refresh = await _storage.read(key: 'refreshToken');
    if (refresh == null) return false;
    final res = await http.post(
      Uri.parse('$_baseUrl/auth/reissue'),
      body: jsonEncode(refresh),
      headers: {'Content-Type': 'application/json'},
    );
    if (res.statusCode == 200) {
      final data = jsonDecode(res.body);
      await _storage.write(key: 'accessToken', value: data['accessToken']);
      await _storage.write(key: 'refreshToken', value: data['refreshToken']);
      return true;
    }
    return false;
  }
}