// screens/login_screen.dart
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '../providers/auth_provider.dart';
import 'home_screen.dart';

class LoginScreen extends ConsumerWidget {
  const LoginScreen({super.key});

  Future<void> loginWithKakao(BuildContext context, WidgetRef ref) async {
    try {
      final token = await (await isKakaoTalkInstalled()
          ? UserApi.instance.loginWithKakaoTalk()
          : UserApi.instance.loginWithKakaoAccount());

      final res = await http.post(
        Uri.parse('http://10.0.2.2:8080/auth/token-login/kakao'),
        body: jsonEncode(token.accessToken),
        headers: {'Content-Type': 'application/json'},
      );

      if (res.statusCode == 200) {
        final data = jsonDecode(res.body);
        await ref.read(authProvider.notifier).login(data['accessToken'], data['refreshToken']);
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const HomeScreen()));
      }
    } catch (e) {
      print('❌ 카카오 로그인 실패: $e');
    }
  }

  Future<void> loginWithGoogle(BuildContext context, WidgetRef ref) async {
    try {
      final GoogleSignIn googleSignIn = GoogleSignIn();
      final account = await googleSignIn.signIn();
      final auth = await account?.authentication;
      final accessToken = auth?.accessToken;

      final res = await http.post(
        Uri.parse('http://10.0.2.2:8080/auth/token-login/google'),
        body: jsonEncode(accessToken),
        headers: {'Content-Type': 'application/json'},
      );

      if (res.statusCode == 200) {
        final data = jsonDecode(res.body);
        await ref.read(authProvider.notifier).login(data['accessToken'], data['refreshToken']);
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const HomeScreen()));
      }
    } catch (e) {
      print('❌ 구글 로그인 실패: $e');
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(24.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text("가사분담앱", style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
                const SizedBox(height: 40),
                ElevatedButton(
                  onPressed: () => loginWithKakao(context, ref),
                  style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFEE500), foregroundColor: Colors.black),
                  child: const Text("카카오로 시작하기"),
                ),
                const SizedBox(height: 16),
                ElevatedButton(
                  onPressed: () => loginWithGoogle(context, ref),
                  child: const Text("구글로 시작하기"),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}