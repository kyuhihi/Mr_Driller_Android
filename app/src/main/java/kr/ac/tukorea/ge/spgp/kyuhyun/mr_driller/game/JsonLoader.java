package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;


import android.content.Context;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.io.IOException;
        import java.io.InputStream;
        import java.nio.charset.StandardCharsets;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class JsonLoader {
    public static Map<String, List<JSONObject>> readAndCategorizeFrames(Context context, String filename) {
        Map<String, List<JSONObject>> categorizedFrames = new HashMap<>();

        // JSON 파일 읽기
        String json = loadJSONFromAsset(context, filename);

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray framesArray = jsonObject.getJSONArray("frames");

            for (int i = 0; i < framesArray.length(); i++) {
                JSONObject frameObject = framesArray.getJSONObject(i);
                String filenameValue = frameObject.getString("filename");
                String category = getCategoryFromFilename(filenameValue);

                if (!categorizedFrames.containsKey(category)) {
                    categorizedFrames.put(category, new ArrayList<>());
                }

                categorizedFrames.get(category).add(frameObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categorizedFrames;
    }

    private static String getCategoryFromFilename(String filename) {
        // 파일 이름에서 "_"을 기준으로 분리하여 첫 번째 부분을 카테고리로 사용
        String[] parts = filename.split("_");
        return parts[1];
    }
    private static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            // assets 폴더에서 파일 열기
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
