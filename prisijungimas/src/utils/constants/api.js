// const host = process.env.HOST;
// const port = process.env.BACKEND_PORT; // 80 PORT netinka

// const pre = `http://${host}:${port}`;
const pre = `http://localhost:8080`;

export const headers = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Credentials': 'true',
  'Access-Control-Allow-Methods':
    'ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL',
  'Access-Control-Max-Age': '3600',
  'Access-Control-Allow-Headers':
    'Origin, X-Requested-With, Content-Type, Accept, Key, Authorization'
};
export const userRegister = `${pre}/api/Users/create`;
export const userLogin = `${pre}/api/Users/login/`;
export const getUser = `${pre}/api/Users/username/`;
