
const express = require('express')
const app = express()
app.use(express.urlencoded())
var path = require('path')
const uiRouter = require("./api");
const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({
  extended: false
}))
app.use(bodyParser.json())
app.use(uiRouter);
const cors = require('cors')
const { request } = require('express')
app.use(cors())
console.log("css path: ");
console.log(path.join(__dirname, '../client'));
app.use(express.static(path.join(__dirname, '..', '/client')));
app.engine('html', require('express-art-template'))
app.use('/public/', express.static('./public/'))


app.listen(801, () => {
  console.log('gogogo');
})