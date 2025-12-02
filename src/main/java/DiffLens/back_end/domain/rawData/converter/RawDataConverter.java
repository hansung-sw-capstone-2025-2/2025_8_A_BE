package DiffLens.back_end.domain.rawData.converter;

public interface RawDataConverter<T, V> {
    T convert(V rawJson);
}
