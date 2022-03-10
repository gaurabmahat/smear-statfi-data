import java.util.List;

public interface JsonToResultConverter {
    List<Result> convert(String json);
}
