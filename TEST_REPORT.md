# Test Report

This file summarizes the front-end and back-end test status, report locations, and commands.

## Front-end test report

- Location: `front`
- Primary test command:

```bash
cd front
npm test
```

- Coverage command:

```bash
cd front
npm run test:coverage
```

- Expected report artifacts:
  - console test summary from Jest
  - coverage output under `front/coverage/` when coverage is enabled

- Current report status:
  - Front-end tests passed successfully.
  - Jest results: 11 test suites passed, 28 tests passed.
  - Coverage generated at `front/coverage/`.
  - Coverage summary: 85.16% statements, 47.72% branches, 78.68% functions, 83.97% lines.
  - Detailed HTML report available at `front/coverage/lcov-report/index.html`.

## Back-end test report

- Location: `back`
- Primary test command:

```bash
cd back
./mvnw test
```

- Expected report artifacts:
  - `back/target/surefire-reports/TEST-*.xml`
  - `back/target/surefire-reports/*.txt`

- Current report status:
  - Back-end tests were rerun successfully using the installed local Maven and safer Surefire options.
  - Generated test report artifacts are available at `back/target/surefire-reports/`.
  - Generated Jacoco coverage report is available at `back/target/site/jacoco/index.html`.
  - Total tests run: 72
  - Failures: 0
  - Errors: 0
  - Skipped: 0
  - Total reported runtime: 33.83 seconds
  - Back-end coverage summary: 86.71% instructions, 61.63% branches, 90.43% lines, 84.03% methods, 71.60% complexity, 80.77% classes.
  - The rerun used Maven options: `-DforkCount=0 -DreuseForks=false -DargLine="-Djdk.attach.allowAttachSelf=true -Dnet.bytebuddy.agent.attacher.dump=/tmp/bytebuddy-attach.log"`.
  - This resolved the previous Mockito/ByteBuddy self-attach initialization issue.

## Report generation guidance

1. Generate front-end reports:

```bash
cd front
npm install
npm test
npm run test:coverage
```

2. Generate back-end reports:

```bash
cd back
./mvnw test
```

3. Inspect generated back-end report files:

```bash
ls back/target/surefire-reports
```

4. Update this file after running tests to capture actual pass/fail counts and report artifacts.
