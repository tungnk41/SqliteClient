import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Main {

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase().replaceAll("đ", "d");
    }

    public static void main(String[] args) {
        try {
            SqliteClient client = new SqliteClient("/home/tuhn/Project/SqliteClient/db/location.db");


            client.normalizeProvinceData();

            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
