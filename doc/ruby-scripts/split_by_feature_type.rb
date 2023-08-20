require 'json'
require 'debug'

# geometry.type によってgeojsonを分割するスクリプト
# bundle exec ruby split_by_feature_type.rb 分割対象の元のgeojson polygonだけ抜き出したgeojson
# bundle exec ruby split_by_feature_type.rb A27-21_08_GML_CRS84.geojson A27-21_08_GML_CRS84_polygon.geojsonruby

read_file = ARGV[0]
polygon_geojson_file = ARGV[1]

def split(read_file, polygon_geojson_file)
  original_json = nil;
  File.open("../A27-21_53_GML_geojson_CRS84/#{read_file}", 'r') do |original_file|
    original_json = JSON.load(original_file)
  end

  feature_array = original_json["features"]

  polygon_feature_array = []
  multiPolygon_feature_array = []
  other_feature_array = []

  feature_array.each do |feature|
    geometry_type = feature["geometry"]["type"]

    if (geometry_type == "Polygon")
      polygon_feature_array.push(feature)
    elsif (geometry_type == "MultiPolygon")
      multiPolygon_feature_array.push(feature)
    else
      other_feature_array.push(feature)
    end
  end

  polygon_geojson = generate_geojson(original_json, polygon_feature_array)
  multiPolygon_geojson = generate_geojson(original_json, multiPolygon_feature_array)

  other_type_array = other_feature_array.map { |feature| feature["geometry"]["type"]}
  puts(other_type_array)

  File.open("../21_53_GML_split_geojson_CRS84/#{polygon_geojson_file}", 'w') do |file|
    file.puts(JSON.pretty_generate(polygon_geojson))
  end
end

def generate_geojson(original_json, feature_array)
  polygon_geojson = {}
  polygon_geojson["type"] = original_json["type"]
  polygon_geojson["name"] = original_json["name"]
  polygon_geojson["crs"] = original_json["crs"]
  polygon_geojson["features"] = feature_array

  return polygon_geojson
end


split(read_file, polygon_geojson_file)
