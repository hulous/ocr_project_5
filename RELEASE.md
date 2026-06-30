# Release Notes

## 0.0.1

This release includes updates focused on code quality, accessibility, and Angular bootstrap behavior:

- Updated Jest configuration to use `String.raw` for regex path patterns.
- Replaced `window` references with `globalThis` in application and Jest setup code.
- Marked injected dependencies and fixed service path constants as `readonly` where they are not reassigned.
- Converted Angular app bootstrap to use top-level `await` with `bootstrapApplication(...)`.
- Added explicit `id` and `for` associations for login and register inputs to improve accessibility.
- No SCSS changes were required for these form accessibility updates.
