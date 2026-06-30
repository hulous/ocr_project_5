export default {
  preset: 'jest-preset-angular',
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  moduleNameMapper: {
    '^@core/(.*)$': '<rootDir>/src/app/core/$1',
  },
  transform: {
    '^.+\\.(ts|js|mjs|html|svg)$': ['jest-preset-angular/build/index.js', {
      tsconfig: '<rootDir>/tsconfig.spec.json',
      stringifyContentPathRegex: '\\.(html|svg)$',
      useESM: true,
      diagnostics: {
        ignoreCodes: [151002]
      }
    }]
  },
  bail: false,
  verbose: false,
  collectCoverage: false,
  coverageDirectory: './coverage/jest',
  testPathIgnorePatterns: ['<rootDir>/node_modules/', '<rootDir>/src/test.ts', '\\.(int|e2e)\\.spec\\.ts$'],
  coveragePathIgnorePatterns: ['<rootDir>/node_modules/'],
  coverageThreshold: {
    global: {
      statements: 80,
    },
  },
  roots: ['<rootDir>'],
  modulePaths: ['<rootDir>'],
  moduleDirectories: ['node_modules'],
};
