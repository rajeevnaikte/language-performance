const express = require('express');
const { saveAndFetch, calculateMonthlyInterest } = require('../controller/app-controller');
const jwt = require('jsonwebtoken');

const router = express.Router();
const accessTokenSecret = 'youraccesstokensecret';

const authenticateJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (authHeader) {
    const token = authHeader.split(' ')[1];

    jwt.verify(token, accessTokenSecret, (err, user) => {
      if (err) {
        return res.sendStatus(403);
      }

      req.user = user;
      next();
    });
  } else {
    res.sendStatus(401);
  }
};

router.post('/save', authenticateJWT, function(req, res, next) {
  saveAndFetch(req.body)
    .then(json => {
      res.json(json);
    })
    .catch(reason => {
      res.status(500).send();
    });
});

router.get('/calculate/monthly/interest', authenticateJWT, function(req, res, next) {
  res.json(calculateMonthlyInterest(
    parseInt(req.query.months),
    parseFloat(req.query.principalAmount),
    parseFloat(req.query.annualRate)
  ));
});

module.exports = router;
