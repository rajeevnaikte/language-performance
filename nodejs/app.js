const express = require('express');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');


const indexRouter = require('./routes/index');

const app = express();

app.use(bodyParser.json());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/', indexRouter);

module.exports = app;
