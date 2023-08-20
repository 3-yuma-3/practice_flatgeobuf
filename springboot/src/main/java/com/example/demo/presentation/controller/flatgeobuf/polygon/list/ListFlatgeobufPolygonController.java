package com.example.demo.presentation.controller.flatgeobuf.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.flatgeobuf.polygon.list.ListFlatGeobufPolygonUsecase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/flatgeobuf/polygon")
@Slf4j
public class ListFlatgeobufPolygonController {
    private final ListFlatGeobufPolygonUsecase listFlatGeobufPolygonUsecase;

    @GetMapping()
    public List<School> listFlatGeobufPolygon(
    ) {
        List<School> file = listFlatGeobufPolygonUsecase.getList();

        return file;
    }

//    @GetMapping()
//    public ResponseEntity<ByteBuffer> download() {
//        ByteBuffer bb = listFlatGeobufPolygonUsecase.write();
//
//        return ResponseEntity.ok()
//            .contentType(MediaType.valueOf("application/octet-stream"))
//            .header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + "hogehoge" + "\"")
//            .body(bb);
//    }

//    @GetMapping()
//    public void hoge(HttpServletResponse response) {
//        ByteBuffer bb = listFlatGeobufPolygonUsecase.write();
//
//        byte[] byteArray = new byte[bb.remaining()];
//
//        //ローカルファイルの読み込み
//        try (OutputStream os = response.getOutputStream()) {
//            //レスポンスヘッダーの設定
//            response.setHeader("Content-Disposition", "attachment; filename=tokyoStation.fgb");
//            response.setContentType("application/octet-stream");
//            //レスポンスボディへの書き込み
////            IOUtils.copy(bb, os);
//            os.write(byteArray);
//            os.flush();
//        } catch (IOException e) {
//            log.error("IOException が発生: ", e);
//        }
//    }
}
