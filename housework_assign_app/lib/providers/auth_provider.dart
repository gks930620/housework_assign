// providers/auth_provider.dart
// 현재 로그인상태 등을 관리
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

final authProvider = StateNotifierProvider<AuthNotifier, bool>((ref) => AuthNotifier());

class AuthNotifier extends StateNotifier<bool> {
  AuthNotifier() : super(false);
  final storage = const FlutterSecureStorage();

  Future<void> login(String access, String refresh) async {
    await storage.write(key: 'accessToken', value: access);
    await storage.write(key: 'refreshToken', value: refresh);
    state = true;
  }

  Future<void> logout() async {
    await storage.deleteAll();
    state = false;
  }
}