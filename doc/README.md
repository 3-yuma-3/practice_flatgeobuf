# doc

## オープンデータを取得して測位系を変換する

### `A27-21_53_GML.zip`

- [TOP > 国土数値情報 > 小学校区データ > 関東地方 > 令和3年](https://nlftp.mlit.go.jp/ksj/gml/datalist/KsjTmplt-A27-v3_0.html)
- `R3_Terms_of_use_municipality_data.xlsx`
  - このデータの各市町村の使用許諾条件
- `urn:ogc:def:crs:EPSG::6668`
- `ポリゴン` と記載されているが、 `マルチポリゴン` も含まれていることに注意

### `A27-21_53_GML_geojson_CRS84`

- `A27-21_53_GML.zip` が `EPSG::6668` なので、QGISで `CRS84` に変換する
  - `EPSG::4326` を選択しても、実際は `CRS84` で出力される
- `A27_001`: `行政区域コード`
- `A27_002`: `設置主体`
- `A27_003`: `学校コード`
- `A27_004`: `名称`
  - `A27_005`: `所在地`

```json
"properties": {
  "A27_001": "08542",
  "A27_002": "五霞町立",
  "A27_003": "B108254200026",
  "A27_004": "五霞西小学校",
  "A27_005": "猿島郡五霞町元栗橋1072"
},
```

### `A27-21_53_GML_split_geojson_CRS84`

- `ruby-scripts` で `geometry.type (polygon, multipolygon, ...)` でpolygonでだけ抜き出す
