const HtmlWebPackPlugin = require('html-webpack-plugin');
const path = require('path');
const Dotenv = require('dotenv-webpack');
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = {
  // module.exports = function(app) {
  //   return {
  entry: ['babel-polyfill', './src/app/index.js'],
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      },
      {
        test: /\.(s*)css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.(png|woff|woff2|eot|ttf|svg)$/,
        loader: 'url-loader?limit=100000'
      }
    ]
  },
  resolve: {
    extensions: ['*', '.js', '.jsx'],
    alias: {
      Components: path.resolve(__dirname, 'src/components'),
      Utils: path.resolve(__dirname, 'src/utils')
    }
  },
  output: {
    path: `${__dirname}/dist`,
    publicPath: '/',
    filename: 'bundle.js'
  },
  devServer: {
    https: false, // must be
    disableHostCheck: true,
    historyApiFallback: true,
    compress: true,
    port: 80,
    host: '0.0.0.0',
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': 'true',
      'Access-Control-Allow-Methods':
        'ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL',
      'Access-Control-Max-Age': '3600',
      'Access-Control-Allow-Headers':
        'Origin, X-Requested-With, Content-Type, Accept, Key, Authorization'
    }
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8080',
    //     pathRewrite: {
    //       '^/api': ''
    //     }
    //   }
    // }
  },
  plugins: [
    new HtmlWebPackPlugin({
      filename: './index.html',
      template: './src/template.html'
    }),
    new Dotenv()
  ]
};
// };
