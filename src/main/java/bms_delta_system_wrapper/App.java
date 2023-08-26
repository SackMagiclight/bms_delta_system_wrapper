package bms_delta_system_wrapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmt.lib.bms.BmsContent;
import com.lmt.lib.bms.BmsException;
import com.lmt.lib.bms.bemusic.BeMusicHeader;
import com.lmt.lib.bms.bemusic.BeMusicRatingType;
import com.lmt.lib.bms.bemusic.BeMusicScoreBuilder;
import com.lmt.lib.bms.bemusic.BeMusicSpec;
import com.lmt.lib.bms.bemusic.BeMusicStatistics;
import com.lmt.lib.bms.bemusic.BeMusicStatisticsBuilder;

public class App {
    public static void main(String[] args) {
        App.getRating(args);
    }

    public static String getRating(String[] args) {
        try {
            BmsContent content = BeMusicSpec.loadContentFrom(Path.of(args[0]), false);

            // 読み込まれたBMSファイルからヘッダ情報を構築し、内容を表示します
            BeMusicHeader header = new BeMusicHeader(content);

            BeMusicScoreBuilder scoreBuilder = new BeMusicScoreBuilder(content).setSeekMeasureLine(true) // 小節線を含む
                    .setSeekVisible(true) // 可視オブジェを含む
                    .setSeekInvisible(false) // 不可視オブジェは無視する
                    .setSeekLandmine(true) // 地雷オブジェを含む
                    .setSeekBgm(false) // BGMは無視する
                    .setSeekBga(false) // BGA(レイヤー、ミス画像含む)は無視する
                    .setSeekText(false); // テキストは無視する;
            BeMusicStatisticsBuilder builder = new BeMusicStatisticsBuilder(header,
                    scoreBuilder.createScore())
                    .addRating(BeMusicRatingType.COMPLEX, BeMusicRatingType.DELTA,
                            BeMusicRatingType.GIMMICK,
                            BeMusicRatingType.HOLDING, BeMusicRatingType.POWER,
                            BeMusicRatingType.RHYTHM,
                            BeMusicRatingType.SCRATCH);
            BeMusicStatistics statistics = builder.statistics();

            int _COMPLEX = statistics.getRating(BeMusicRatingType.COMPLEX);
            int _GIMMICK = statistics.getRating(BeMusicRatingType.GIMMICK);
            int _HOLDING = statistics.getRating(BeMusicRatingType.HOLDING);
            int _POWER = statistics.getRating(BeMusicRatingType.POWER);
            int _RHYTHM = statistics.getRating(BeMusicRatingType.RHYTHM);
            int _SCRATCH = statistics.getRating(BeMusicRatingType.SCRATCH);

            Map<String, Integer> returnMap = new HashMap<String, Integer>() {
                {
                    put("COMPLEX", _COMPLEX / 100);
                    put("GIMMICK", _GIMMICK / 100);
                    put("HOLDING", _HOLDING / 100);
                    put("POWER", _POWER / 100);
                    put("RHYTHM", _RHYTHM / 100);
                    put("SCRATCH", _SCRATCH / 100);
                }
            };

            ObjectMapper objectMapper = new ObjectMapper();
            String jacksonData = objectMapper.writeValueAsString(returnMap);
            System.out.println(jacksonData);
            return jacksonData;

        } catch (IOException e) {
            e.printStackTrace();
            return "{\"ERROR\":\"I/O error.\"}";
        } catch (BmsException e) {
            e.printStackTrace();
            return "{\"ERROR\":\"BMS parse error.\"}";
        }
    }
}
