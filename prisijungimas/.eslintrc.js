module.exports = {
  extends: ['airbnb', 'prettier'],
  parser: 'babel-eslint',
  plugins: ['prettier'],
  rules: {
    'react/jsx-filename-extension': 'off',
    'prettier/prettier': ['error'],
    'no-underscore-dangle': 'off',
    'react/jsx-one-expression-per-line': 'off',
    'import/prefer-default-export': 'off',
    'jsx-a11y/interactive-supports-focus': 'off',
    'jsx-a11y/click-events-have-key-events': 'off',
    radix: 'off'
  },
  globals: {
    fetch: 'writeable',
    document: 'writeable',
    window: 'writeable',
    localStorage: true,
  }
};
