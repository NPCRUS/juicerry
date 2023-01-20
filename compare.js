const stringsSimilarity = require("string-similarity")

const argv = process.argv
const baseString = argv[2];
const targetString = argv[3].split(',');

const result = stringsSimilarity.findBestMatch(baseString, targetString)
console.log(`${result.bestMatch.target},${result.bestMatch.rating}`)