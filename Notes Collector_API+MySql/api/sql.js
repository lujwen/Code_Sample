const mysql = require('mysql2')
// const mysql = require('mysql')
const connection = mysql.createConnection({
  host: '127.0.0.1',
  user: 'root',
  password: 'password',
  database: 'notedb',
  port: '3306'
})
connection.connect((err) => {
  if (err) return console.log('Connection failed');
  console.log("connection succeeded");
});

module.exports = connection