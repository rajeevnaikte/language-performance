const fs = require('fs').promises;
const { v4 } = require('uuid');
const winston = require('winston');

const logger = winston.createLogger({
  transports: [
    new winston.transports.Console()
  ]
});

const appController = async (json) => {
  logger.info('creating unique file name');
  const filePath = v4();

  logger.info('writing data to file');
  await fs.writeFile(filePath, JSON.stringify(json), 'utf-8');

  logger.info('reading data from file name')
  const result = JSON.parse(await fs.readFile(filePath, 'utf-8'));

  fs.unlink(filePath).catch(reason => logger.error(`failed to delete file ${filePath}. ${reason}`));

  return result;
}

const calculateMonthlyInterest = (months, principalAmount, annualRate) => {
  const result = [];
  for (let month = 1; month < months; month++) {
    const interest = principalAmount * annualRate / 12/ 100;
    principalAmount += interest;
    result.push({
      month,
      interest,
      balance: principalAmount
    });
  }

  return result;
}

module.exports = {
  saveAndFetch: appController,
  calculateMonthlyInterest
}
