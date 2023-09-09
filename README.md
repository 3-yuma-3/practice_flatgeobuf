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

## 色々試した結果

### 関係してそうなissue

- [JS/TS issue RangeError: start offset of Float64Array should be a multiple of 8 #5325 (google/flatbuffers)](https://github.com/google/flatbuffers/issues/5325)
  - feature.properties 有りの場合はこのエラーが出てjavascriptでdeserializeできない
- [Update to Rust flatbuffers 2.0 #105 (flatgeobuf/flatgeobuf)](https://github.com/flatgeobuf/flatgeobuf/pull/105)
- [Java/C#/Python prefixed size support #4445 (google/flatbuffers)](https://github.com/google/flatbuffers/pull/4445)

### feature.properties 無しの場合はjavaでserializeしたfgbをjavascriptでdeserializeできる

- springbootで意図した通りのgeojsonを作れていることの確認
  - `$ curl http://localhost:8080/geojson/polygon/original_geojson_no_properties_java.json > original_geojson_no_properties_java.json`
- springbootからfgbを取得
  - `$ curl http://localhost:8080/flatgeobuf/polygon/original_geojson_no_properties_java.fgb > original_geojson_no_properties_java.fgb`
- flatgeobuf/example/node で同じgeojsonをserialize, deserializeできることを確認

  - ```js
      /* eslint-disable no-undef */
      import { geojson } from 'flatgeobuf'
      import { readFileSync, writeFileSync }  from 'fs'

      const originalGeojson = JSON.parse(readFileSync('./original_geojson_no_properties_java.json'))
      console.log(JSON.stringify(originalGeojson, undefined, 1))
      const serialized = geojson.serialize(originalGeojson)
      writeFileSync('./original_geojson_no_properties_js.fgb', serialized)
      const bufferJs = readFileSync('./original_geojson_no_properties_js.fgb')
      const deserialized = geojson.deserialize(new Uint8Array(bufferJs))
      console.log(JSON.stringify(deserialized, undefined, 1))
    ```

- javaで生成したfgbをjavascriptでdeserializeできることを確認

  - ```js
      /* eslint-disable no-undef */
      import { geojson } from 'flatgeobuf'
      import { readFileSync, writeFileSync }  from 'fs'

      const bufferJs = readFileSync('./original_geojson_no_properties_java.fgb')
      const deserialized = geojson.deserialize(new Uint8Array(bufferJs))
      console.log(JSON.stringify(deserialized, undefined, 1))
    ```

### feature.properties 有りの場合はjavaでserializeしたfgbをjavascriptでdeserializeできない

- springbbootで意図した通りのgeojsonを作れていることの確認
  - `$ curl http://localhost:8080/geojson/polygon/original_geojson_with_properties_java.json > original_geojson_with_properties_java.json`
- springbootからfgbを取得
  - `$ curl http://localhost:8080/flatgeobuf/polygon/original_geojson_with_properties_java.fgb > original_geojson_with_properties_java.fgb`
- flatgeobuf/example/node で同じgeojsonをserialize, deserializeできることを確認

  - ```js
      /* eslint-disable no-undef */
      import { geojson } from 'flatgeobuf'
      import { readFileSync, writeFileSync }  from 'fs'

      const originalGeojson = JSON.parse(readFileSync('./original_geojson_with_properties_java.json'))
      console.log(JSON.stringify(originalGeojson, undefined, 1))
      const serialized = geojson.serialize(originalGeojson)
      writeFileSync('./original_geojson_with_properties_js.fgb', serialized)
      const bufferJs = readFileSync('./original_geojson_with_properties_js.fgb')
      const deserialized = geojson.deserialize(new Uint8Array(bufferJs))
      console.log(JSON.stringify(deserialized, undefined, 1))
    ```

- javaで生成したfgbをjavascriptでdeserializeしようとする
  - `RangeError: start offset of Float64Array should be a multiple of 8` が発生

  - ```js
      /* eslint-disable no-undef */
      import { geojson } from 'flatgeobuf'
      import { readFileSync, writeFileSync }  from 'fs'

      const bufferJs = readFileSync('./original_geojson_with_properties_java.fgb')
      const deserialized = geojson.deserialize(new Uint8Array(bufferJs))
      console.log(JSON.stringify(deserialized, undefined, 1))
    ```

### それぞれのファイルの差分

- feature.properties 無しの場合でも、java, javascriptで生成したそれぞれのfgbファイルに差分がある

```sh
❯ ll
216B  9  9 15:39 original_geojson_no_properties_java.fgb
575B  9  9 15:33 original_geojson_no_properties_java.json
208B  9  9 15:53 original_geojson_no_properties_js.fgb
300B  9  9 15:35 original_geojson_with_properties_java.fgb
609B  9  9 15:31 original_geojson_with_properties_java.json
256B  9  9 15:55 original_geojson_with_properties_js.fgb
```

#### feature.properties 無しの場合にjavascriptでdeserializeした時に仕込んだログの中身

- `examples/node/node_modules/flatgeobuf/lib/mjs/geojson/geometry.js`
  - `function toGeoJsonCoordinates(geometry, type)` の直後にログを仕込む

    - ```js
        console.log(JSON.stringify(geometry))
      ```

- `examples/node/node_modules/flatgeobuf/lib/mjs/flat-geobuf/geometry.js`
  - `xyArray()` にログを仕込む

    - ```js
        console.log(`offset: ${offset}`)
        console.log(`this.bb.bytes().buffer: ${this.bb.bytes().buffer}`)
        console.log(`this.bb.bytes().byteOffset + this.bb.__vector(this.bb_pos + offset): ${this.bb.bytes().byteOffset + this.bb.__vector(this.bb_pos + offset)}`)
        console.log(`this.bb.__vector_len(this.bb_pos + offset): ${this.bb.__vector_len(this.bb_pos + offset)}`)
      ```

- `original_geojson_no_properties_java.fgb`
  - `original_geojson_no_properties_java_deserialize_log.json`
- `original_geojson_no_properties_js.fgb`
  - `original_geojson_no_properties_js_deserialize_log.json`
- `original_geojson_with_properties_java.fgb`
  - `original_geojson_with_properties_java_deserialize_log.json`
- `original_geojson_with_properties_js.fgb`
  - `original_geojson_with_properties_js_deserialize_log.json`
- javaで生成したfgbをdeserializeする際、feature.properties有りでも無しでも、offsetは12になっていた。javascriptではoffsetは8。
- deserializeできない `original_geojson_with_properties_java.fgb` は確かに `new Float64Array()` に渡すoffset(第2引数)が８で割り切れない220になっていた
