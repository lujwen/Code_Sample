const express = require('express')
const router = express.Router();
const connection = require('./sql')
const app = express()
app.use(express.urlencoded())

var path = require('path')
__dirname = path.resolve(path.dirname(''));

const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({
  extended: false
}))
app.use(bodyParser.json())
const cors = require('cors')
app.use(cors())
router.get('/', (req, res) => {
  res.render(path.join(__dirname, '..', 'client/index.html'));
})
router.post('/getData', async (req, res) => {
  const selsql = `select * from notedb`
  connection.query(selsql, (err, result) => {
    res.json({
      msg: result,
      code: 1
    })
  })
});

router.post('/addData', async (req, res) => {
  const { title, note } = req.body
  const resql = `insert into notedb(title, note) values('${title}','${note}')`
  connection.query(resql, (err, data) => {
    if (err) {
      console.log(err);
      res.json({
        msg: 'add faild',
        code: 0
      })
    } else {
      console.log(data);
      res.json({
        msg: 'add success',
        code: 1
      })
    }
  })
});

router.post('/editData', async (req, res) => {
  const { title, note, id } = req.body
  const editsql = `update notedb set title= '${title}',note='${note}' where id='${id}'`
  connection.query(editsql, (err, result) => {
    res.json({
      msg: 'edit success',
      code: 1
    })
  })
});

router.get('/showDetails', (req, res) => {
  var id = req.query.id
  const selsql = `select * from notedb where id='${id}'`
  connection.query(selsql, (err, result) => {
    res.render(path.join(__dirname, '..', 'client/details.html'), { title: result[0].title, comments: result[0].note, id: result[0].id })
  }) 
});

router.post('/deleteItem', async (req, res) => {
  const { title, note, id } = req.body
  const editsql = `delete from notedb where id='${id}'`
  connection.query(editsql, (err, result) => {
    res.json({
      msg: 'delete success',
      code: 1
    })
  })
});





module.exports = router;