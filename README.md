# practice_flatgeobuf

## 参考情報

- [flatgeobuf / flatgeobuf (github)](https://github.com/flatgeobuf/flatgeobuf)
- [flatgeobuf.org](https://flatgeobuf.org/)
- [geotools.org](https://www.geotools.org/)
- [geotools / geotools](https://github.com/geotools/geotools)
  - [repository内を `flatgeobuf` で検索](https://github.com/search?q=repo%3Ageotools%2Fgeotools%20flatgeobuf&type=code)
    - プロダクションコードも、テストコードも `modules/unsupported` 配下に置かれてる
    - (geobuf もまだ unsupported 配下に置かれてる)

## 環境構築

1. `$ brew install --cask corretto`
2. `$ brew install --cask corretto17`

## [flatgeobuf / flatgeobuf (github)](https://github.com/flatgeobuf/flatgeobuf/tree/master/src/ts)

1. `$ git clone git@github.com:flatgeobuf/flatgeobuf.git`
2. `$ cd flatgeobuf`
3. `$ asdf local nodejs 20.5.1`
4. `$ yarn`
5. `$ cd examples/node`
6. `$ asdf local nodejs 20.5.1`
7. `$ yarn`
8. `$ node index.js`
9. `deserialize.js` を編集
    - `console.log(features)` → `console.log(JSON.stringify(features))`
10. `$ node deserialize.js > deserialize.json`
11. `$ node streamtest.js > streamtest.json`
