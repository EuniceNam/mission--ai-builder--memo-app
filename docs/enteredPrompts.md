# 세션 2
`CLAUDE.md`를 읽고 프로젝트 구조와 주요 파일 위치를 파악해줘.

참고로 2세션에서 아래 작업이 완료됐어:
- `MarkdownRenderer.kt` 신규 생성 (또는 App.kt 내부에 구현) — 정확한 위치는 CLAUDE.md 읽고 확인해줘
- 헤더(H1~H4), 볼드(**), 이탤릭(*) 파싱 구현 완료

파악 후 아래 작업을 순서대로 진행해줘.

---

## 1. 편집 기능 — 좌우 분할 레이아웃

- `App.kt`를 수정해서 화면을 `Row`로 50:50 분할
- 왼쪽: `BasicTextField`로 raw 마크다운 입력 (세로 스크롤 가능)
- 오른쪽: 기존 `MarkdownRenderer`에 입력값 실시간 연결
- 편집창과 미리보기가 같은 `mutableStateOf` 상태를 공유하도록 구성

## 2. 저장/불러오기

- 파일 다이얼로그는 `java.awt.FileDialog` 사용 (macOS 네이티브)
- `Cmd+S`: 현재 편집창 텍스트를 `.md` 파일로 저장
- `Cmd+O`: `.md` 파일을 열어 편집창에 로드
- 단축키는 `main.kt`의 Window에서 `onKeyEvent`로 처리
   - `isMetaPressed + key == Key.S` → 저장
   - `isMetaPressed + key == Key.O` → 열기

## 3. 글씨 크기 단축키

- `Cmd+=`: 폰트 크기 +2sp
- `Cmd+-`: 폰트 크기 -2sp
- `Cmd+0`: 폰트 크기 초기화 (기본값 16sp)
- 폰트 크기 상태를 `App.kt`에서 관리하고 `MarkdownRenderer`에 파라미터로 전달

---

## 작업 완료 후 `CLAUDE.md` 업데이트

아래 두 섹션을 수정해줘.

### TODO 섹션 갱신

```markdown
## TODO

- [x] 마크다운 렌더링 (Compose)
- [x] 편집 기능 (좌우 분할, 실시간 미리보기)
- [x] 저장/불러오기 (Cmd+S, Cmd+O)
- [x] 글씨 크기 단축키 (Cmd+=, Cmd+-, Cmd+0)
- [ ] 로컬 .md 파일 목록 읽기 (사이드바)
- [ ] 플랫폼별 파일 접근 (expect/actual)
- [ ] 형광펜 기능
```

### 마지막 작업 섹션 갱신 (기존 내용에 추가)

```markdown
- 2026-XX-XX: [구현한 기능 한 줄 요약] / 수정 파일: [파일명들] / 특이사항: [있으면 기록, 없으면 생략]
```

--- 결과로 나온 참고사항에 따라 ---
BasicTextField를 감싼 Column에 defaultMinSize를 추가해서
짧은 텍스트일 때도 빈 공간 전체가 클릭 가능하게 해줘.

---

# 세션 1 
다음 순서대로 진행해줘.

1. 프로젝트 구조 파악
    - 전체 디렉토리 구조
    - libs.versions.toml, build.gradle.kts 확인해서
      KMP 타겟 플랫폼, 주요 의존성, Compose 버전 정리
    - commonMain 등 소스셋에 있는 파일들 확인

2. CLAUDE.md 작성 (프로젝트 루트에 생성)
    - 프로젝트 개요: KMP 기반 macOS 마크다운 뷰어 메모장 앱
    - 확인된 기술 스택 (1에서 파악한 내용)
    - 타겟 플랫폼: macOS Desktop
    - 빌드 명령어
    - 코드 규칙:
        - 새 파일 생성 전 기존 구조 먼저 확인
        - 한 번에 하나의 파일/기능만 수정
        - 불필요한 주석 추가 금지
        - 함수는 간결하게 유지
    - TODO:
        1. 로컬 .md 파일 목록 읽기
        2. 마크다운 렌더링 (Compose)
        3. 플랫폼별 파일 접근 (expect/actual)

3. 작성된 CLAUDE.md 내용 보여줘

---

작업 완료 후 CLAUDE.md의 TODO 항목 중 완료된 것에 체크하고,
"마지막 작업" 섹션에 오늘 한 일을 한 줄로 기록해줘.

--- 아차 clear 안했다

CLAUDE.md를 읽고 프로젝트 구조를 파악한 뒤 아래 작업을 순서대로 진행해줘.

1. commonMain에 MarkdownRenderer.kt 생성
   - 입력: String (마크다운 텍스트)
   - 출력: Compose UI
   - 지원할 문법:
      - 헤더 (# ## ### ####, 4레벨까지만)
      - 볼드 (**text**)
      - 이탤릭 (*text*)

2. desktopMain에 App.kt (또는 기존 진입점 파일) 수정
   - 하드코딩된 샘플 마크다운 문자열을 MarkdownRenderer에 넘겨서 화면에 표시
   - 샘플 텍스트:
     # 제목
     ## 소제목
     일반 텍스트, **볼드**, *이탤릭*

3. 실행해서 확인할 수 있는 상태로 마무리해줘

작업 완료 후 CLAUDE.md의 TODO에서 완료 항목 체크하고,
"마지막 작업" 섹션에 한 줄로 기록해줘.