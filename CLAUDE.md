# memoApp

KMP 기반 macOS 마크다운 뷰어 메모장 앱

## 기술 스택

| 항목 | 버전 |
|------|------|
| Kotlin | 2.3.20 |
| Compose Multiplatform | 1.10.3 |
| Compose Hot Reload | 1.0.0 |
| Material3 | 1.10.0-alpha05 |
| AndroidX Lifecycle | 2.10.0 |
| kotlinx-coroutines | 1.10.2 |
| Gradle | 8.14.3 |

## 타겟 플랫폼

- **macOS Desktop** (JVM 타겟 단독 — `kotlin { jvm() }`)
- 배포 포맷: DMG, MSI, Deb (`nativeDistributions`)

## 프로젝트 구조

```
memoApp/
├── composeApp/
│   └── src/
│       ├── jvmMain/kotlin/com/back/
│       │   ├── main.kt        # 앱 진입점 (Window + application)
│       │   ├── App.kt         # 루트 Composable
│       │   ├── Greeting.kt    # 인사말 로직
│       │   └── Platform.kt    # JVM 플랫폼 정보
│       └── jvmTest/kotlin/com/back/
│           └── ComposeAppDesktopTest.kt
├── gradle/
│   └── libs.versions.toml     # 버전 카탈로그
└── build.gradle.kts
```

소스셋: `commonMain` (Compose UI 의존성) + `jvmMain` (desktop, coroutines-swing)

## 빌드 명령어

```bash
# 실행
./gradlew :composeApp:run

# DMG 패키징
./gradlew :composeApp:packageDmg

# 테스트
./gradlew :composeApp:jvmTest
```

## 코드 규칙

- 새 파일 생성 전 기존 구조 먼저 확인
- 한 번에 하나의 파일/기능만 수정
- 불필요한 주석 추가 금지
- 함수는 간결하게 유지

## TODO

- [x] 마크다운 렌더링 (Compose)
- [x] 편집 기능 (좌우 분할, 실시간 미리보기)
- [x] 저장/불러오기 (Cmd+S, Cmd+O)
- [x] 글씨 크기 단축키 (Cmd+=, Cmd+-, Cmd+0)
- [ ] 로컬 `.md` 파일 목록 읽기 (사이드바)
- [ ] 플랫폼별 파일 접근 (`expect`/`actual`)
- [ ] 형광펜 기능

## 마지막 작업

- 2026-04-30: 프로젝트 구조 파악 및 CLAUDE.md 초기 작성
- 2026-04-30: MarkdownRenderer 구현 (헤더 4레벨, 볼드, 이탤릭) 및 App.kt 샘플 연동
- 2026-04-30: 좌우 분할 편집기 + 저장/불러오기(Cmd+S/O) + 글씨 크기 단축키(Cmd+=/-/0) 구현 / 수정 파일: main.kt, App.kt, MarkdownRenderer.kt